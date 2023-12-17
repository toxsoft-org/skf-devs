package org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogInfo;
import org.toxsoft.core.tsgui.dialogs.datarec.TsDialogInfo;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tsgui.m5.gui.M5GuiUtils;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.skf.devs.rtbrowser.gui.km5.CommandM5Model;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.impl.SkCommand;
import org.toxsoft.uskat.core.impl.dto.DtoCommand;
import org.toxsoft.uskat.s5.server.backend.supports.commands.impl.S5CommandIdGenerator;

/**
 * Редактор значения в ячейке таблицы просмотра команд
 *
 * @author dima
 */
public class ObjectCmdValueEditingSupport
    extends BaseCmdValueEditingSupport {

  private final Skid skid;

  ObjectCmdValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext, Skid aSkid ) {
    super( aViewer, aCoreApi, aTsContext );
    skid = aSkid;
  }

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    CellEditor retVal = null;
    CmdBrowserRow browserRow = (CmdBrowserRow)aElement;
    IAtomicValue dfltCellVal = IAtomicValue.NULL;
    IAtomicValue cellVal = browserRow.objCellVal( skid );
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
        retVal = currEditor;
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
    Gwid cmdGwid = Gwid.createCmd( skid.classId(), skid.strid(), browserRow.cmdInfo().id() );
    OptionSet cmdArgs = new OptionSet();
    if( !browserRow.cmdInfo().argDefs().isEmpty() ) {
      // аргумент команды
      String argName = browserRow.cmdInfo().argDefs().first().id();
      cmdArgs.setValue( argName, avValue );
    }
    // запросим у пользователя подтверждение
    // пользуемся технологией м5, получаем модель из контекста и используем готовый диалог для ее редактирования
    ISkConnectionSupplier connSup = tsContext.get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();
    // Текущее время
    long currTime = System.currentTimeMillis();
    // Идентификатор команды
    String instanceId = S5CommandIdGenerator.INSTANCE.nextId();
    Skid authorSkid = conn.coreApi().getCurrentUserInfo().userSkid();
    // Новая команда
    DtoCommand dtoCommand = new DtoCommand( currTime, instanceId, cmdGwid, authorSkid, cmdArgs );
    // создаем новую команду со всеми аргументами
    SkCommand editCmd = new SkCommand( dtoCommand );

    IM5Domain m5 = conn.scope().get( IM5Domain.class );

    IM5Model<SkCommand> model = m5.getModel( CommandM5Model.MID_SKCOMMAND_M5MODEL, SkCommand.class );
    ITsDialogInfo cdi = new TsDialogInfo( tsContext, null, ISkResources.STR_CMD_CONFIRM_DLG_CAPTION,
        ISkResources.STR_CMD_CONFIRM_DLG_TITLE, 0 );
    // в диалоге редактирования просим проверить аргумены и подтвердить посылку
    SkCommand confirmedCmd = M5GuiUtils.askEdit( tsContext, model, editCmd, cdi, model.getLifecycleManager( null ) );
    if( confirmedCmd != null ) {
      // ISkCommand cmd = coreApi.cmdService().sendCommand( cmdGwid, author.skid(), cmdArgs );
      browserRow.handleCommand( confirmedCmd );
    }
  }
}
