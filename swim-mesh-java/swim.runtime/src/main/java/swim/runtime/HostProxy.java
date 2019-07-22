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

import swim.api.Downlink;
import swim.api.agent.Agent;
import swim.api.agent.AgentDef;
import swim.api.agent.AgentFactory;
import swim.api.auth.Credentials;
import swim.api.auth.Identity;
import swim.api.policy.Policy;
import swim.api.policy.PolicyDirective;
import swim.collections.HashTrieMap;
import swim.concurrent.Schedule;
import swim.concurrent.Stage;
import swim.store.StoreBinding;
import swim.structure.Value;
import swim.uri.Uri;

public class HostProxy implements HostBinding, HostContext {
  protected final HostBinding hostBinding;
  protected HostContext hostContext;

  public HostProxy(HostBinding hostBinding) {
    this.hostBinding = hostBinding;
  }

  @Override
  public final TierContext tierContext() {
    return this;
  }

  @Override
  public final PartBinding part() {
    return this.hostContext.part();
  }

  @Override
  public final HostBinding hostWrapper() {
    return this.hostBinding.hostWrapper();
  }

  public final HostBinding hostBinding() {
    return this.hostBinding;
  }

  @Override
  public final HostContext hostContext() {
    return hostContext;
  }

  @Override
  public void setHostContext(HostContext hostContext) {
    this.hostContext = hostContext;
    this.hostBinding.setHostContext(this);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T unwrapHost(Class<T> hostClass) {
    if (hostClass.isAssignableFrom(getClass())) {
      return (T) this;
    } else {
      return this.hostContext.unwrapHost(hostClass);
    }
  }

  @Override
  public Uri meshUri() {
    return this.hostContext.meshUri();
  }

  @Override
  public Value partKey() {
    return this.hostContext.partKey();
  }

  @Override
  public Uri hostUri() {
    return this.hostContext.hostUri();
  }

  @Override
  public Policy policy() {
    return this.hostContext.policy();
  }

  @Override
  public Schedule schedule() {
    return this.hostContext.schedule();
  }

  @Override
  public Stage stage() {
    return this.hostContext.stage();
  }

  @Override
  public StoreBinding store() {
    return this.hostContext.store();
  }

  @Override
  public boolean isConnected() {
    return this.hostBinding.isConnected();
  }

  @Override
  public boolean isRemote() {
    return this.hostBinding.isRemote();
  }

  @Override
  public boolean isSecure() {
    return this.hostBinding.isSecure();
  }

  @Override
  public boolean isPrimary() {
    return this.hostBinding.isPrimary();
  }

  @Override
  public void setPrimary(boolean isPrimary) {
    this.hostBinding.setPrimary(isPrimary);
  }

  @Override
  public boolean isReplica() {
    return this.hostBinding.isReplica();
  }

  @Override
  public void setReplica(boolean isReplica) {
    this.hostBinding.setReplica(isReplica);
  }

  @Override
  public boolean isMaster() {
    return this.hostBinding.isMaster();
  }

  @Override
  public boolean isSlave() {
    return this.hostBinding.isSlave();
  }

  @Override
  public void didBecomeMaster() {
    this.hostBinding.didBecomeMaster();
  }

  @Override
  public void didBecomeSlave() {
    this.hostBinding.didBecomeSlave();
  }

  @Override
  public HashTrieMap<Uri, NodeBinding> nodes() {
    return this.hostBinding.nodes();
  }

  @Override
  public NodeBinding getNode(Uri nodeUri) {
    return this.hostBinding.getNode(nodeUri);
  }

  @Override
  public NodeBinding openNode(Uri nodeUri) {
    return this.hostBinding.openNode(nodeUri);
  }

  @Override
  public NodeBinding openNode(Uri nodeUri, NodeBinding node) {
    return this.hostBinding.openNode(nodeUri, node);
  }

  @Override
  public NodeBinding createNode(Uri nodeUri) {
    return this.hostContext.createNode(nodeUri);
  }

  @Override
  public NodeBinding injectNode(Uri nodeUri, NodeBinding node) {
    return this.hostContext.injectNode(nodeUri, node);
  }

  @Override
  public LaneBinding createLane(Uri nodeUri, LaneDef laneDef) {
    return this.hostContext.createLane(nodeUri, laneDef);
  }

  @Override
  public LaneBinding createLane(Uri nodeUri, Uri laneUri) {
    return this.hostContext.createLane(nodeUri, laneUri);
  }

  @Override
  public LaneBinding injectLane(Uri nodeUri, Uri laneUri, LaneBinding lane) {
    return this.hostContext.injectLane(nodeUri, laneUri, lane);
  }

  @Override
  public void openLanes(Uri nodeUri, NodeBinding node) {
    this.hostContext.openLanes(nodeUri, node);
  }

  @Override
  public AgentFactory<?> createAgentFactory(Uri nodeUri, AgentDef agentDef) {
    return this.hostContext.createAgentFactory(nodeUri, agentDef);
  }

  @Override
  public <A extends Agent> AgentFactory<A> createAgentFactory(Uri nodeUri, Class<? extends A> agentClass) {
    return this.hostContext.createAgentFactory(nodeUri, agentClass);
  }

  @Override
  public void openAgents(Uri nodeUri, NodeBinding node) {
    this.hostContext.openAgents(nodeUri, node);
  }

  @Override
  public PolicyDirective<Identity> authenticate(Credentials credentials) {
    return this.hostContext.authenticate(credentials);
  }

  @Override
  public LinkBinding bindDownlink(Downlink downlink) {
    return this.hostContext.bindDownlink(downlink);
  }

  @Override
  public void openDownlink(LinkBinding link) {
    this.hostContext.openDownlink(link);
  }

  @Override
  public void closeDownlink(LinkBinding link) {
    this.hostContext.closeDownlink(link);
  }

  @Override
  public void pushDown(PushRequest pushRequest) {
    this.hostContext.pushDown(pushRequest);
  }

  @Override
  public void openUplink(LinkBinding link) {
    this.hostBinding.openUplink(link);
  }

  @Override
  public void pushUp(PushRequest pushRequest) {
    this.hostBinding.pushUp(pushRequest);
  }

  @Override
  public void trace(Object message) {
    this.hostContext.trace(message);
  }

  @Override
  public void debug(Object message) {
    this.hostContext.debug(message);
  }

  @Override
  public void info(Object message) {
    this.hostContext.info(message);
  }

  @Override
  public void warn(Object message) {
    this.hostContext.warn(message);
  }

  @Override
  public void error(Object message) {
    this.hostContext.error(message);
  }

  @Override
  public boolean isClosed() {
    return this.hostBinding.isClosed();
  }

  @Override
  public boolean isOpened() {
    return this.hostBinding.isOpened();
  }

  @Override
  public boolean isLoaded() {
    return this.hostBinding.isLoaded();
  }

  @Override
  public boolean isStarted() {
    return this.hostBinding.isStarted();
  }

  @Override
  public void open() {
    this.hostBinding.open();
  }

  @Override
  public void load() {
    this.hostBinding.load();
  }

  @Override
  public void start() {
    this.hostBinding.start();
  }

  @Override
  public void stop() {
    this.hostBinding.stop();
  }

  @Override
  public void unload() {
    this.hostBinding.unload();
  }

  @Override
  public void close() {
    this.hostBinding.close();
  }

  @Override
  public void willOpen() {
    this.hostContext.willOpen();
  }

  @Override
  public void didOpen() {
    this.hostContext.didOpen();
  }

  @Override
  public void willLoad() {
    this.hostContext.willLoad();
  }

  @Override
  public void didLoad() {
    this.hostContext.didLoad();
  }

  @Override
  public void willStart() {
    this.hostContext.willStart();
  }

  @Override
  public void didStart() {
    this.hostContext.didStart();
  }

  @Override
  public void didConnect() {
    this.hostContext.didConnect();
  }

  @Override
  public void didDisconnect() {
    this.hostContext.didDisconnect();
  }

  @Override
  public void willStop() {
    this.hostContext.willStop();
  }

  @Override
  public void didStop() {
    this.hostContext.didStop();
  }

  @Override
  public void willUnload() {
    this.hostContext.willUnload();
  }

  @Override
  public void didUnload() {
    this.hostContext.didUnload();
  }

  @Override
  public void willClose() {
    this.hostContext.willClose();
  }

  @Override
  public void didClose() {
    this.hostBinding.didClose();
  }

  @Override
  public void didFail(Throwable error) {
    this.hostBinding.didFail(error);
  }
}
