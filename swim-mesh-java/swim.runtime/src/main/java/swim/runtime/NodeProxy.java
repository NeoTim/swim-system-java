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

package swim.runtime;

import swim.api.agent.Agent;
import swim.api.agent.AgentFactory;
import swim.api.auth.Identity;
import swim.api.downlink.Downlink;
import swim.api.policy.Policy;
import swim.collections.HashTrieMap;
import swim.concurrent.Schedule;
import swim.concurrent.Stage;
import swim.store.StoreBinding;
import swim.structure.Value;
import swim.uri.Uri;

public class NodeProxy implements NodeBinding, NodeContext {
  protected final NodeBinding nodeBinding;
  protected NodeContext nodeContext;

  public NodeProxy(NodeBinding nodeBinding) {
    this.nodeBinding = nodeBinding;
  }

  @Override
  public final TierContext tierContext() {
    return this;
  }

  @Override
  public final HostBinding host() {
    return this.nodeContext.host();
  }

  @Override
  public final NodeBinding nodeWrapper() {
    return this.nodeBinding.nodeWrapper();
  }

  public final NodeBinding nodeBinding() {
    return this.nodeBinding;
  }

  @Override
  public final NodeContext nodeContext() {
    return this.nodeContext;
  }

  @Override
  public void setNodeContext(NodeContext nodeContext) {
    this.nodeContext = nodeContext;
    this.nodeBinding.setNodeContext(this);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T unwrapNode(Class<T> nodeClass) {
    if (nodeClass.isAssignableFrom(getClass())) {
      return (T) this;
    } else {
      return this.nodeContext.unwrapNode(nodeClass);
    }
  }

  @Override
  public Uri meshUri() {
    return this.nodeContext.meshUri();
  }

  @Override
  public Value partKey() {
    return this.nodeContext.partKey();
  }

  @Override
  public Uri hostUri() {
    return this.nodeContext.hostUri();
  }

  @Override
  public Uri nodeUri() {
    return this.nodeContext.nodeUri();
  }

  @Override
  public long createdTime() {
    return this.nodeBinding.createdTime();
  }

  @Override
  public Identity identity() {
    return this.nodeContext.identity();
  }

  @Override
  public Policy policy() {
    return this.nodeContext.policy();
  }

  @Override
  public Schedule schedule() {
    return this.nodeContext.schedule();
  }

  @Override
  public Stage stage() {
    return this.nodeContext.stage();
  }

  @Override
  public StoreBinding store() {
    return this.nodeContext.store();
  }

  @Override
  public HashTrieMap<Uri, LaneBinding> lanes() {
    return this.nodeBinding.lanes();
  }

  @Override
  public LaneBinding getLane(Uri laneUri) {
    return this.nodeBinding.getLane(laneUri);
  }

  @Override
  public LaneBinding openLane(Uri laneUri, LaneBinding lane) {
    return this.nodeBinding.openLane(laneUri, lane);
  }

  @Override
  public LaneBinding injectLane(Uri laneUri, LaneBinding lane) {
    return this.nodeContext.injectLane(laneUri, lane);
  }

  @Override
  public <A extends Agent> AgentFactory<A> createAgentFactory(Class<? extends A> agentClass) {
    return this.nodeContext.createAgentFactory(agentClass);
  }

  @Override
  public LinkBinding bindDownlink(Downlink downlink) {
    return this.nodeContext.bindDownlink(downlink);
  }

  @Override
  public void openDownlink(LinkBinding link) {
    this.nodeContext.openDownlink(link);
  }

  @Override
  public void closeDownlink(LinkBinding link) {
    this.nodeContext.closeDownlink(link);
  }

  @Override
  public void httpDownlink(HttpBinding http) {
    this.nodeContext.httpDownlink(http);
  }

  @Override
  public void pushDown(PushRequest pushRequest) {
    this.nodeContext.pushDown(pushRequest);
  }

  @Override
  public void openUplink(LinkBinding link) {
    this.nodeBinding.openUplink(link);
  }

  @Override
  public void httpUplink(HttpBinding http) {
    this.nodeBinding.httpUplink(http);
  }

  @Override
  public void pushUp(PushRequest pushRequest) {
    this.nodeBinding.pushUp(pushRequest);
  }

  @Override
  public void trace(Object message) {
    this.nodeContext.trace(message);
  }

  @Override
  public void debug(Object message) {
    this.nodeContext.debug(message);
  }

  @Override
  public void info(Object message) {
    this.nodeContext.info(message);
  }

  @Override
  public void warn(Object message) {
    this.nodeContext.warn(message);
  }

  @Override
  public void error(Object message) {
    this.nodeContext.error(message);
  }

  @Override
  public boolean isClosed() {
    return this.nodeBinding.isClosed();
  }

  @Override
  public boolean isOpened() {
    return this.nodeBinding.isOpened();
  }

  @Override
  public boolean isLoaded() {
    return this.nodeBinding.isLoaded();
  }

  @Override
  public boolean isStarted() {
    return this.nodeBinding.isStarted();
  }

  @Override
  public void open() {
    this.nodeBinding.open();
  }

  @Override
  public void load() {
    this.nodeBinding.load();
  }

  @Override
  public void start() {
    this.nodeBinding.start();
  }

  @Override
  public void stop() {
    this.nodeBinding.stop();
  }

  @Override
  public void unload() {
    this.nodeBinding.unload();
  }

  @Override
  public void close() {
    this.nodeBinding.close();
  }

  @Override
  public void willOpen() {
    this.nodeContext.willOpen();
  }

  @Override
  public void didOpen() {
    this.nodeContext.didOpen();
  }

  @Override
  public void willLoad() {
    this.nodeContext.willLoad();
  }

  @Override
  public void didLoad() {
    this.nodeContext.didLoad();
  }

  @Override
  public void willStart() {
    this.nodeContext.willStart();
  }

  @Override
  public void didStart() {
    this.nodeContext.didStart();
  }

  @Override
  public void willStop() {
    this.nodeContext.willStop();
  }

  @Override
  public void didStop() {
    this.nodeContext.didStop();
  }

  @Override
  public void willUnload() {
    this.nodeContext.willUnload();
  }

  @Override
  public void didUnload() {
    this.nodeContext.didUnload();
  }

  @Override
  public void willClose() {
    this.nodeContext.willClose();
  }

  @Override
  public void didClose() {
    this.nodeBinding.didClose();
  }

  @Override
  public void didFail(Throwable error) {
    this.nodeBinding.didFail(error);
  }
}
