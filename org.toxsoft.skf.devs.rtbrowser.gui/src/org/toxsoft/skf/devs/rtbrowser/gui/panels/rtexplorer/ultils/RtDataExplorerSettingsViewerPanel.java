package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.reports.gui.panels.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Панель просмотра списка наборов настроек {#link IRtDataExplorerSettings}.<br>
 *
 * @author dima
 */
public class RtDataExplorerSettingsViewerPanel
    extends TsPanel {

  private final ITsSelectionChangeListener<IRtDataExplorerSettings> dataChangeListener = ( aSource, aSelectedItem ) -> {
    this.selectedSettings = aSelectedItem;
  };

  private final ISkConnection                         conn;
  private IM5CollectionPanel<IRtDataExplorerSettings> settingsListPanel;
  private IRtDataExplorerSettings                     selectedSettings = null;

  /**
   * @return {#link IRtDataExplorerSettings} settings selected by user
   */
  public IRtDataExplorerSettings getSelected() {
    return selectedSettings;
  }

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public RtDataExplorerSettingsViewerPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    ISkConnectionSupplier connSup = aContext.get( ISkConnectionSupplier.class );
    // проверяем в контекте наличие информации о соединении
    if( tsContext().params().hasKey( PanelGwidSelector.OPDEF_CONN_ID_CHAIN.id() ) ) {
      IdChain idChain = PanelGwidSelector.OPDEF_CONN_ID_CHAIN.getValue( tsContext().params() ).asValobj();
      conn = connSup.getConn( idChain );
    }
    else {
      conn = connSup.defConn();
    }
    RtDataExplorerSettingsM5Model model = new RtDataExplorerSettingsM5Model( conn );
    m5().initTemporaryModel( model );

    // Не отображаем панель подробного просмотра
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_FALSE );
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE_HIDDEN.setValue( ctx.params(), AvUtils.AV_FALSE );
    settingsListPanel =
        model.panelCreator().createCollViewerPanel( ctx, model.getLifecycleManager( conn ).itemsProvider() );
    settingsListPanel.createControl( this );
    settingsListPanel.getControl().setLayoutData( BorderLayout.CENTER );
    settingsListPanel.refresh();
    settingsListPanel.addTsSelectionListener( dataChangeListener );
  }

}
