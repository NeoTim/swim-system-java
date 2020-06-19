// Copyright 2015-2020 Swim inc.
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

package swim.dynamic.structure;

import swim.dynamic.HostObjectType;
import swim.dynamic.JavaHostClassType;
import swim.structure.Record;

public final class HostRecord {

  public static final HostObjectType<Record> TYPE;

  static {
    final JavaHostClassType<Record> type = new JavaHostClassType<>(Record.class);
    TYPE = type;
    type.extendType(HostValue.TYPE);
  }

  private HostRecord() {
    // static
  }

}
