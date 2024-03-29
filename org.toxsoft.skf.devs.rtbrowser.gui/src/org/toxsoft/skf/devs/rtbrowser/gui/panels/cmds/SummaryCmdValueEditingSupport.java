package org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds;

import static org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources.*;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.dialogs.ETsDialogCode;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.skid.ISkidList;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.cmdserv.ISkCommand;

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
    StringBuilder retVal = new StringBuilder( " \n" );
    for( Skid skid : aSkidList ) {
      retVal.append( skid ).append( "\n" );
    }
    return retVal.toString();
  }
}
