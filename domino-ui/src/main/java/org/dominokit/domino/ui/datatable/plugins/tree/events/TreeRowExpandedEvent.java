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
package org.dominokit.domino.ui.datatable.plugins.tree.events;

import org.dominokit.domino.ui.datatable.TableRow;
import org.dominokit.domino.ui.datatable.events.TableEvent;

/**
 * This event will be fired by the {@link
 * org.dominokit.domino.ui.datatable.plugins.RecordDetailsPlugin} when a record is expanded
 *
 * @param <T> the type of the record.
 */
public class TreeRowExpandedEvent<T> implements TableEvent {

  /** A constant string to define a unique type for this event */
  public static final String TREE_ROW_EXPANDED_EVENT = "tree-row-expanded-event";

  private final TableRow<T> tableRow;

  /** @param tableRow the {@link TableRow} being expanded */
  public TreeRowExpandedEvent(TableRow<T> tableRow) {
    this.tableRow = tableRow;
  }

  /** {@inheritDoc} */
  @Override
  public String getType() {
    return TREE_ROW_EXPANDED_EVENT;
  }

  /** @return the {@link TableRow} being expanded */
  public TableRow<T> getTableRow() {
    return tableRow;
  }
}