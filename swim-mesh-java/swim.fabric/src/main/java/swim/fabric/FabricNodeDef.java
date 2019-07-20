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

package swim.fabric;

import java.util.Collection;
import swim.api.agent.AgentDef;
import swim.codec.Debug;
import swim.codec.Format;
import swim.codec.Output;
import swim.collections.FingerTrieSeq;
import swim.concurrent.StageDef;
import swim.runtime.LaneDef;
import swim.runtime.LogDef;
import swim.runtime.NodeDef;
import swim.runtime.PolicyDef;
import swim.store.StoreDef;
import swim.uri.Uri;
import swim.uri.UriMapper;
import swim.uri.UriPattern;
import swim.util.Murmur3;

public class FabricNodeDef implements NodeDef, Debug {
  final UriPattern nodePattern;
  final FingerTrieSeq<AgentDef> agentDefs;
  final UriMapper<LaneDef> laneDefs;
  final LogDef logDef;
  final PolicyDef policyDef;
  final StageDef stageDef;
  final StoreDef storeDef;

  public FabricNodeDef(UriPattern nodePattern, FingerTrieSeq<AgentDef> agentDefs,
                       UriMapper<LaneDef> laneDefs, LogDef logDef, PolicyDef policyDef,
                       StageDef stageDef, StoreDef storeDef) {
    this.nodePattern = nodePattern;
    this.agentDefs = agentDefs;
    this.laneDefs = laneDefs;
    this.logDef = logDef;
    this.policyDef = policyDef;
    this.stageDef = stageDef;
    this.storeDef = storeDef;
  }

  @Override
  public final Uri nodeUri() {
    return this.nodePattern.isUri() ? this.nodePattern.toUri() : null;
  }

  @Override
  public final UriPattern nodePattern() {
    return this.nodePattern;
  }

  public FabricNodeDef nodePattern(UriPattern nodePattern) {
    return copy(nodePattern, this.agentDefs, this.laneDefs,
                this.logDef, this.policyDef, this.stageDef, this.storeDef);
  }

  @Override
  public final Collection<? extends AgentDef> agentDefs() {
    return this.agentDefs;
  }

  @Override
  public final AgentDef getAgentDef(String agentName) {
    for (AgentDef agentDef : this.agentDefs) {
      if (agentName.equals(agentDef.agentName())) {
        return agentDef;
      }
    }
    return null;
  }

  public FabricNodeDef agentDef(AgentDef agentDef) {
    return copy(this.nodePattern, this.agentDefs.appended(agentDef), this.laneDefs,
                this.logDef, this.policyDef, this.stageDef, this.storeDef);
  }

  @Override
  public final Collection<? extends LaneDef> laneDefs() {
    return this.laneDefs.values();
  }

  @Override
  public final LaneDef getLaneDef(Uri laneUri) {
    return this.laneDefs.get(laneUri);
  }

  public FabricNodeDef laneDef(LaneDef laneDef) {
    return copy(this.nodePattern, this.agentDefs, this.laneDefs.updated(laneDef.lanePattern(), laneDef),
                this.logDef, this.policyDef, this.stageDef, this.storeDef);
  }

  @Override
  public final LogDef logDef() {
    return this.logDef;
  }

  public FabricNodeDef logDef(LogDef logDef) {
    return copy(this.nodePattern, this.agentDefs, this.laneDefs,
                logDef, this.policyDef, this.stageDef, this.storeDef);
  }

  @Override
  public final PolicyDef policyDef() {
    return this.policyDef;
  }

  public FabricNodeDef policyDef(PolicyDef policyDef) {
    return copy(this.nodePattern, this.agentDefs, this.laneDefs,
                this.logDef, policyDef, this.stageDef, this.storeDef);
  }

  @Override
  public final StageDef stageDef() {
    return this.stageDef;
  }

  public FabricNodeDef stageDef(StageDef stageDef) {
    return copy(this.nodePattern, this.agentDefs, this.laneDefs,
                this.logDef, this.policyDef, stageDef, this.storeDef);
  }

  @Override
  public final StoreDef storeDef() {
    return this.storeDef;
  }

  public FabricNodeDef storeDef(StoreDef storeDef) {
    return copy(this.nodePattern, this.agentDefs, this.laneDefs,
                this.logDef, this.policyDef, this.stageDef, storeDef);
  }

  protected FabricNodeDef copy(UriPattern nodePattern, FingerTrieSeq<AgentDef> agentDefs,
                               UriMapper<LaneDef> laneDefs, LogDef logDef, PolicyDef policyDef,
                               StageDef stageDef, StoreDef storeDef) {
    return new FabricNodeDef(nodePattern, agentDefs, laneDefs,
                             logDef, policyDef, stageDef, storeDef);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other instanceof FabricNodeDef) {
      final FabricNodeDef that = (FabricNodeDef) other;
      return this.nodePattern.equals(that.nodePattern)
          && this.agentDefs.equals(that.agentDefs)
          && this.laneDefs.equals(that.laneDefs)
          && (this.logDef == null ? that.logDef == null : this.logDef.equals(that.logDef))
          && (this.policyDef == null ? that.policyDef == null : this.policyDef.equals(that.policyDef))
          && (this.stageDef == null ? that.stageDef == null : this.stageDef.equals(that.stageDef))
          && (this.storeDef == null ? that.storeDef == null : this.storeDef.equals(that.storeDef));
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (hashSeed == 0) {
      hashSeed = Murmur3.seed(FabricNodeDef.class);
    }
    return Murmur3.mash(Murmur3.mix(Murmur3.mix(Murmur3.mix(Murmur3.mix(Murmur3.mix(Murmur3.mix(
        Murmur3.mix(hashSeed, this.nodePattern.hashCode()), this.agentDefs.hashCode()),
        this.laneDefs.hashCode()), Murmur3.hash(this.logDef)), Murmur3.hash(this.policyDef)),
        Murmur3.hash(this.stageDef)), Murmur3.hash(this.storeDef)));
  }

  @Override
  public void debug(Output<?> output) {
    output = output.write("FabricNodeDef").write('.');
    if (this.nodePattern.isUri()) {
      output = output.write("fromNodeUri").write('(').debug(this.nodePattern.toUri()).write(')');
    } else {
      output = output.write("fromNodePattern").write('(').debug(this.nodePattern).write(')');
    }
    for (AgentDef agentDef : this.agentDefs()) {
      output = output.write('.').write("agentDef").write('(').debug(agentDef).write(')');
    }
    for (LaneDef laneDef : this.laneDefs.values()) {
      output = output.write('.').write("laneDef").write('(').debug(laneDef).write(')');
    }
    if (this.logDef != null) {
      output = output.write('.').write("logDef").write('(').debug(this.logDef).write(')');
    }
    if (this.policyDef != null) {
      output = output.write('.').write("policyDef").write('(').debug(this.policyDef).write(')');
    }
    if (this.stageDef != null) {
      output = output.write('.').write("stageDef").write('(').debug(this.stageDef).write(')');
    }
    if (this.storeDef != null) {
      output = output.write('.').write("storeDef").write('(').debug(this.storeDef).write(')');
    }
  }

  @Override
  public String toString() {
    return Format.debug(this);
  }

  private static int hashSeed;

  public static FabricNodeDef fromNodeUri(Uri nodeUri) {
    return new FabricNodeDef(UriPattern.from(nodeUri), FingerTrieSeq.empty(),
                             UriMapper.empty(), null, null, null, null);
  }

  public static FabricNodeDef fromNodeUri(String nodeUri) {
    return fromNodeUri(Uri.parse(nodeUri));
  }

  public static FabricNodeDef fromNodePattern(UriPattern nodePattern) {
    return new FabricNodeDef(nodePattern, FingerTrieSeq.empty(),
                             UriMapper.empty(), null, null, null, null);
  }

  public static FabricNodeDef fromNodePattern(String nodePattern) {
    return fromNodePattern(UriPattern.parse(nodePattern));
  }
}
