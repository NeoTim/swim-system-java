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

package swim.runtime.agent;

import swim.api.Link;
import swim.api.SwimContext;
import swim.api.agent.Agent;
import swim.api.agent.AgentContext;
import swim.api.agent.AgentFactory;
import swim.api.data.ListData;
import swim.api.data.MapData;
import swim.api.data.SpatialData;
import swim.api.data.ValueData;
import swim.api.downlink.Downlink;
import swim.api.lane.CommandLane;
import swim.api.lane.DemandLane;
import swim.api.lane.DemandMapLane;
import swim.api.lane.JoinMapLane;
import swim.api.lane.JoinValueLane;
import swim.api.lane.Lane;
import swim.api.lane.ListLane;
import swim.api.lane.MapLane;
import swim.api.lane.SpatialLane;
import swim.api.lane.SupplyLane;
import swim.api.lane.ValueLane;
import swim.api.policy.Policy;
import swim.api.ws.WsLane;
import swim.collections.FingerTrieSeq;
import swim.concurrent.Schedule;
import swim.concurrent.Stage;
import swim.math.R2Shape;
import swim.math.Z2Form;
import swim.runtime.AbstractTierBinding;
import swim.runtime.HttpBinding;
import swim.runtime.LinkBinding;
import swim.runtime.PushRequest;
import swim.runtime.TierContext;
import swim.runtime.lane.CommandLaneView;
import swim.runtime.lane.DemandLaneView;
import swim.runtime.lane.DemandMapLaneView;
import swim.runtime.lane.JoinMapLaneView;
import swim.runtime.lane.JoinValueLaneView;
import swim.runtime.lane.LaneView;
import swim.runtime.lane.ListLaneView;
import swim.runtime.lane.MapLaneView;
import swim.runtime.lane.SpatialLaneView;
import swim.runtime.lane.SupplyLaneView;
import swim.runtime.lane.ValueLaneView;
import swim.spatial.GeoProjection;
import swim.store.StoreBinding;
import swim.structure.Text;
import swim.structure.Value;
import swim.uri.Uri;
import swim.util.Builder;

public class AgentView extends AbstractTierBinding implements TierContext, AgentContext {
  protected final AgentModel node;

  protected final Value props;

  protected Agent agent;

  public AgentView(AgentModel node, Value props) {
    this.node = node;
    this.props = props.commit();
  }

  public final Agent agent() {
    return this.agent;
  }

  void setAgent(Agent agent) {
    this.agent = agent;
  }

  @Override
  public TierContext tierContext() {
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T> T unwrapNode(Class<T> nodeClass) {
    if (nodeClass.isAssignableFrom(getClass())) {
      return (T) this;
    } else if (nodeClass.isAssignableFrom(this.agent.getClass())) {
      return (T) this.agent;
    } else {
      return null;
    }
  }

  @Override
  public final Uri meshUri() {
    return this.node.meshUri();
  }

  @Override
  public final Uri hostUri() {
    return this.node.hostUri();
  }

  @Override
  public final Uri nodeUri() {
    return this.node.nodeUri();
  }

  @Override
  public final Value props() {
    return this.props;
  }

  @Override
  public Value getProp(Value key) {
    return this.props.get(key);
  }

  @Override
  public Value getProp(String name) {
    return this.props.get(name);
  }

  @Override
  public Policy policy() {
    return this.node.policy();
  }

  @Override
  public Schedule schedule() {
    return this.node.schedule();
  }

  @Override
  public Stage stage() {
    return this.node.stage();
  }

  @Override
  public StoreBinding store() {
    return this.node.store();
  }

  @Override
  public Lane lane() {
    return SwimContext.getLane();
  }

  @Override
  public Link link() {
    return SwimContext.getLink();
  }

  public <A extends Agent> AgentFactory<A> createAgentFactory(Class<? extends A> agentClass) {
    return this.node.createAgentFactory(agentClass);
  }

  @Override
  public FingerTrieSeq<Agent> traits() {
    final Object views = this.node.views;
    final Builder<Agent, FingerTrieSeq<Agent>> traits = FingerTrieSeq.builder();
    if (views instanceof AgentView) {
      traits.add(((AgentView) views).agent);
    } else if (views instanceof AgentView[]) {
      final AgentView[] viewArray = (AgentView[]) views;
      for (int i = 0, n = viewArray.length; i < n; i += 1) {
        traits.add(viewArray[i].agent);
      }
    }
    return traits.bind();
  }

  @Override
  public Agent getTrait(Value props) {
    final AgentView view = this.node.getAgentView(props);
    if (view != null) {
      return view.agent;
    } else {
      return null;
    }
  }

  @Override
  public Agent getTrait(String name) {
    return getTrait(Text.from(name));
  }

  @Override
  public <A extends Agent> A getTrait(Class<? extends A> agentClass) {
    return this.node.getTrait(agentClass);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <A extends Agent> A addTrait(Value props, AgentFactory<A> agentFactory) {
    return this.node.addTrait(props, agentFactory);
  }

  @Override
  public <A extends Agent> A addTrait(String name, AgentFactory<A> agentFactory) {
    return addTrait(Text.from(name), agentFactory);
  }

  @Override
  public <A extends Agent> A addTrait(Value props, Class<? extends A> agentClass) {
    return addTrait(props, createAgentFactory(agentClass));
  }

  @Override
  public <A extends Agent> A addTrait(String name, Class<? extends A> agentClass) {
    return addTrait(Text.from(name), agentClass);
  }

  @Override
  public void removeTrait(Value props) {
    final AgentView view = this.node.getAgentView(props);
    if (view != null) {
      this.node.removeAgentView(view);
    }
  }

  @Override
  public void removeTrait(String name) {
    removeTrait(Text.from(name));
  }

  @Override
  public Lane getLane(Uri laneUri) {
    return this.node.getLane(laneUri).getLaneView(this);
  }

  @Override
  public Lane openLane(Uri laneUri, Lane lane) {
    this.node.openLaneView(laneUri, (LaneView) lane);
    return lane;
  }

  @Override
  public <V> CommandLane<V> commandLane() {
    return new CommandLaneView<V>(this, null);
  }

  @Override
  public <V> DemandLane<V> demandLane() {
    return new DemandLaneView<V>(this, null);
  }

  @Override
  public <K, V> DemandMapLane<K, V> demandMapLane() {
    return new DemandMapLaneView<K, V>(this, null, null);
  }

  @Override
  public <L, K, V> JoinMapLane<L, K, V> joinMapLane() {
    return new JoinMapLaneView<L, K, V>(this, null, null, null);
  }

  @Override
  public <K, V> JoinValueLane<K, V> joinValueLane() {
    return new JoinValueLaneView<K, V>(this, null, null);
  }

  @Override
  public <V> ListLane<V> listLane() {
    return new ListLaneView<V>(this, null);
  }

  @Override
  public <K, V> MapLane<K, V> mapLane() {
    return new MapLaneView<K, V>(this, null, null);
  }

  @Override
  public <K, S, V> SpatialLane<K, S, V> spatialLane(Z2Form<S> shapeForm) {
    return new SpatialLaneView<K, S, V>(this, null, shapeForm, null);
  }

  @Override
  public <K, V> SpatialLane<K, R2Shape, V> geospatialLane() {
    return new SpatialLaneView<K, R2Shape, V>(this, null, GeoProjection.wgs84Form(), null);
  }

  @Override
  public <V> SupplyLane<V> supplyLane() {
    return new SupplyLaneView<V>(this, null);
  }

  @Override
  public <V> ValueLane<V> valueLane() {
    return new ValueLaneView<V>(this, null);
  }

  @Override
  public <I, O> WsLane<I, O> wsLane() {
    throw new UnsupportedOperationException(); // TODO
  }

  @Override
  public ListData<Value> listData(Value name) {
    return store().listData(name);
  }

  @Override
  public ListData<Value> listData(String name) {
    return store().listData(name);
  }

  @Override
  public MapData<Value, Value> mapData(Value name) {
    return store().mapData(name);
  }

  @Override
  public MapData<Value, Value> mapData(String name) {
    return store().mapData(name);
  }

  @Override
  public <S> SpatialData<Value, S, Value> spatialData(Value name, Z2Form<S> shapeForm) {
    return store().spatialData(name, shapeForm);
  }

  @Override
  public <S> SpatialData<Value, S, Value> spatialData(String name, Z2Form<S> shapeForm) {
    return store().spatialData(name, shapeForm);
  }

  @Override
  public SpatialData<Value, R2Shape, Value> geospatialData(Value name) {
    return store().geospatialData(name);
  }

  @Override
  public SpatialData<Value, R2Shape, Value> geospatialData(String name) {
    return store().geospatialData(name);
  }

  @Override
  public ValueData<Value> valueData(Value name) {
    return store().valueData(name);
  }

  @Override
  public ValueData<Value> valueData(String name) {
    return store().valueData(name);
  }

  @Override
  public LinkBinding bindDownlink(Downlink downlink) {
    return this.node.bindDownlink(downlink);
  }

  @Override
  public void openDownlink(LinkBinding link) {
    this.node.openDownlink(link);
  }

  @Override
  public void closeDownlink(LinkBinding link) {
    this.node.closeDownlink(link);
  }

  @Override
  public void httpDownlink(HttpBinding http) {
    this.node.httpDownlink(http);
  }

  @Override
  public void pushDown(PushRequest pushRequest) {
    this.node.pushDown(pushRequest);
  }

  @Override
  public void trace(Object message) {
    this.node.trace(message);
  }

  @Override
  public void debug(Object message) {
    this.node.debug(message);
  }

  @Override
  public void info(Object message) {
    this.node.info(message);
  }

  @Override
  public void warn(Object message) {
    this.node.warn(message);
  }

  @Override
  public void error(Object message) {
    this.node.error(message);
  }

  @Override
  public void willOpen() {
    this.agent.willOpen();
  }

  @Override
  public void didOpen() {
    this.agent.didOpen();
  }

  @Override
  public void willLoad() {
    this.agent.willLoad();
  }

  @Override
  public void didLoad() {
    this.agent.didLoad();
  }

  @Override
  public void willStart() {
    this.agent.willStart();
  }

  @Override
  public void didStart() {
    this.agent.didStart();
  }

  @Override
  public void willStop() {
    this.agent.willStop();
  }

  @Override
  public void didStop() {
    this.agent.didStop();
  }

  @Override
  public void willUnload() {
    this.agent.willUnload();
  }

  @Override
  public void didUnload() {
    this.agent.didUnload();
  }

  @Override
  public void willClose() {
    this.agent.willClose();
    this.node.close();
  }

  @Override
  public void didClose() {
    this.agent.didClose();
  }

  @Override
  public void didFail(Throwable error) {
    this.agent.didFail(error);
  }
}
