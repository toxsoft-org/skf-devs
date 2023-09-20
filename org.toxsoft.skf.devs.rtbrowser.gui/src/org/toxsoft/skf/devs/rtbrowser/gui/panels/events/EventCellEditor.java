/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.events;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.skf.devs.rtbrowser.gui.km5.*;
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Редактор описания события в ячейке таблицы
 *
 * @author dima
 */
public class EventCellEditor
    extends DialogCellEditor {

  /**
   * The composite widget containing the event label widgets
   */
  private Composite composite;

  /**
   * The label widget showing the event to generate.
   */
  private Label eventGwidLabel;

  private ITsGuiContext tsContext;

  /**
   * Creates a new event cell editor parented under the given control.
   *
   * @param parent the parent control
   * @param aContext app tsContext
   */
  public EventCellEditor( Composite parent, ITsGuiContext aContext ) {
    this( parent, SWT.NONE, aContext );
    tsContext = aContext;
  }

  /**
   * Creates a new event cell editor parented under the given control.
   *
   * @param parent the parent control
   * @param style the style bits
   * @param aContext app tsContext
   * @since 2.1
   */
  public EventCellEditor( Composite parent, int style, ITsGuiContext aContext ) {
    super( parent, style );
    tsContext = aContext;
  }

  @Override
  protected Control createContents( Composite cell ) {
    Color bg = cell.getBackground();
    composite = new Composite( cell, getStyle() );
    composite.setLayout( new BorderLayout() );
    composite.setBackground( bg );

    eventGwidLabel = new Label( composite, SWT.LEFT );
    eventGwidLabel.setBackground( bg );
    eventGwidLabel.setFont( cell.getFont() );
    eventGwidLabel.setLayoutData( BorderLayout.CENTER );
    return composite;
  }

  @Override
  protected Object openDialogBox( Control aCellEditorWindow ) {
    // пользуемся технологией м5, получаем модель из контекста и используем готовый диалог для ее редактирования
    ISkConnectionSupplier connSup = tsContext.get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();
    SkEvent cellEvent = (SkEvent)getValue();
    // получаем описание своего события
    IDtoEventInfo evInfo = conn.coreApi().sysdescr().getClassInfo( cellEvent.eventGwid().classId() ).events().list()
        .findByKey( cellEvent.eventGwid().propId() );
    // создаем в нем описания параметров и инициализируем значениями по умолчанию
    IOptionSetEdit params = new OptionSet();
    for( IDataDef dataDef : evInfo.paramDefs() ) {
      params.put( dataDef.id(), dataDef.defaultValue() );
    }
    SkEvent editEvent = new SkEvent( cellEvent.timestamp(), cellEvent.eventGwid(), params );
    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<SkEvent> model = m5.getModel( EventM5Model.MID_SKEVENT_M5MODEL, SkEvent.class );
    ITsDialogInfo cdi = TsDialogInfo.forEditEntity( tsContext );
    return M5GuiUtils.askEdit( tsContext, model, editEvent, cdi, model.getLifecycleManager( null ) );
  }

  @Override
  protected void updateContents( Object aValue ) {
    if( aValue != null ) {
      SkEvent cellEvent = (SkEvent)aValue;
      eventGwidLabel.setText( cellEvent.eventGwid().asString() );
    }

  }
}
