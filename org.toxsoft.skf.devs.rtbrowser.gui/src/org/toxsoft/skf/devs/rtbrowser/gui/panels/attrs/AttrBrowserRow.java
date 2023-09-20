/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.attrs;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Модель (в парадигме MVC) одного ряда значений атрибутов компонента {@link AttrsBrowser}
 *
 * @author dima
 */
class AttrBrowserRow
    extends BaseBrowserRow {

  /**
   * @param aCoreApi server's API
   * @param aSkidList список {@link Skid} значения атрибутов которых отображаются
   * @param aAttrInfo описание атрибута
   */
  public AttrBrowserRow( ISkCoreApi aCoreApi, ISkidList aSkidList, IDtoAttrInfo aAttrInfo ) {
    super( aCoreApi, aSkidList, aAttrInfo );
    // attrInfo = aAttrInfo;
  }

  @Override
  public IAtomicValue objCellVal( Skid aCellSkid ) {
    IAtomicValue retVal = AvUtils.avStr( IRtBrowserTable.NO_VALID_VALUE );
    ISkObject cellObj = coreApi.objService().find( aCellSkid );
    if( cellObj != null ) {
      IAtomicValue attrVal = cellObj.attrs().getByKey( сlassPropInfoBase.id() );
      retVal = attrVal == IAtomicValue.NULL ? AvUtils.avStr( IRtBrowserTable.NONE_VALUE ) : attrVal;
    }
    return retVal;
  }

  @Override
  public IAtomicValue summaryCellVal() {
    // инициализируем опорное значение
    IAtomicValue baseValue = coreApi.objService().find( skidList.first() ).attrs().getByKey( propInfo().id() );
    for( Skid cellSkid : skidList ) {
      ISkObject cellObj = coreApi.objService().find( cellSkid );
      IAtomicValue cellValue = cellObj.attrs().getByKey( propInfo().id() );
      // сравниваем опорное значение с текущим, если различается то заканчиваем анализ
      if( !baseValue.equals( cellValue ) ) {
        return AvUtils.avStr( IRtBrowserTable.DIFF_VALUE );
      }
    }
    return baseValue.equals( IAtomicValue.NULL ) ? AvUtils.avStr( IRtBrowserTable.NONE_VALUE ) : baseValue;
  }

}
