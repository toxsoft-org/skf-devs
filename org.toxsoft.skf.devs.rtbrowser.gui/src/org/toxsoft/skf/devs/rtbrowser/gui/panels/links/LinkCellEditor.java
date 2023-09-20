/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.links;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.km5.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Редактор описания связи в ячейке таблицы
 *
 * @author dima
 */
public class LinkCellEditor
    extends DialogCellEditor {

  /**
   * The composite widget containing the rightSkidsLabel widget
   */
  private Composite composite;

  /**
   * The label widget showing the right skids.
   */
  private Label rightSkidsLabel;

  private ITsGuiContext tsContext;

  /**
   * Creates a new link cell editor parented under the given control.
   *
   * @param parent the parent control
   * @param aContext app tsContext
   */
  public LinkCellEditor( Composite parent, ITsGuiContext aContext ) {
    this( parent, SWT.NONE, aContext );
    tsContext = aContext;
  }

  /**
   * Creates a new link cell editor parented under the given control.
   *
   * @param parent the parent control
   * @param style the style bits
   * @param aContext app tsContext
   * @since 2.1
   */
  public LinkCellEditor( Composite parent, int style, ITsGuiContext aContext ) {
    super( parent, style );
    tsContext = aContext;
  }

  @Override
  protected Control createContents( Composite cell ) {
    Color bg = cell.getBackground();
    composite = new Composite( cell, getStyle() );
    composite.setLayout( new BorderLayout() );
    composite.setBackground( bg );

    rightSkidsLabel = new Label( composite, SWT.LEFT );
    rightSkidsLabel.setBackground( bg );
    rightSkidsLabel.setFont( cell.getFont() );
    rightSkidsLabel.setLayoutData( BorderLayout.CENTER );
    return composite;
  }

  @Override
  protected Object openDialogBox( Control aCellEditorWindow ) {
    // пользуемся технологией м5, получаем модель из контекста и используем готовый диалог для ее редактирования
    ISkConnectionSupplier connSup = tsContext.get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();
    IDtoLinkFwd cellLinkInfo = (IDtoLinkFwd)getValue();

    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<IDtoLinkFwd> model = m5.getModel( DtoLinkFwdM5Model.MID_LINK_FWD_M5MODEL, IDtoLinkFwd.class );
    ITsDialogInfo cdi = TsDialogInfo.forEditEntity( tsContext );
    return M5GuiUtils.askEdit( tsContext, model, cellLinkInfo, cdi, model.getLifecycleManager( null ) );
  }

  @Override
  protected void updateContents( Object aValue ) {
    if( aValue != null ) {
      IDtoLinkFwd cellLinkInfo = (IDtoLinkFwd)aValue;
      rightSkidsLabel.setText( SkidListKeeper.KEEPER.ent2str( cellLinkInfo.rightSkids() ) );
    }
  }
}
