package org.toxsoft.skf.devs.rtbrowser.gui.panels;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.skf.devs.rtbrowser.gui.IRtBrowserGuiConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources.*;
import static org.toxsoft.uskat.core.gui.km5.sded.IKM5SdedConstants.*;

import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.devs.rtbrowser.gui.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.attrs.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.events.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.links.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas.*;
import org.toxsoft.skf.devs.rtbrowser.gui.widgets.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Панель просмотра и редактирования значений полей:
 * <li>attrs</li>
 * <li>rtDatas</li>
 * <li>cmds</li>
 * <li>events</li>
 * <li>links</li>
 * <p>
 * сущностей типа ISkObject
 *
 * @author vs
 * @author dima // transfer to SkIDE/MWS
 */
public class RtBrowserPanel
    extends TsPanel {

  private final ISkConnection conn;

  private SashForm bkPanel;

  private AttrsBrowser    attrEditor;
  private RtDataBrowser   dataEditor;
  private CommandsBrowser cmdEditor;
  private EventsBrowser   evtEditor;
  private LinksBrowser    linkEditor;
  private Control         rtDataBrowserControl = null;
  private Control         attrBrowserControl   = null;
  private Control         cmdBrowserControl    = null;
  private Control         eventBrowserControl  = null;
  private Control         linkBrowserControl   = null;

  private Composite  tbHolder;
  private CTabFolder tabFolder;

  private SkObjectsCheckList objectsList;

  private ISkClassInfo classInfo = null;

  private CLabelContribution classLabel;
  private TextContribution   fldFilterText;
  private CTabFolder         mainTabFolder;

  /**
   * Конструктор компоненты
   *
   * @param aParent родительский компонент
   * @param aTsContext контекст приложения
   */
  public RtBrowserPanel( Composite aParent, ITsGuiContext aTsContext ) {
    super( aParent, aTsContext );
    this.setLayout( new BorderLayout() );
    ISkConnectionSupplier connSup = aTsContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();

    addDisposeListener( aE -> {
      // nop
    } );
    // Вставляем панель для главной tab панели
    Composite mainTabPanel = new Composite( this, SWT.NONE );
    mainTabPanel.setLayout( new BorderLayout() );
    mainTabFolder = new CTabFolder( mainTabPanel, SWT.BORDER );
    mainTabFolder.setLayout( new BorderLayout() );
    CTabItem mainTabItem = new CTabItem( mainTabFolder, SWT.NONE );
    mainTabItem.setText( STR_MAIN_EDITOR );
    mainTabFolder.setSelection( 0 );

    // Composite contentPanel = new Composite( this, SWT.NONE );
    Composite contentPanel = new Composite( mainTabFolder, SWT.NONE );
    mainTabItem.setControl( contentPanel );

    contentPanel.setLayout( new BorderLayout() );

    tbHolder = new Composite( contentPanel, SWT.NONE );
    tbHolder.setLayoutData( BorderLayout.NORTH );

    createToolBar();

    bkPanel = new SashForm( contentPanel, SWT.HORIZONTAL );
    bkPanel.setLayoutData( BorderLayout.CENTER );

    objectsList = SkObjectsCheckList.createCheckList( bkPanel, TsLibUtils.EMPTY_STRING, conn.coreApi() );
    tabFolder = new CTabFolder( bkPanel, SWT.BORDER );
    tabFolder.setLayout( new BorderLayout() );
    bkPanel.setWeights( IRtBrowserGuiConstants.LEFT_WEIGHT, IRtBrowserGuiConstants.RIGHT_WEIGHT );

    objectsList.genericChangeEventer().addListener( aSource -> {
      if( objectsList.checkedObjects().size() > 0 ) {

        SkidList skidList = new SkidList();
        for( ISkObject obj : objectsList.checkedObjects() ) {
          skidList.add( new Skid( obj.classId(), obj.id() ) );
        }
        // данные
        dataEditor.setColumnObjectSkids( skidList );
        IStringListEdit dataIds = new StringArrayList();
        for( IDtoRtdataInfo dataInfo : classInfo.rtdata().list() ) {
          dataIds.add( dataInfo.id() );
        }
        // dataEditor.setRowRtdataIds( dataIds );
        dataEditor.setRowPropIds( dataIds );
        if( rtDataBrowserControl == null ) {
          rtDataBrowserControl = dataEditor.createControl( tabFolder );
          CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
          tabItem.setText( STR_RT_DATA );
          tabItem.setControl( rtDataBrowserControl );

          ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
          Image img = iconManager.loadStdIcon( ICONID_OBJ_DATA_EDIT, EIconSize.IS_24X24 );
          tabItem.setImage( img );

        }
        // атрибуты
        attrEditor.setColumnObjectSkids( skidList );
        IStringListEdit attrIds = new StringArrayList();
        for( IDtoAttrInfo attrInfo : classInfo.attrs().list() ) {
          attrIds.add( attrInfo.id() );
        }
        // attrEditor.setRowAttrIds( attrIds );
        attrEditor.setRowPropIds( attrIds );
        if( attrBrowserControl == null ) {
          attrBrowserControl = attrEditor.createControl( tabFolder );
          CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
          tabItem.setText( STR_ATTRS );
          tabItem.setControl( attrBrowserControl );
          ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
          Image img = iconManager.loadStdIcon( ICONID_OBJ_ATTR_EDIT, EIconSize.IS_24X24 );
          tabItem.setImage( img );

        }
        // команды
        cmdEditor.setColumnObjectSkids( skidList );
        IStringListEdit cmdIds = new StringArrayList();
        for( IDtoCmdInfo cmdInfo : classInfo.cmds().list() ) {
          cmdIds.add( cmdInfo.id() );
        }
        // cmdEditor.setRowCmdIds( cmdIds );
        cmdEditor.setRowPropIds( cmdIds );

        if( cmdBrowserControl == null ) {
          cmdBrowserControl = cmdEditor.createControl( tabFolder );
          CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
          tabItem.setText( STR_CMDS );
          tabItem.setControl( cmdBrowserControl );
          ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
          Image img = iconManager.loadStdIcon( ICONID_OBJ_CMD_EDIT, EIconSize.IS_24X24 );
          tabItem.setImage( img );

        }
        // события
        evtEditor.setColumnObjectSkids( skidList );
        IStringListEdit evtIds = new StringArrayList();
        for( IDtoEventInfo evtInfo : classInfo.events().list() ) {
          evtIds.add( evtInfo.id() );
        }
        // evtEditor.setRowEventIds( evtIds );
        evtEditor.setRowPropIds( evtIds );
        if( eventBrowserControl == null ) {
          eventBrowserControl = evtEditor.createControl( tabFolder );
          CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
          tabItem.setText( STR_EVENTS );
          tabItem.setControl( eventBrowserControl );
          ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
          Image img = iconManager.loadStdIcon( ICONID_OBJ_EVT_EDIT, EIconSize.IS_24X24 );
          tabItem.setImage( img );

        }
        // связи
        linkEditor.setColumnObjectSkids( skidList );
        IStringListEdit linkIds = new StringArrayList();
        for( IDtoLinkInfo linkInfo : classInfo.links().list() ) {
          linkIds.add( linkInfo.id() );
        }
        // linkEditor.setRowLinkIds( linkIds );
        linkEditor.setRowPropIds( linkIds );

        if( linkBrowserControl == null ) {
          linkBrowserControl = linkEditor.createControl( tabFolder );
          CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
          tabItem.setText( STR_LINKS );
          tabItem.setControl( linkBrowserControl );
          ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
          Image img = iconManager.loadStdIcon( ICONID_OBJ_LINK_EDIT, EIconSize.IS_24X24 );
          tabItem.setImage( img );

        }
        if( tabFolder.getSelectionIndex() < 0 ) {
          // если ни одна закладка не выбрана, то делаем текущей закладку с RtData
          tabFolder.setSelection( 0 );
        }
      }
    } );

    dataEditor = new RtDataBrowser( conn.coreApi(), tsContext() );
    attrEditor = new AttrsBrowser( conn.coreApi(), tsContext() );
    cmdEditor = new CommandsBrowser( conn.coreApi(), tsContext() );
    evtEditor = new EventsBrowser( conn.coreApi(), tsContext() );
    linkEditor = new LinksBrowser( conn.coreApi(), tsContext(), this );

    CTabItem gwidTabItem = new CTabItem( mainTabFolder, SWT.NONE );
    gwidTabItem.setText( "rt explorer" );

    // Composite contentPanel = new Composite( this, SWT.NONE );
    RtDataExplorerPanel explorerPanel = new RtDataExplorerPanel( mainTabFolder, tsContext() );
    gwidTabItem.setControl( explorerPanel );

    aParent.layout( true );
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

    tbm.add( new ControlContribution( "useFilter" ) { //$NON-NLS-1$

      @Override
      protected Control createControl( Composite aParent ) {
        Button btnUseFilter = new Button( aParent, SWT.CHECK );
        btnUseFilter.setText( STR_FILTER );
        btnUseFilter.addSelectionListener( new SelectionAdapter() {

          @Override
          public void widgetSelected( SelectionEvent aE ) {
            if( btnUseFilter.getSelection() ) {
              objectsList.setFilterText( fldFilterText.textField().getText() );
            }
            else {
              objectsList.setFilterText( TsLibUtils.EMPTY_STRING );
            }
          }

        } );
        return btnUseFilter;
      }
    } );

    fldFilterText = new TextContribution( "filterText", 150, SWT.BORDER | SWT.SINGLE ); //$NON-NLS-1$
    tbm.add( fldFilterText );
    // отключаем групповые пометки, зависает программа на больших кол-вах объектов
    // Action actCheckAll = new Action( "checkAll", IAction.AS_PUSH_BUTTON ) { //$NON-NLS-1$
    //
    // @Override
    // public void run() {
    // objectsList.checkAll();
    // SkidList skidList = new SkidList();
    // for( ISkObject obj : objectsList.checkedObjects() ) {
    // skidList.add( new Skid( obj.classId(), obj.id() ) );
    // }
    // dataEditor.setColumnObjectSkids( skidList );
    // attrEditor.setColumnObjectSkids( skidList );
    // cmdEditor.setColumnObjectSkids( skidList );
    // evtEditor.setColumnObjectSkids( skidList );
    // linkEditor.setColumnObjectSkids( skidList );
    //
    // }
    // };
    //
    // ImageDescriptor imd = iconManager.loadStdDescriptor( ICONID_CHECK_ALL, EIconSize.IS_24X24 );
    // actCheckAll.setImageDescriptor( imd );
    // actCheckAll.setToolTipText( STR_CHECK_ALL );
    // tbm.add( actCheckAll );

    Action actUncheckAll = new Action( "uncheckAll", IAction.AS_PUSH_BUTTON ) { //$NON-NLS-1$

      @Override
      public void run() {
        objectsList.uncheckAll();
        SkidList skidList = new SkidList();
        dataEditor.setColumnObjectSkids( skidList );
        attrEditor.setColumnObjectSkids( skidList );
        cmdEditor.setColumnObjectSkids( skidList );
        evtEditor.setColumnObjectSkids( skidList );
        linkEditor.setColumnObjectSkids( skidList );
      }
    };
    ImageDescriptor imd = iconManager.loadStdDescriptor( ICONID_UNCHECK_ALL, EIconSize.IS_24X24 );

    actUncheckAll.setImageDescriptor( imd );
    actUncheckAll.setToolTipText( STR_UNCHECK_ALL );
    tbm.add( actUncheckAll );

    tbm.add( new ControlContribution( "label1" ) { //$NON-NLS-1$

      @Override
      protected Control createControl( Composite aParent ) {
        CLabel l = new CLabel( aParent, SWT.CENTER );
        l.setText( STR_TARGET_CLASS );
        return l;
      }
    } );

    classLabel = new CLabelContribution( "labelClass", 300, TsLibUtils.EMPTY_STRING, SWT.BORDER | SWT.CENTER ); //$NON-NLS-1$
    tbm.add( classLabel );

    Action actBrowseClass = new Action( "browseClass", IAction.AS_PUSH_BUTTON ) { //$NON-NLS-1$

      @Override
      public void run() {

        classInfo = selectClass();
        if( classInfo != null ) {
          // очищаем все редакторы
          clearAll();
          objectsList.setClassId( classInfo.id() );
          String text = classInfo.nmName() + " (" + classInfo.id() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
          classLabel.label().setText( text );
          classLabel.label().setToolTipText( text );
        }
      }

    };
    actBrowseClass.setImageDescriptor( iconManager().loadStdDescriptor( ICONID_EDIT_FIND, EIconSize.IS_24X24 ) );
    actBrowseClass.setToolTipText( STR_SELECT_CLASS );
    tbm.add( actBrowseClass );

    Action actBrowseGwids = new Action( "browseGwids", IAction.AS_PUSH_BUTTON ) { //$NON-NLS-1$

      @Override
      public void run() {

        ExplorerDialog d = new ExplorerDialog( null, tsContext() );

        d.open();
      }

    };
    actBrowseGwids
        .setImageDescriptor( iconManager().loadStdDescriptor( ICONID_EDIT_FIND_REPLACE, EIconSize.IS_24X24 ) );
    actBrowseGwids.setToolTipText( "Rt data explorer" );
    tbm.add( actBrowseGwids );

    tbm.update( true );
    tb.pack();
    tb.getParent().layout( true );
  }

  private void clearAll() {
    boolean dirty = false;

    if( rtDataBrowserControl != null ) {
      rtDataBrowserControl.dispose();
      rtDataBrowserControl = null;
      dirty = true;
    }
    if( attrBrowserControl != null ) {
      attrBrowserControl.dispose();
      attrBrowserControl = null;
      dirty = true;
    }
    if( cmdBrowserControl != null ) {
      cmdBrowserControl.dispose();
      cmdBrowserControl = null;
      dirty = true;
    }
    if( eventBrowserControl != null ) {
      eventBrowserControl.dispose();
      eventBrowserControl = null;
      dirty = true;
    }
    if( linkBrowserControl != null ) {
      linkBrowserControl.dispose();
      linkBrowserControl = null;
      dirty = true;
    }
    tabFolder.dispose();
    tabFolder = new CTabFolder( bkPanel, SWT.BORDER );
    tabFolder.setLayout( new BorderLayout() );
    bkPanel.setWeights( IRtBrowserGuiConstants.LEFT_WEIGHT, IRtBrowserGuiConstants.RIGHT_WEIGHT );

    dataEditor = new RtDataBrowser( conn.coreApi(), tsContext() );
    attrEditor = new AttrsBrowser( conn.coreApi(), tsContext() );
    cmdEditor = new CommandsBrowser( conn.coreApi(), tsContext() );
    evtEditor = new EventsBrowser( conn.coreApi(), tsContext() );
    linkEditor = new LinksBrowser( conn.coreApi(), tsContext(), this );
    if( dirty ) {
      bkPanel.layout( true );
    }
  }

  protected ISkClassInfo selectClass() {
    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<ISkClassInfo> model = m5.getModel( MID_SDED_SK_CLASS_INFO, ISkClassInfo.class );
    TsDialogInfo cdi = new TsDialogInfo( tsContext(), null, DLG_C_SELECT_CLASS, DLG_T_SELECT_CLASS, 0 );
    cdi.setMinSizeShellRelative( 60, 90 );
    IM5LifecycleManager<ISkClassInfo> lm = model.getLifecycleManager( null );

    // GOGA --- 2025-06-11
    // // фильтруем классы с которыми не работает ISkSysdescr
    // IM5ItemsProvider<ISkClassInfo> itemsProvider = () -> {
    // IListEdit<ISkClassInfo> retVal = new ElemArrayList<>();
    // for( ISkClassInfo ci : lm.itemsProvider().listItems() ) {
    // // dima 05.12.24 fix NPE , TODO i don't understand but without SkObject RtBrowser fall down
    // // if( ci.id().equals( IGwHardConstants.GW_ROOT_CLASS_ID ) ) {
    // // continue;
    // // }
    // String claimerId = conn.coreApi().sysdescr().determineClassClaimingServiceId( ci.id() );
    // if( claimerId.equals( ISkSysdescr.SERVICE_ID ) || claimerId.equals( ISkAlarmService.SERVICE_ID ) ) {
    // retVal.add( ci );
    // }
    // }
    // return retVal;
    // };
    // ---

    return M5GuiUtils.askSelectItem( cdi, model, null, lm.itemsProvider(), null );
  }

  /**
   * Добавить новую закладку с объектами выбранными пользователем
   *
   * @param aSkidList выбранные объекты
   */
  public void addSubRtBrowser( ISkidList aSkidList ) {
    CTabItem subTabItem = new CTabItem( mainTabFolder, SWT.CLOSE );
    subTabItem.setText( STR_SUB_EDITOR );
    mainTabFolder.setSelection( subTabItem );
    RtSubBrowserPanel subPanel = new RtSubBrowserPanel( mainTabFolder, tsContext(), aSkidList, this );
    subTabItem.setControl( subPanel );
    mainTabFolder.getParent().layout( true );
  }

  static class ExplorerDialog
      extends TitleAreaDialog {

    private ITsGuiContext context;

    public ExplorerDialog( Shell aParentShell, ITsGuiContext aContext ) {
      super( aParentShell );
      context = aContext;
      int style = getShellStyle() | SWT.RESIZE | SWT.CLOSE;

      style = style & ~SWT.APPLICATION_MODAL;
      style |= SWT.MODELESS;

      setShellStyle( style );

    }

    @Override
    protected Control createDialogArea( Composite aParent ) {
      Composite container = new Composite( aParent, SWT.NONE );
      container.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
      container.setLayout( new BorderLayout() );
      RtDataExplorerPanel explorerPanel = new RtDataExplorerPanel( container, context );
      explorerPanel.setLayoutData( BorderLayout.CENTER );
      explorerPanel.setSize( 400, 300 );
      return explorerPanel;
    }

  }
}
