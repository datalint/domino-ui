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
package org.dominokit.domino.ui.datatable.plugins;

import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.events.TablePageChangeEvent;
import org.dominokit.domino.ui.pagination.ScrollingPagination;

/**
 * This plugin attach an {@link ScrollingPagination} component to the data table and fires {@link
 * TablePageChangeEvent} when ever the page is changed
 *
 * @param <T> the type of the data table records
 */
public class ScrollingPaginationPlugin<T> implements DataTablePlugin<T> {

  private ScrollingPagination pagination;

  /** Creates and instance with default page size of 10 */
  public ScrollingPaginationPlugin() {
    this(10);
  }

  public ScrollingPaginationPlugin(int pageSize) {
    this(pageSize, 10);
  }

  /**
   * Creates and instance with a custom page size
   *
   * @param pageSize int, Page size
   */
  public ScrollingPaginationPlugin(int pageSize, int windowSize) {
    this.pagination = ScrollingPagination.create(0, pageSize, windowSize);
  }

  /** {@inheritDoc} */
  @Override
  public void onAfterAddTable(DataTable<T> dataTable) {
    dataTable.element().appendChild(pagination.element());
    pagination.addChangeListener((oldValue, newValue) -> dataTable.fireTableEvent(new TablePageChangeEvent(newValue, pagination)));
  }

  /** @return The {@link ScrollingPagination} component wrapped in this plugin */
  public ScrollingPagination getPagination() {
    return pagination;
  }
}
