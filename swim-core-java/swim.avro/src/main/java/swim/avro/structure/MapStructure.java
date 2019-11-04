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

package swim.avro.structure;

import swim.avro.schema.AvroMapType;
import swim.avro.schema.AvroType;
import swim.codec.Input;
import swim.codec.Output;
import swim.codec.Parser;
import swim.codec.Unicode;
import swim.structure.Record;
import swim.structure.Slot;
import swim.structure.Text;
import swim.structure.Value;
import swim.util.PairBuilder;

final class MapStructure extends AvroMapType<Value, Value, Value> {
  final AvroType<Value> valueType;

  MapStructure(AvroType<Value> valueType) {
    this.valueType = valueType;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Parser<Value> parseKey(Input input) {
    return Unicode.parseOutput((Output<Value>) (Output<?>) Text.output(), input);
  }

  @Override
  public AvroType<Value> valueType() {
    return this.valueType;
  }

  @Override
  public PairBuilder<Value, Value, Value> mapBuilder() {
    return new MapStructureBuilder();
  }
}

final class MapStructureBuilder implements PairBuilder<Value, Value, Value> {
  final Record record;

  MapStructureBuilder() {
    this.record = Record.create();
  }

  @Override
  public boolean add(Value key, Value value) {
    return this.record.add(Slot.of(key, value));
  }

  @Override
  public Value bind() {
    return this.record.branch();
  }
}
