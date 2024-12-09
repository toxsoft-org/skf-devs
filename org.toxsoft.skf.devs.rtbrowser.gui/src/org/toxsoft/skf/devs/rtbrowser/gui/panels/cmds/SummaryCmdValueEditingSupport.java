package org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds;

import static org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.cmdserv.*;

/**
 * Редактор для генерации команд для колонки Summary
 *
 * @author dima
 */
public class SummaryCmdValueEditingSupport
    extends BaseCmdValueEditingSupport {

  private final ISkidList skidList;

  SummaryCmdValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext,
      ISkidList aSkidList ) {
    super( aViewer, aCoreApi, aTsContext );
    skidList = aSkidList;
  }

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    CellEditor retVal = null;
    CmdBrowserRow browserRow = (CmdBrowserRow)aElement;
    IAtomicValue dfltCellVal = IAtomicValue.NULL;
    IAtomicValue cellVal = browserRow.objCellVal( skidList.first() );
    // FIXME dima 12.07.23 здесь нужно переделать на вызов диалога в котором запрашивается полный набор значений
    // аргументов команды
    EAtomicType cellValueType = browserRow.cmdInfo().argDefs().isEmpty() ? EAtomicType.NONE
        : browserRow.cmdInfo().argDefs().first().atomicType();
    switch( cellValueType ) {
      case NONE:
      case BOOLEAN:
        currEditor = boolEditor;
        if( cellVal.atomicType() != EAtomicType.BOOLEAN ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        retVal = currEditor;
        break;
      case FLOATING:
        currEditor = floatEditor;
        if( cellVal.atomicType() != EAtomicType.FLOATING ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        retVal = currEditor;
        break;
      case INTEGER:
        currEditor = intEditor;
        if( cellVal.atomicType() != EAtomicType.INTEGER ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        retVal = currEditor;
        break;
      case STRING:
        currEditor = textEditor;
        if( cellVal.atomicType() != EAtomicType.STRING ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        break;
      case TIMESTAMP:
        break;
      case VALOBJ:
        break;
      default:
        break;

    }
    return retVal;
  }

  @Override
  protected Object getValue( Object aElement ) {
    return currEditor.getValue();
  }

  @Override
  protected void setValue( Object aElement, Object aValue ) {
    CmdBrowserRow browserRow = (CmdBrowserRow)aElement;
    IAtomicValue avValue = (IAtomicValue)aValue;
    OptionSet cmdArgs = new OptionSet();
    String cmdArgsStr = TsLibUtils.EMPTY_STRING;
    if( !browserRow.cmdInfo().argDefs().isEmpty() ) {
      // аргумент команды
      String argName = browserRow.cmdInfo().argDefs().first().id();
      cmdArgs.setValue( argName, avValue );
      cmdArgsStr = String.format( STR_FMT_CMD_ARG_VAL_FMT, argName, avValue );
    }
    String objSkids = getObjSkidsStr( skidList );
    String confirmFmtStr = STR_FMT_CMD_ID + cmdArgsStr + objSkids + STR_RUN;
    Shell shell = tsContext.get( Shell.class );
    if( TsDialogUtils.askYesNoCancel( shell, confirmFmtStr, browserRow.cmdInfo().id() ) == ETsDialogCode.YES ) {

      Skid authorSkid = coreApi.getCurrentUserInfo().userSkid();
      for( Skid cellSkid : skidList ) {
        Gwid cmdGwid = Gwid.createCmd( cellSkid.classId(), cellSkid.strid(), browserRow.cmdInfo().id() );
        ISkCommand cmd = coreApi.cmdService().sendCommand( cmdGwid, authorSkid, cmdArgs );
        browserRow.handleCommand( cmd );
      }
    }
  }

  @SuppressWarnings( "nls" )
  private static String getObjSkidsStr( ISkidList aSkidList ) {
    StringBuilder retVal = new StringBuilder( " \n" ); //$NON-NLS-1$
    for( Skid skid : aSkidList ) {
      retVal.append( skid ).append( "\n" ); //$NON-NLS-1$
    }
    return retVal.toString();
  }
}
