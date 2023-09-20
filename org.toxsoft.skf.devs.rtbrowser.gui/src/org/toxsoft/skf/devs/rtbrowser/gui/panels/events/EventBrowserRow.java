/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.events;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Модель (в парадигме MVC) одного ряда событий компонента {@link EventsBrowser}
 *
 * @author dima
 */
class EventBrowserRow
    extends BaseBrowserRow {

  /**
   * @param aCoreApi серверное API
   * @param aSkidList список {@link Skid} значения связей которых отображаются
   * @param aEventInfo описание события
   */
  public EventBrowserRow( ISkCoreApi aCoreApi, ISkidList aSkidList, IDtoEventInfo aEventInfo ) {
    super( aCoreApi, aSkidList, aEventInfo );
  }

  @Override
  protected IAtomicValue objCellVal( Skid aCellSkid ) {
    IAtomicValue retVal = AvUtils.avStr( TsLibUtils.EMPTY_STRING );
    Gwid eventGwid = Gwid.createEvent( aCellSkid.classId(), aCellSkid.strid(), propInfo().id() );
    retVal = AvUtils.avStr( eventGwid.asString() );
    return retVal;
  }

  SkEvent eventCellVal( Skid aCellSkid ) {
    OptionSet params = new OptionSet();
    Gwid eventGwid = Gwid.createEvent( aCellSkid.classId(), aCellSkid.strid(), propInfo().id() );
    SkEvent event = new SkEvent( System.currentTimeMillis(), eventGwid, params );
    return event;
  }

  @Override
  protected IAtomicValue summaryCellVal() {
    return AvUtils.avStr( TsLibUtils.EMPTY_STRING );
  }

}
