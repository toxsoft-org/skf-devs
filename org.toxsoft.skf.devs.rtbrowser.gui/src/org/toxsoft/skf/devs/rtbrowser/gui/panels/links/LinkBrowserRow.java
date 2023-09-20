/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.links;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Модель (в парадигме MVC) одного ряда связей компонента {@link LinksBrowser}
 *
 * @author dima
 */
class LinkBrowserRow
    extends BaseBrowserRow {

  private final IDtoLinkInfo linkInfo;

  /**
   * @param aCoreApi серверное API
   * @param aSkidList список {@link Skid} значения связей которых отображаются
   * @param aLinkInfo описание команды
   */
  public LinkBrowserRow( ISkCoreApi aCoreApi, ISkidList aSkidList, IDtoLinkInfo aLinkInfo ) {
    super( aCoreApi, aSkidList, aLinkInfo );
    linkInfo = aLinkInfo;
  }

  @Override
  protected IAtomicValue objCellVal( Skid aCellSkid ) {
    IAtomicValue retVal = AvUtils.avStr( TsLibUtils.EMPTY_STRING );
    Gwid linkGwid = Gwid.createLink( aCellSkid.classId(), aCellSkid.strid(), linkInfo.id() );
    IDtoLinkFwd cellLinkInfo = coreApi.linkService().getLinkFwd( linkGwid );
    if( !cellLinkInfo.rightSkids().isEmpty() ) {
      retVal = AvUtils.avStr( skidList2String( cellLinkInfo.rightSkids() ) );
    }
    return retVal;
  }

  IDtoLinkFwd linkCellVal( Skid aCellSkid ) {
    Gwid linkGwid = Gwid.createLink( aCellSkid.classId(), aCellSkid.strid(), linkInfo.id() );
    return coreApi.linkService().getLinkFwd( linkGwid );
  }

  public static String skidList2String( ITsCollection<?> aColl ) {
    StringBuilder sb = new StringBuilder();
    int i = 0;
    for( Object o : aColl ) {
      sb.append( o.toString() );
      if( i < aColl.size() - 1 ) {
        sb.append( ", " ); //$NON-NLS-1$
      }
      ++i;
      if( i > 5 ) {
        break;
      }
    }
    if( aColl.size() > 5 ) {
      sb.append( "..." ); //$NON-NLS-1$
    }
    return String.format( "{ %s }", sb.toString() ); //$NON-NLS-1$
  }

  IDtoLinkInfo linkInfo() {
    return linkInfo;
  }

  @Override
  protected IAtomicValue summaryCellVal() {
    // инициализируем опорное значение
    Gwid linkGwid = Gwid.createLink( skidList.first().classId(), skidList.first().strid(), linkInfo.id() );
    IDtoLinkFwd baseLinkInfo = coreApi.linkService().getLinkFwd( linkGwid );
    ISkidList baseRightSkids = baseLinkInfo.rightSkids();
    for( Skid cellSkid : skidList ) {
      Gwid cellGwid = Gwid.createLink( cellSkid.classId(), cellSkid.strid(), linkInfo.id() );
      IDtoLinkFwd cellLinkInfo = coreApi.linkService().getLinkFwd( cellGwid );
      // сравниваем опорное значение с текущим, если различается, то заканчиваем анализ
      if( !baseRightSkids.equals( cellLinkInfo.rightSkids() ) ) {
        return AvUtils.avStr( IRtBrowserTable.DIFF_VALUE );
      }
    }
    return AvUtils.avStr( skidList2String( baseRightSkids ) );
  }

  public ISkidList skidList() {
    return skidList;
  }
}
