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

/**
 * Lock-free document store—optimized for high rate atomic state changes—that
 * concurrently commits and compacts on-disk log-structured storage files
 * without blocking parallel in-memory updates to associative B-tree maps,
 * spatial Q-tree maps, sequential S-tree lists, and singleton U-tree values.
 */
module swim.db {
  requires transitive swim.util;
  requires transitive swim.codec;
  requires transitive swim.structure;
  requires transitive swim.recon;
  requires transitive swim.math;
  requires transitive swim.spatial;
  requires transitive swim.collections;
  requires transitive swim.concurrent;
  requires swim.uri;

  exports swim.db;
}
