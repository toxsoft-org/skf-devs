package org.toxsoft.skf.devs.rtbrowser.gui.panels;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Путь к ИДу сущности описания системы, представляющий собой последовательность взаимосвязанных Gwid'ов.
 *
 * @author vs
 */
public interface IGwidPath {

  /**
   * Список последовательных Gwid'ов.
   *
   * @return IList&lt;Gwid> список последовательных Gwid'ов
   */
  IList<Gwid> gwids();

  /**
   * Возвращает предыдущий Gwid или <b>null</> если переданный Gwid является первым в пути.
   *
   * @param aGwid - ИД предыдущий к которому необходимо найти
   * @return Gwid - предыдущий ИД
   * @throws TsItemNotFoundRtException - если aGwid в пути отсутствует
   */
  Gwid prevGwid( Gwid aGwid );
}
