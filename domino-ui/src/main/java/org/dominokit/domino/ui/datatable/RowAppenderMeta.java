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
package org.dominokit.domino.ui.datatable;

import java.util.Objects;
import java.util.Optional;
import org.dominokit.domino.ui.utils.ComponentMeta;

/** RowAppenderMeta class. */
public class RowAppenderMeta<T> implements ComponentMeta {

  /** Constant <code>TABLE_ROW_APPENDER_META="table-row-appender-meta"</code> */
  public static final String TABLE_ROW_APPENDER_META = "table-row-appender-meta";

  private final TableConfig.RowAppender<T> rowAppender;

  /**
   * of.
   *
   * @param rowAppender a {@link org.dominokit.domino.ui.datatable.TableConfig.RowAppender} object
   * @param <T> a T class
   * @return a {@link org.dominokit.domino.ui.datatable.RowAppenderMeta} object
   */
  public static <T> RowAppenderMeta<T> of(TableConfig.RowAppender<T> rowAppender) {
    return new RowAppenderMeta<>(rowAppender);
  }

  /**
   * Constructor for RowAppenderMeta.
   *
   * @param rowAppender a {@link org.dominokit.domino.ui.datatable.TableConfig.RowAppender} object
   */
  public RowAppenderMeta(TableConfig.RowAppender<T> rowAppender) {
    Objects.requireNonNull(rowAppender, "RowAppender cant be null.");
    this.rowAppender = rowAppender;
  }

  /**
   * get.
   *
   * @param row a {@link org.dominokit.domino.ui.datatable.TableRow} object
   * @param <T> a T class
   * @return a {@link java.util.Optional} object
   */
  public static <T> Optional<RowAppenderMeta<T>> get(TableRow<T> row) {
    return row.getMeta(TABLE_ROW_APPENDER_META);
  }

  /**
   * Getter for the field <code>rowAppender</code>.
   *
   * @return a {@link org.dominokit.domino.ui.datatable.TableConfig.RowAppender} object
   */
  public TableConfig.RowAppender<T> getRowAppender() {
    return rowAppender;
  }

  /** {@inheritDoc} */
  @Override
  public String getKey() {
    return TABLE_ROW_APPENDER_META;
  }
}
