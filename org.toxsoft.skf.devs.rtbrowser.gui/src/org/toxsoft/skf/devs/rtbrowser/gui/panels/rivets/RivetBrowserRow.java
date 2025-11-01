/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.rivets;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.links.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Модель (в парадигме MVC) одного ряда заклепок компонента {@link RivetsBrowser}
 *
 * @author dima
 */
class RivetBrowserRow
    extends BaseBrowserRow {

  private final IDtoRivetInfo rivetInfo;

  /**
   * @param aCoreApi серверное API
   * @param aSkidList список {@link Skid} значения связей которых отображаются
   * @param aRivetInfo описание заклепки
   */
  public RivetBrowserRow( ISkCoreApi aCoreApi, ISkidList aSkidList, IDtoRivetInfo aRivetInfo ) {
    super( aCoreApi, aSkidList, aRivetInfo );
    rivetInfo = aRivetInfo;
  }

  @Override
  protected IAtomicValue objCellVal( Skid aCellSkid ) {
    IAtomicValue retVal = AvUtils.avStr( TsLibUtils.EMPTY_STRING );
    // get rivet value
    ISkObject cellObj = coreApi.objService().get( aCellSkid );
    ISkidList rvtSkidList = cellObj.rivets().map().getByKey( rivetInfo.id() );
    if( !rvtSkidList.isEmpty() ) {
      retVal = AvUtils.avStr( LinkBrowserRow.skidList2String( rvtSkidList ) );
    }
    return retVal;
  }

  IDtoRivetInfo rvtCellVal( Skid aCellSkid ) {
    return coreApi.sysdescr().getClassInfo( aCellSkid.classId() ).rivets().list().findByKey( rivetInfo.id() );
  }

  @Override
  protected IAtomicValue summaryCellVal() {
    // инициализируем опорное значение
    Skid baseSkid = new Skid( skidList.first().classId(), skidList.first().strid() );
    // get rivet value
    ISkObject baseObj = coreApi.objService().get( baseSkid );
    ISkidList baseRvtSkidList = baseObj.rivets().map().getByKey( rivetInfo.id() );

    for( Skid cellSkid : skidList ) {
      ISkObject cellObj = coreApi.objService().get( cellSkid );
      ISkidList cellRvtSkidList = cellObj.rivets().map().getByKey( rivetInfo.id() );
      // сравниваем опорное значение с текущим, если различается, то заканчиваем анализ
      if( !baseRvtSkidList.equals( cellRvtSkidList ) ) {
        return AvUtils.avStr( IRtBrowserTable.DIFF_VALUE );
      }
    }
    return AvUtils.avStr( LinkBrowserRow.skidList2String( baseRvtSkidList ) );
  }

  public ISkidList skidList() {
    return skidList;
  }
}
