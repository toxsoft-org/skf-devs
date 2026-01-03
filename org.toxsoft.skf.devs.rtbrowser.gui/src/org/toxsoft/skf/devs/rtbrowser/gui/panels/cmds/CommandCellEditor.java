/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.km5.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.core.impl.dto.*;
import org.toxsoft.uskat.s5.server.backend.supports.commands.impl.*;

/**
 * Редактор описания команды в ячейке таблицы
 *
 * @author dima
 */
public class CommandCellEditor
    extends DialogCellEditor {

  /**
   * The composite widget containing the command label widgets
   */
  private Composite composite;

  /**
   * The label widget showing the command to execute.
   */
  private Label cmdGwidLabel;

  private ITsGuiContext tsContext;

  /**
   * Creates a new command cell editor parented under the given control.
   *
   * @param parent the parent control
   * @param aContext app tsContext
   */
  public CommandCellEditor( Composite parent, ITsGuiContext aContext ) {
    this( parent, SWT.NONE, aContext );
    tsContext = aContext;
  }

  /**
   * Creates a new command cell editor parented under the given control.
   *
   * @param parent the parent control
   * @param style the style bits
   * @param aContext app tsContext
   * @since 2.1
   */
  public CommandCellEditor( Composite parent, int style, ITsGuiContext aContext ) {
    super( parent, style );
    tsContext = aContext;
  }

  @Override
  protected Control createContents( Composite cell ) {
    Color bg = cell.getBackground();
    composite = new Composite( cell, getStyle() );
    composite.setLayout( new BorderLayout() );
    composite.setBackground( bg );

    cmdGwidLabel = new Label( composite, SWT.LEFT );
    cmdGwidLabel.setBackground( bg );
    cmdGwidLabel.setFont( cell.getFont() );
    cmdGwidLabel.setLayoutData( BorderLayout.CENTER );
    return composite;
  }

  @Override
  protected Object openDialogBox( Control aCellEditorWindow ) {
    // пользуемся технологией м5, получаем модель из контекста и используем готовый диалог для ее редактирования
    ISkConnectionSupplier connSup = tsContext.get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();
    Gwid cellCommandGwid = (Gwid)getValue();
    // получаем описание своей команды
    IDtoCmdInfo cmdInfo = conn.coreApi().sysdescr().getClassInfo( cellCommandGwid.classId() ).cmds().list()
        .findByKey( cellCommandGwid.propId() );
    // создаем в нем описания аргументов и инициализируем значениями по умолчанию
    IOptionSetEdit cmdArgs = new OptionSet();
    for( IDataDef dataDef : cmdInfo.argDefs() ) {
      cmdArgs.put( dataDef.id(), getDefaultValue( dataDef ) );
    }
    // Текущее время
    long currTime = System.currentTimeMillis();
    // Идентификатор команды
    String instanceId = S5CommandIdGenerator.INSTANCE.nextId();
    Skid authorSkid = conn.coreApi().getCurrentUserInfo().userSkid();
    // Новая команда
    DtoCommand dtoCommand = new DtoCommand( currTime, instanceId, cellCommandGwid, authorSkid, cmdArgs );
    // создаем новую команду без значений аргументов
    SkCommand editCmd = new SkCommand( dtoCommand );

    IM5Domain m5 = conn.scope().get( IM5Domain.class );

    IM5Model<SkCommand> model = m5.getModel( CommandM5Model.MID_SKCOMMAND_M5MODEL, SkCommand.class );
    ITsDialogInfo cdi = TsDialogInfo.forEditEntity( tsContext );
    // в диалоге редактирования просим ввести аргументы команды
    return M5GuiUtils.askEdit( tsContext, model, editCmd, cdi, model.getLifecycleManager( null ) );
  }

  private static IAtomicValue getDefaultValue( IDataDef aDataDef ) {
    IAtomicValue retVal = aDataDef.defaultValue();
    switch( aDataDef.atomicType() ) {
      case BOOLEAN:
        retVal = AvUtils.avBool( true );
        break;
      case FLOATING:
        retVal = AvUtils.avFloat( 0.0 );
        break;
      case INTEGER:
        retVal = AvUtils.avInt( 0 );
        break;
      case STRING:
        retVal = AvUtils.avStr( "?" ); //$NON-NLS-1$
        break;
      case NONE:
      case TIMESTAMP:
      case VALOBJ:
      default:
        break;
    }
    return retVal;
  }

  @Override
  protected void updateContents( Object aValue ) {
    if( aValue != null ) {
      if( aValue instanceof Gwid cellCmdGwid ) {
        cmdGwidLabel.setText( cellCmdGwid.canonicalString() );
      }
      if( aValue instanceof ISkCommand cellCmd ) {
        cmdGwidLabel.setText( cellCmd.state().toString() );
      }
    }
  }
}
