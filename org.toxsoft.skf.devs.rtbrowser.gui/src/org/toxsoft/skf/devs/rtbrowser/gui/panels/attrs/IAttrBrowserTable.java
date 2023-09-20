package org.toxsoft.skf.devs.rtbrowser.gui.panels.attrs;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;

/**
 * ISkObject attributes browser/editor table widget.
 * <p>
 * Это компонента, аналогом которой может служить SWT {@link TableViewer}, где по строкам идут атрибуты, а по столбцам
 * объекты одного класса. Основная функциональность компоненты включает:
 * <ul>
 * <li>Отображение значений атрибутов в реальном времени</li>
 * <li>Редактирование одного атрибута для одного объекта непосредственно в ячейке таблицы</li>
 * <li>Редактирование одного атрибута для всех объектов непосредственно в ячейке таблицы</li>
 * <li>Поддержка понятий выделенного столбца или строки или ячейки</li>
 * </ul>
 * Для редактирования атрибута для всех объектов предусмотрена особая колонка "commonColumn", которая по необходимости
 * может показываться или скрываться см. методы: {@link #isCommonColumnVisible()} и
 * {@link #setCommonColumnVisible(boolean)}.<br>
 * Для поддержки понятий выделенного строки, столбца или ячейки предусмотрены интерфейсы типа
 * {@link ITsSelectionProvider}. В каждый момент времени может быть выделено не более одного типа сущности:
 * <ul>
 * <li>Колонка</li>
 * <li>Строка</li>
 * <li>ячейка</li> При нажатии на заголовок выделяется соотвествующий столбец (кроме первого, где находятся названия
 * данных), при нажатии на ячейку первого столбца выделяется соответствующая строка, при нажатии на ячейку -
 * соответствующая ячейка.
 * </ul>
 * Для редактирования значения в ячейке следует использовать ValedCellEditor.
 *
 * @author dima
 */
public interface IAttrBrowserTable
    extends ILazyControl<Control>, IPausableAnimation {

  /**
   * Возвращает ИДы (Skid) всех объектов, данные которых представлены в таблице.
   *
   * @return {@link ISkidList} - ИДы всех объектов, данные которых представлены в таблице
   */
  ISkidList columnObjctsSkids();

  /**
   * Задает ИДы (Skid) всех объектов, данные которых представлены в таблице.
   *
   * @param aObjIds {@link ISkidList} - ИДы всех объектов, данные которых представлены в таблице
   */
  void setColumnObjectSkids( ISkidList aObjIds );

  /**
   * Возвращает ИДы всех атрибутов представленых в таблице.
   *
   * @return {@link IStringList} - ИДы всех данных представленых в таблице
   */
  IStringList rowAttrIds();

  /**
   * Задает ИДы (Skid) всех атрибутов представленных в таблице.
   *
   * @param aRowAttrIds {@link IStringList} - ИДы всех атрибутов представленных в таблице
   */
  void setRowAttrIds( IStringList aRowAttrIds );

  /**
   * Возвращает признак видимости колонки общих данных.
   *
   * @return <b>true</b> - общая колонка видна<br>
   *         <b>false</b> - общая колонка скрыта
   */
  boolean isCommonColumnVisible();

  /**
   * Задает признак видимости колонки общих данных.
   *
   * @param aVisible <b>true</b> - общая колонка видна<br>
   *          <b>false</b> - общая колонка скрыта
   */
  void setCommonColumnVisible( boolean aVisible );

  /**
   * Возвращает "поставщик" выделенных столбцов.<br>
   * для CommonColumn - возвращает мульти-GWID
   *
   * @return {@link ITsSelectionProvider} - "поставщик" выделенных столбцов
   */
  ITsSelectionProvider<Gwid> columnSelectionProvider();

  /**
   * Возвращает "поставщик" выделенных строк.
   *
   * @return {@link ITsSelectionProvider} - "поставщик" выделенных строк
   */
  ITsSelectionProvider<Gwid> rowSelectionProvider();

  /**
   * Возвращает "поставщик" выделенных ячеек.
   *
   * @return {@link ITsSelectionProvider} - "поставщик" выделенных ячеек
   */
  ITsSelectionProvider<Gwid> cellSelectionProvider();

}
