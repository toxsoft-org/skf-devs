package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Панель для настроек {@link RtDataExplorerPanel} .
 * <p>
 *
 * @author dima
 */
public class RtDataExplorerSettingSelector
    extends AbstractTsDialogPanel<IRtDataExplorerSettings, ITsGuiContext> {

  ValedComboSelector<IRtDataExplorerSettings> settingPackSelector;

  /**
   * Constructor.
   *
   * @param aParent - parent
   * @param aOwnerDialog - owner dialog
   */
  public RtDataExplorerSettingSelector( Composite aParent,
      TsDialog<IRtDataExplorerSettings, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new GridLayout( 2, false ) );
    CLabel l = new CLabel( this, SWT.LEFT );
    l.setText( "Що есть щас:" );

    IList<IRtDataExplorerSettings> values = new ElemArrayList<>();
    ITsVisualsProvider<IRtDataExplorerSettings> visualsProvider = IRtDataExplorerSettings::nmName;
    settingPackSelector = new ValedComboSelector<>( tsContext(), values, visualsProvider );
    settingPackSelector.createControl( this )
        .setLayoutData( new GridData( GridData.FILL, GridData.FILL, true, false ) );
    // оставим пока для примера, может пригодится еще
    settingPackSelector.eventer().addListener( ( aSource, aEditFinished ) -> {
      IRtDataExplorerSettings selPack = settingPackSelector.selectedItem();
      if( selPack != null ) {
        // TODO
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( IRtDataExplorerSettings aData ) {
    IListEdit<IRtDataExplorerSettings> packs = new ElemArrayList<>();

    ISkConnectionSupplier connSup = tsContext().get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();
    ISkObjectService objService = conn.coreApi().objService();

    for( ISkObject settingsPack : objService.listObjs( IRtDataExplorerSettings.CLASS_ID, false ) ) {
      packs.add( (IRtDataExplorerSettings)settingsPack );
    }
    settingPackSelector.setItems( packs );
  }

  @Override
  protected IRtDataExplorerSettings doGetDataRecord() {
    return settingPackSelector.getValue();
  }

  // ------------------------------------------------------------------------------------
  //
  //

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
    TsDialogInfo dlgInfo =
        new TsDialogInfo( aContext, "Сохраненные настройки RtDataExplorer", "Выберите нужный пакет и нажмите Ok", 0 );
    dlgInfo.setMinSizeShellRelative( 28, 26 );
    dlgInfo.setMaxSizeShellRelative( 28, 26 );
    TsDialog<IRtDataExplorerSettings, ITsGuiContext> d = new TsDialog<>( dlgInfo, aDfltPack, aContext, creator );
    return d.execData();
  }

}
