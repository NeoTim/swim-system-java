// Copyright 2015-2019 SWIM.AI inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package swim.runtime.http;

import swim.codec.Decoder;
import swim.http.HttpBody;
import swim.http.HttpRequest;
import swim.http.HttpResponse;
import swim.http.HttpStatus;
import swim.runtime.HttpBinding;
import swim.runtime.LaneModel;
import swim.runtime.LaneRelay;
import swim.runtime.LaneView;
import swim.runtime.LinkBinding;
import swim.runtime.PushRequest;
import swim.warp.CommandMessage;

public class HttpLaneModel<View extends HttpLaneView<?>, U extends HttpLaneUplink> extends LaneModel<View, U> {
  @Override
  public String laneType() {
    return "http";
  }

  @Override
  protected U createUplink(LinkBinding link) {
    if (link instanceof HttpBinding) {
      return createHttpUplink((HttpBinding) link);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  protected U createHttpUplink(HttpBinding link) {
    return (U) new HttpLaneUplink(this, link);
  }

  @Override
  public void pushUp(PushRequest pushRequest) {
    pushRequest.didDecline();
  }

  @Override
  public void pushUpCommand(CommandMessage message) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void didOpenLaneView(View view) {
    // nop
  }

  public Decoder<Object> decodeRequest(HttpLaneUplink uplink, HttpRequest<?> request) {
    final Object views = this.views;
    HttpLaneView<?> view;
    Decoder<Object> decoder = null;
    if (views instanceof HttpLaneView) {
      view = (HttpLaneView<?>) views;
      decoder = view.dispatchDecodeRequest(uplink, request);
      if (decoder == null) {
        decoder = view.laneDecodeRequest(uplink, request);
      }
    } else if (views instanceof LaneView[]) {
      final LaneView[] viewArray = (LaneView[]) views;
      for (int i = 0, n = viewArray.length; i < n; i += 1) {
        view = (HttpLaneView<?>) viewArray[i];
        decoder = view.dispatchDecodeRequest(uplink, request);
        if (decoder == null) {
          decoder = view.laneDecodeRequest(uplink, request);
        }
        if (decoder != null) {
          break;
        }
      }
    }
    return decoder;
  }

  public void willRequest(HttpLaneUplink uplink, HttpRequest<?> request) {
    new HttpLaneRelayWillRequest<View>(this, uplink, request).run();
  }

  public void didRequest(HttpLaneUplink uplink, HttpRequest<Object> request) {
    new HttpLaneRelayDidRequest<View>(this, uplink, request).run();
  }

  public void doRespond(HttpLaneUplink uplink, HttpRequest<Object> request) {
    new HttpLaneRelayDoRespond<View>(this, uplink, request).run();
  }

  public void willRespond(HttpLaneUplink uplink, HttpResponse<?> response) {
    new HttpLaneRelayWillRespond<View>(this, uplink, response).run();
  }

  public void didRespond(HttpLaneUplink uplink, HttpResponse<?> response) {
    new HttpLaneRelayDidRespond<View>(this, uplink, response).run();
  }
}

final class HttpLaneRelayWillRequest<View extends HttpLaneView<?>> extends LaneRelay<HttpLaneModel<View, ?>, View> {
  final HttpLaneUplink uplink;
  final HttpRequest<?> request;

  HttpLaneRelayWillRequest(HttpLaneModel<View, ?> model, HttpLaneUplink uplink, HttpRequest<?> request) {
    super(model);
    this.uplink = uplink;
    this.request = request;
  }

  @Override
  protected boolean runPhase(View view, int phase, boolean preemptive) {
    if (phase == 0) {
      if (preemptive) {
        view.laneWillRequest(this.uplink, this.request);
      }
      return view.dispatchWillRequest(this.uplink, this.request, preemptive);
    } else {
      throw new AssertionError(); // unreachable
    }
  }
}

final class HttpLaneRelayDidRequest<View extends HttpLaneView<?>> extends LaneRelay<HttpLaneModel<View, ?>, View> {
  final HttpLaneUplink uplink;
  final HttpRequest<Object> request;

  HttpLaneRelayDidRequest(HttpLaneModel<View, ?> model, HttpLaneUplink uplink, HttpRequest<Object> request) {
    super(model);
    this.uplink = uplink;
    this.request = request;
  }

  @Override
  protected boolean runPhase(View view, int phase, boolean preemptive) {
    if (phase == 0) {
      if (preemptive) {
        view.laneDidRequest(this.uplink, this.request);
      }
      return view.dispatchDidRequest(this.uplink, this.request, preemptive);
    } else {
      throw new AssertionError(); // unreachable
    }
  }
}

final class HttpLaneRelayDoRespond<View extends HttpLaneView<?>> extends LaneRelay<HttpLaneModel<View, ?>, View> {
  final HttpLaneUplink uplink;
  final HttpRequest<Object> request;
  HttpResponse<?> response;

  HttpLaneRelayDoRespond(HttpLaneModel<View, ?> model, HttpLaneUplink uplink, HttpRequest<Object> request) {
    super(model);
    this.uplink = uplink;
    this.request = request;
  }

  @Override
  protected boolean runPhase(View view, int phase, boolean preemptive) {
    if (phase == 0) {
      Object response = this.response;
      if (response == null) {
        response = view.dispatchDoRespond(this.uplink, this.request, preemptive);
      }
      final boolean complete = response == Boolean.TRUE;
      if (complete) {
        response = view.laneDoRespond(this.uplink, this.request);
      }
      if (response instanceof HttpResponse<?>) {
        this.response = (HttpResponse<?>) response;
        return true;
      }
      return complete;
    } else {
      throw new AssertionError(); // unreachable
    }
  }

  @Override
  protected void done() {
    if (this.response == null) {
      this.response = HttpResponse.from(HttpStatus.NOT_FOUND).entity(HttpBody.empty());
    }
    this.uplink.writeResponse(this.response);
  }
}

final class HttpLaneRelayWillRespond<View extends HttpLaneView<?>> extends LaneRelay<HttpLaneModel<View, ?>, View> {
  final HttpLaneUplink uplink;
  final HttpResponse<?> response;

  HttpLaneRelayWillRespond(HttpLaneModel<View, ?> model, HttpLaneUplink uplink, HttpResponse<?> response) {
    super(model);
    this.uplink = uplink;
    this.response = response;
  }

  @Override
  protected boolean runPhase(View view, int phase, boolean preemptive) {
    if (phase == 0) {
      if (preemptive) {
        view.laneWillRespond(this.uplink, this.response);
      }
      return view.dispatchWillRespond(this.uplink, this.response, preemptive);
    } else {
      throw new AssertionError(); // unreachable
    }
  }
}

final class HttpLaneRelayDidRespond<View extends HttpLaneView<?>> extends LaneRelay<HttpLaneModel<View, ?>, View> {
  final HttpLaneUplink uplink;
  final HttpResponse<?> response;

  HttpLaneRelayDidRespond(HttpLaneModel<View, ?> model, HttpLaneUplink uplink, HttpResponse<?> response) {
    super(model);
    this.uplink = uplink;
    this.response = response;
  }

  @Override
  protected boolean runPhase(View view, int phase, boolean preemptive) {
    if (phase == 0) {
      if (preemptive) {
        view.laneDidRespond(this.uplink, this.response);
      }
      return view.dispatchDidRespond(this.uplink, this.response, preemptive);
    } else {
      throw new AssertionError(); // unreachable
    }
  }
}
