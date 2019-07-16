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

package swim.http.header;

import org.testng.annotations.Test;
import swim.http.Http;
import swim.http.HttpAssertions;
import swim.http.HttpHeader;
import static swim.http.HttpAssertions.assertWrites;

public class SecWebSocketProtocolSpec {
  public void assertParses(String string, HttpHeader header) {
    HttpAssertions.assertParses(Http.standardParser().headerParser(), string, header);
  }

  @Test
  public void parseSecWebSocketProtocolHeaders() {
    assertParses("Sec-WebSocket-Protocol: chat", SecWebSocketProtocol.from("chat"));
    assertParses("Sec-WebSocket-Protocol: chat,superchat", SecWebSocketProtocol.from("chat", "superchat"));
    assertParses("Sec-WebSocket-Protocol: chat, superchat", SecWebSocketProtocol.from("chat", "superchat"));
    assertParses("Sec-WebSocket-Protocol: chat , superchat", SecWebSocketProtocol.from("chat", "superchat"));
  }

  @Test
  public void writeSecWebSocketProtocolHeaders() {
    assertWrites(SecWebSocketProtocol.from("chat"), "Sec-WebSocket-Protocol: chat");
    assertWrites(SecWebSocketProtocol.from("chat", "superchat"), "Sec-WebSocket-Protocol: chat, superchat");
  }
}
