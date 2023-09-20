/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds;

import java.util.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Модель (в парадигме MVC) одного ряда команд компонента {@link CommandsBrowser}
 *
 * @author dima
 */
class CmdBrowserRow
    extends BaseBrowserRow
    implements IGenericChangeListener {

  private final IDtoCmdInfo          cmdInfo;
  private Map<Gwid, ESkCommandState> cmdStatusMap;

  /**
   * @param aCoreApi API сервера
   * @param aSkidList список {@link Skid} значения атрибутов которых отображаются
   * @param aCmdInfo описание команды
   */
  public CmdBrowserRow( ISkCoreApi aCoreApi, ISkidList aSkidList, IDtoCmdInfo aCmdInfo ) {
    super( aCoreApi, aSkidList, aCmdInfo );
    cmdInfo = aCmdInfo;
  }

  @Override
  protected void doInit( ISkCoreApi aCoreApi, ISkidList aSkidList ) {
    cmdStatusMap = new HashMap<>();
    for( Skid cellSkid : skidList ) {
      Gwid cmdGwid = Gwid.createCmd( cellSkid.classId(), cellSkid.strid(), propInfo().id() );
      cmdStatusMap.put( cmdGwid, null );
    }
  }

  @Override
  protected IAtomicValue objCellVal( Skid aCellSkid ) {
    Gwid cmdGwid = Gwid.createCmd( aCellSkid.classId(), aCellSkid.strid(), cmdInfo.id() );
    ESkCommandState cmdState = cmdStatusMap.get( cmdGwid );
    IAtomicValue retVal = AvUtils.avStr( TsLibUtils.EMPTY_STRING );
    if( cmdState != null ) {
      retVal = AvUtils.avStr( cmdState.name() );
    }
    return retVal;
  }

  IDtoCmdInfo cmdInfo() {
    return cmdInfo;
  }

  Gwid cmdCellVal( Skid aCellSkid ) {
    return Gwid.createCmd( aCellSkid.classId(), aCellSkid.strid(), cmdInfo.id() );
  }

  @Override
  protected IAtomicValue summaryCellVal() {
    // инициализируем опорное значение
    ESkCommandState baseCmdState = null;
    for( Skid cellSkid : skidList ) {
      // ищем первое не пустое значение в ячейках ряда
      Gwid cellGwid = Gwid.createCmd( cellSkid.classId(), cellSkid.strid(), cmdInfo.id() );
      baseCmdState = cmdStatusMap.get( cellGwid );
      if( baseCmdState != null ) {
        break;
      }
    }
    if( baseCmdState != null ) {
      for( Skid cellSkid : skidList ) {
        Gwid cellGwid = Gwid.createCmd( cellSkid.classId(), cellSkid.strid(), cmdInfo.id() );
        ESkCommandState cellValue = cmdStatusMap.get( cellGwid );
        // сравниваем опорное значение с текущим, если различается то заканчиваем анализ
        if( cellValue != null ) {
          if( !baseCmdState.equals( cellValue ) ) {
            return AvUtils.avStr( IRtBrowserTable.DIFF_VALUE );
          }
        }
      }
    }
    return baseCmdState == null ? AvUtils.avStr( IRtBrowserTable.NONE_VALUE )
        : AvUtils.avStr( baseCmdState.toString() );
  }

  @Override
  public void onGenericChangeEvent( Object aSource ) {
    ISkCommand cmd = (ISkCommand)aSource;
    SkCommandState cmdState = cmd.state();
    cmdStatusMap.put( cmd.cmdGwid(), cmd.state().state() );
    switch( cmdState.state() ) {
      case EXECUTING:
        break;
      case SENDING:
        break;
      case FAILED:
        cmd.stateEventer().removeListener( this );
        break;
      case SUCCESS:
        cmd.stateEventer().removeListener( this );
        break;
      case TIMEOUTED:
        cmd.stateEventer().removeListener( this );
        break;
      case UNHANDLED:
        cmd.stateEventer().removeListener( this );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    if( cmd.isComplete() ) {
      cmd.stateEventer().removeListener( this );
      if( cmd.isComplete() && cmd.state().state() != ESkCommandState.SUCCESS ) {
        // TODO вывести в tooltip ячейки
        // errStr = cmd.state().state().description();
      }
    }
  }

  void handleCommand( ISkCommand aCommand ) {
    if( CmdUtils.isFailed( aCommand.state().state() ) ) {
      onGenericChangeEvent( aCommand );
    }
    else {
      aCommand.stateEventer().addListener( this );
    }
  }

}
