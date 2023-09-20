package org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.cmdserv.*;

/**
 * Редактор значения в ячейке таблицы просмотра команд со всплывающим диалогом для ввода аргументов
 *
 * @author dima
 */
public class ObjectCmdValueDlgEditingSupport
    extends BaseCmdValueDlgEditingSupport {

  private final Skid skid;

  ObjectCmdValueDlgEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext, Skid aSkid ) {
    super( aViewer, aCoreApi, aTsContext );
    skid = aSkid;
  }

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    currEditor = new CommandCellEditor( (Composite)getViewer().getControl(), tsContext );
    CmdBrowserRow browserRow = (CmdBrowserRow)aElement;
    currEditor.setValue( browserRow.cmdCellVal( skid ) );
    return currEditor;
  }

  @Override
  protected Object getValue( Object aElement ) {
    return currEditor.getValue();
  }

  @Override
  protected void setValue( Object aElement, Object aValue ) {
    // посылка команды
    if( aValue instanceof ISkCommand ) {
      CmdBrowserRow browserRow = (CmdBrowserRow)aElement;
      ISkCommand cmd = (ISkCommand)aValue;
      // сама посылка команды реализована в lm модели
      // Gwid cmdGwid = Gwid.createCmd( skid.classId(), skid.strid(), browserRow.cmdInfo().id() );
      // OptionSet cmdArgs = new OptionSet();
      // if( !browserRow.cmdInfo().argDefs().isEmpty() ) {
      // // аргумент команды
      // String argName = browserRow.cmdInfo().argDefs().first().id();
      // cmdArgs.setValue( argName, avValue );
      // }
      // ISkUser author = S5ConnectionUtils.getConnectedUser( coreApi );
      // ISkCommand cmd = coreApi.cmdService().sendCommand( cmdGwid, author.skid(), cmdArgs );
      browserRow.handleCommand( cmd );
    }
  }

}
