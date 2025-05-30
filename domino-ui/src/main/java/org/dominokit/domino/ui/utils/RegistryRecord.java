/*
 * Copyright © 2019 Dominokit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dominokit.domino.ui.utils;

public class RegistryRecord<T> {

  private final T record;
  private Registry<T> registry;

  public static <T> RegistryRecord<T> of(T record) {
    return new RegistryRecord<>(record);
  }

  public RegistryRecord(T record) {
    this.record = record;
  }

  void bind(Registry<T> registry) {
    this.registry = registry;
  }

  T getRecord() {
    return record;
  }

  public final void remove() {
    this.registry.remove(record);
  }
}
