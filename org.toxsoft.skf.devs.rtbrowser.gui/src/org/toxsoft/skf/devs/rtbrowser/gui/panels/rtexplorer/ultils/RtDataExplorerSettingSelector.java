package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils;

import static org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils.ISkResources.*;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;

/**
 * Панель для выбора пакета настроек {@link RtDataExplorerPanel} .
 * <p>
 *
 * @author dima
 */
public class RtDataExplorerSettingSelector
    extends AbstractTsDialogPanel<IRtDataExplorerSettings, ITsGuiContext> {

  // ValedComboSelector<IRtDataExplorerSettings> settingPackSelector;
  private RtDataExplorerSettingsViewerPanel settingsPanel;

  /**
   * Constructor.
   *
   * @param aParent - parent
   * @param aOwnerDialog - owner dialog
   */
  public RtDataExplorerSettingSelector( Composite aParent,
      TsDialog<IRtDataExplorerSettings, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    FillLayout fillLayout = new FillLayout();
    fillLayout.marginHeight = 5;
    fillLayout.marginWidth = 5;
    this.setLayout( fillLayout );
    // version use combo
    // CLabel l = new CLabel( this, SWT.LEFT );
    // l.setText( STR_N_STORED_SETTINGS );
    // IList<IRtDataExplorerSettings> values = new ElemArrayList<>();
    // ITsVisualsProvider<IRtDataExplorerSettings> visualsProvider = IRtDataExplorerSettings::nmName;
    // settingPackSelector = new ValedComboSelector<>( tsContext(), values, visualsProvider );
    // settingPackSelector.createControl( this )
    // .setLayoutData( new GridData( GridData.FILL, GridData.FILL, true, false ) );
    // // оставим пока для примера, может пригодится еще
    // settingPackSelector.eventer().addListener( ( aSource, aEditFinished ) -> {
    // IRtDataExplorerSettings selPack = settingPackSelector.selectedItem();
    // if( selPack != null ) {
    // // TODO
    // }
    // } );
    // version use list
    settingsPanel = new RtDataExplorerSettingsViewerPanel( this, tsContext() );

  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( IRtDataExplorerSettings aData ) {
    // nop
  }

  @Override
  protected IRtDataExplorerSettings doGetDataRecord() {
    return settingsPanel.getSelected();
  }

  /**
   * Выбор линии пакета настроек из выпадающего списка.
   * <p>
   *
   * @param aDfltPack {@link RtDataExplorerSettings} - настройки по умолчанию
   * @param aContext - контекст
   * @return RtDataExplorerSettings - выбранный пакет настроек или <b>null</b> в случает отказа от редактирования
   */
  public static final IRtDataExplorerSettings selectSettingsPack( RtDataExplorerSettings aDfltPack,
      ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<IRtDataExplorerSettings, ITsGuiContext> creator = RtDataExplorerSettingSelector::new;
    TsDialogInfo dlgInfo = new TsDialogInfo( aContext, STR_N_STORED_SETTINGS_CAPTION, STR_N_STORED_SETTINGS_TITLE, 0 );
    dlgInfo.setMinSizeShellRelative( 28, 26 );
    dlgInfo.setMaxSizeShellRelative( 28, 26 );
    TsDialog<IRtDataExplorerSettings, ITsGuiContext> d = new TsDialog<>( dlgInfo, aDfltPack, aContext, creator );
    return d.execData();
  }

}
