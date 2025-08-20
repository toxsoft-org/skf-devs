package org.toxsoft.skf.devs.rtbrowser.gui.panels;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.skf.devs.rtbrowser.gui.IRtBrowserGuiConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources.*;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils.*;
import org.toxsoft.skf.reports.gui.panels.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Панель просмотра и редактирования значений полей:
 * <li>rtDatas</li> любого набора gwid
 *
 * @author max
 */
public class RtDataExplorerPanel
    extends TsPanel {

  private final ISkConnection conn;

  private RtDataExplorer dataEditor;
  private Control        rtDataBrowserControl = null;

  private Composite  tbHolder;
  private CTabFolder tabFolder;

  /**
   * Конструктор компоненты
   *
   * @param aParent родительский компонент
   * @param aTsContext контекст приложения
   */
  public RtDataExplorerPanel( Composite aParent, ITsGuiContext aTsContext ) {
    super( aParent, aTsContext );
    this.setLayout( new BorderLayout() );
    ISkConnectionSupplier connSup = aTsContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();

    addDisposeListener( aE -> {
      // nop
    } );

    // Composite contentPanel = new Composite( this, SWT.NONE );
    Composite contentPanel = new Composite( this, SWT.NONE );
    contentPanel.setLayout( new BorderLayout() );

    tbHolder = new Composite( contentPanel, SWT.NONE );
    tbHolder.setLayoutData( BorderLayout.NORTH );

    createToolBar();

    tabFolder = new CTabFolder( contentPanel, SWT.BORDER );
    tabFolder.setLayout( new BorderLayout() );

    dataEditor = new RtDataExplorer( conn.coreApi(), tsContext() );
    aParent.layout( true );

    if( rtDataBrowserControl == null ) {
      rtDataBrowserControl = dataEditor.createControl( tabFolder );
      CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
      tabItem.setText( STR_RT_DATA );
      tabItem.setControl( rtDataBrowserControl );

      ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
      Image img = iconManager.loadStdIcon( ICONID_OBJ_DATA_EDIT, EIconSize.IS_24X24 );
      tabItem.setImage( img );

    }

    if( tabFolder.getSelectionIndex() < 0 ) {
      // если ни одна закладка не выбрана, то делаем текущей закладку с RtData
      tabFolder.setSelection( 0 );
    }

  }

  void createToolBar() {

    BorderLayout bl = new BorderLayout();
    bl.setMargins( 8, 8, 8, 8 );
    tbHolder.setLayout( bl );

    ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
    Image img = iconManager.loadStdIcon( ICONID_GRADIENT, EIconSize.IS_48X48 );
    tbHolder.setBackgroundImage( img );

    ToolBarManager tbm = new ToolBarManager( SWT.FLAT );
    ToolBar tb = tbm.createControl( tbHolder );

    // tbm.add( new ControlContribution( "label1" ) { //$NON-NLS-1$
    //
    // @Override
    // protected Control createControl( Composite aParent ) {
    // CLabel l = new CLabel( aParent, SWT.CENTER );
    // l.setText( "Добавить" );
    // return l;
    // }
    // } );
    // добавить список Gwid
    Action actAddGwids = new Action( "addGwids", IAction.AS_PUSH_BUTTON ) { //$NON-NLS-1$

      @Override
      public void run() {
        IDialogPanelCreator<IGwidList, ITsGuiContext> creator = PanelGwidListSelector::new;

        TsDialogInfo dlgInfo =
            new TsDialogInfo( tsContext(), getShell(), STR_SEL_RTDATA_CAPTION, STR_SEL_RTDATA_TITLE, 0 );
        dlgInfo.setMinSizeShellRelative( 200, 400 );
        dlgInfo.setMaxSizeShellRelative( 200, 400 );

        TsDialog<IGwidList, ITsGuiContext> d = new TsDialog<>( dlgInfo, null, tsContext(), creator );

        IGwidList selGwid = d.execData();
        // Gwid selGwid = PanelGwidSelector.selectGwid( null, tsContext(), ESkClassPropKind.RTDATA, null );

        if( selGwid != null ) {
          dataEditor.addGwids( selGwid );
        }
      }

    };
    actAddGwids.setImageDescriptor( iconManager().loadStdDescriptor( ICONID_LIST_ADD, EIconSize.IS_24X24 ) );
    actAddGwids.setToolTipText( STR_SEL_RTDATA_TOOLTIP );
    tbm.add( actAddGwids );

    // tbm.add( new ControlContribution( "removeSelected" ) { //$NON-NLS-1$
    //
    // @Override
    // protected Control createControl( Composite aParent ) {
    // CLabel l = new CLabel( aParent, SWT.CENTER );
    // l.setText( "удалить" );
    // return l;
    // }
    // } );

    // удалить выбранный Gwid
    Action actRemoveSelected = new Action( "removeSelected", IAction.AS_PUSH_BUTTON ) { //$NON-NLS-1$

      @Override
      public void run() {

        dataEditor.removeSelGwid();

      }
    };
    ImageDescriptor imd = iconManager.loadStdDescriptor( ICONID_LIST_REMOVE, EIconSize.IS_24X24 );

    actRemoveSelected.setImageDescriptor( imd );
    actRemoveSelected.setToolTipText( STR_REMOVE_RTDATA_TOOLTIP );
    tbm.add( actRemoveSelected );
    tbm.add( new Separator() );

    // сохранить настройки
    // tbm.add( new ControlContribution( "saveSetting" ) { //$NON-NLS-1$
    //
    // @Override
    // protected Control createControl( Composite aParent ) {
    // CLabel l = new CLabel( aParent, SWT.CENTER );
    // l.setText( "Сохранить" );
    // return l;
    // }
    // } );

    Action actSaveSettings = new Action( "saveSetting", IAction.AS_PUSH_BUTTON ) { //$NON-NLS-1$

      @Override
      public void run() {
        RtDataExplorerPanel outerInstance = RtDataExplorerPanel.this;
        RtDataExplorerUtils.saveActualSettings( outerInstance.conn.coreApi(), outerInstance );
      }
    };
    imd = iconManager.loadStdDescriptor( ICONID_DOCUMENT_SAVE_AS, EIconSize.IS_24X24 );

    actSaveSettings.setImageDescriptor( imd );
    actSaveSettings.setToolTipText( STR_SAVE_SETTING_TOOLTIP );
    tbm.add( actSaveSettings );
    tbm.add( new Separator() );

    // загрузить настройки
    // tbm.add( new ControlContribution( "loadSetting" ) { //$NON-NLS-1$
    //
    // @Override
    // protected Control createControl( Composite aParent ) {
    // CLabel l = new CLabel( aParent, SWT.CENTER );
    // l.setText( "Загрузить" );
    // return l;
    // }
    // } );

    Action actLoadSetting = new Action( "loadSettings", IAction.AS_PUSH_BUTTON ) { //$NON-NLS-1$

      @Override
      public void run() {
        IRtDataExplorerSettings selSettings = selectSettingsPack();

        RtDataExplorerPanel outerInstance = RtDataExplorerPanel.this;
        // в этой версии панель возникает снизу от окна RtDataExplorer
        // IRtDataExplorerSettings selSettings =
        // RtDataExplorerUtils.selectSettings( outerInstance.conn.coreApi(), outerInstance.tsContext() );
        if( selSettings != null ) {
          dataEditor.removeAllGwids();
          dataEditor.addGwids( selSettings.listUserRtGwids() );
          outerInstance.getShell().setLocation( selSettings.location().x, selSettings.location().y );
          outerInstance.getShell().setSize( selSettings.size() );
        }
      }

    };
    imd = iconManager.loadStdDescriptor( ICONID_DOCUMENT_EXPORT, EIconSize.IS_24X24 );

    actLoadSetting.setImageDescriptor( imd );
    actLoadSetting.setToolTipText( STR_LOAD_SETTINGS_TOOLTIP );
    tbm.add( actLoadSetting );

    // удалить настройки
    // tbm.add( new ControlContribution( "removeSetting" ) { //$NON-NLS-1$
    //
    // @Override
    // protected Control createControl( Composite aParent ) {
    // CLabel l = new CLabel( aParent, SWT.CENTER );
    // l.setText( "Удалить" );
    // return l;
    // }
    // } );

    Action actRemoveSetting = new Action( "removeSettings", IAction.AS_PUSH_BUTTON ) { //$NON-NLS-1$

      @Override
      public void run() {

        RtDataExplorerPanel outerInstance = RtDataExplorerPanel.this;
        IRtDataExplorerSettings selItem = selectSettingsPack();
        if( selItem != null ) {
          StringBuilder sb = new StringBuilder();
          ISkObject obj = outerInstance.conn.coreApi().objService().find( selItem.skid() );
          sb.append( obj.nmName() + "\n" ); //$NON-NLS-1$
          if( TsDialogUtils.askYesNoCancel( getShell(), FMT_STR_REMOVE_SETTING, sb.toString() ) == ETsDialogCode.YES ) {
            outerInstance.conn.coreApi().objService().removeObject( selItem.skid() );
          }
        }
      }
    };
    imd = iconManager.loadStdDescriptor( ICONID_EDIT_DELETE, EIconSize.IS_24X24 );

    actRemoveSetting.setImageDescriptor( imd );
    actRemoveSetting.setToolTipText( STR_REMOVE_SETTINGS_TOOLTIP );
    tbm.add( actRemoveSetting );

    //
    tbm.update( true );
    tb.pack();
    tb.getParent().layout( true );
  }

  /**
   * @return all gwids of panel
   */
  public GwidList allGwids() {
    return dataEditor.allGwids();
  }

  /**
   * Get user choice
   *
   * @return {@link IRtDataExplorerSettings} selected pack
   */
  private IRtDataExplorerSettings selectSettingsPack() {
    IDialogPanelCreator<IRtDataExplorerSettings, ITsGuiContext> creator = RtDataExplorerSettingSelector::new;

    TsDialogInfo dlgInfo =
        new TsDialogInfo( tsContext(), getShell(), STR_SAVED_SETTINGS_CAPTION, STR_SAVED_SETTINGS_TITLE, 0 );

    TsDialog<IRtDataExplorerSettings, ITsGuiContext> d = new TsDialog<>( dlgInfo, null, tsContext(), creator );

    IRtDataExplorerSettings selSettings = d.execData();
    return selSettings;
  }
}
