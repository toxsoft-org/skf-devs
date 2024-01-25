/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.rtdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Модель (в парадигме MVC) одного ряда данных компонента {@link RtDataBrowser}
 *
 * @author dima
 */
class RtDataBrowserRow
    extends BaseBrowserRow {

  /**
   * каналы текущих данных
   */
  private IMap<Gwid, ISkReadCurrDataChannel> rtDataChannelsMap;
  /**
   * описание Rt данного
   */
  private IDtoRtdataInfo                     rtDataInfo;

  /**
   * @param aCoreApi server's API
   * @param aSkidList список {@link Skid} данные которых отображаются
   * @param aRtdataInfo описание данного
   */
  public RtDataBrowserRow( ISkCoreApi aCoreApi, ISkidList aSkidList, IDtoRtdataInfo aRtdataInfo ) {
    super( aCoreApi, aSkidList, aRtdataInfo );
    rtDataInfo = aRtdataInfo;
  }

  @Override
  protected void doInit( ISkCoreApi aCoreApi, ISkidList aSkidList ) {
    // создаем список наших Gwid
    GwidList gwidList = new GwidList();
    for( Skid skid : aSkidList ) {
      gwidList.add( Gwid.createRtdata( skid.classId(), skid.strid(), сlassPropInfoBase.id() ) );
    }
    // создаем каналы текущих данных
    ISkRtdataService dataServ = aCoreApi.getService( ISkRtdataService.SERVICE_ID );
    rtDataChannelsMap = dataServ.createReadCurrDataChannels( gwidList );
  }

  @Override
  public IAtomicValue objCellVal( Skid aCellSkid ) {
    IAtomicValue retVal = AvUtils.avStr( IRtBrowserTable.NO_VALID_VALUE );
    Gwid cellGwid = Gwid.createRtdata( aCellSkid.classId(), aCellSkid.strid(), сlassPropInfoBase.id() );
    if( rtDataChannelsMap.findByKey( cellGwid ).isOk() ) {
      ISkReadCurrDataChannel channel = rtDataChannelsMap.getByKey( cellGwid );
      retVal =
          channel.getValue() == IAtomicValue.NULL ? AvUtils.avStr( IRtBrowserTable.NONE_VALUE ) : channel.getValue();
    }
    return retVal;
  }

  IDtoRtdataInfo dataInfo() {
    return rtDataInfo;
  }

  @Override
  public IAtomicValue summaryCellVal() {
    for( Gwid cellGwid : rtDataChannelsMap.keys() ) {
      if( !rtDataChannelsMap.findByKey( cellGwid ).isOk() ) {
        // если хоть один объект не на связи, то summary не имеет смысла
        return AvUtils.avStr( IRtBrowserTable.NO_VALID_VALUE );
      }
    }
    // инициализируем опорное значение
    IAtomicValue baseValue = rtDataChannelsMap.getByKey( rtDataChannelsMap.keys().first() ).getValue();
    for( Gwid cellGwid : rtDataChannelsMap.keys() ) {
      ISkReadCurrDataChannel channel = rtDataChannelsMap.getByKey( cellGwid );
      // сравниваем опорное значение с текущим, если различается то заканчиваем анализ
      if( !baseValue.equals( channel.getValue() ) ) {
        return AvUtils.avStr( IRtBrowserTable.DIFF_VALUE );
      }
    }
    return baseValue.equals( IAtomicValue.NULL ) ? AvUtils.avStr( IRtBrowserTable.NONE_VALUE ) : baseValue;
  }

  @Override
  public void dispose() {
    // TODO закрытие канала приводит к тому что далее эти данные не поступают
    // for( Gwid cellGwid : rtDataChannelsMap.keys() ) {
    // rtDataChannelsMap.findByKey( cellGwid ).close();
    // }

  }

}
