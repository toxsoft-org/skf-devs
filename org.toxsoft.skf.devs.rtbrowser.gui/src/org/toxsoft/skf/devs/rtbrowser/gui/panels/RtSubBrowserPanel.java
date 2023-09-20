package org.toxsoft.skf.devs.rtbrowser.gui.panels;

import static org.toxsoft.skf.devs.rtbrowser.gui.IRtBrowserGuiConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.attrs.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.events.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.links.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Подпанель просмотра и редактирования значений полей:
 * <li>attrs</li>
 * <li>rtDatas</li>
 * <li>cmds</li>
 * <li>events</li>
 * <li>links</li>
 * <p>
 * сущностей типа ISkObject <br>
 * Отличается от {@link RtBrowserPanel} уменьшенным функционалом за счет отсутствия возможности выбрать/сменить класс
 *
 * @author dima
 */
public class RtSubBrowserPanel
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

  private CTabFolder tabFolder;

  private SkObjectsCheckList objectsList;

  private ISkClassInfo         classInfo = null;
  private final RtBrowserPanel mainRtBrowserPanel;

  /**
   * Конструктор компоненты
   *
   * @param aParent родительский компонент
   * @param aTsContext контекст приложения
   * @param aSkidList список объектов панели
   * @param aRtBrowserPanel главная панель компонента
   */
  public RtSubBrowserPanel( Composite aParent, ITsGuiContext aTsContext, ISkidList aSkidList,
      RtBrowserPanel aRtBrowserPanel ) {
    super( aParent, aTsContext );
    this.setLayout( new BorderLayout() );
    mainRtBrowserPanel = aRtBrowserPanel;
    ISkConnectionSupplier connSup = aTsContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();

    addDisposeListener( aE -> {
      // nop
    } );
    classInfo = conn.coreApi().sysdescr().findClassInfo( aSkidList.classIds().first() );

    Composite contentPanel = new Composite( this, SWT.NONE );

    contentPanel.setLayout( new BorderLayout() );

    bkPanel = new SashForm( contentPanel, SWT.HORIZONTAL );
    bkPanel.setLayoutData( BorderLayout.CENTER );

    objectsList = SkObjectsCheckList.createCheckList( bkPanel, TsLibUtils.EMPTY_STRING, conn.coreApi() );
    tabFolder = new CTabFolder( bkPanel, SWT.BORDER );
    tabFolder.setLayout( new BorderLayout() );
    bkPanel.setWeights( LEFT_WEIGHT, RIGHT_WEIGHT );

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
    linkEditor = new LinksBrowser( conn.coreApi(), tsContext(), mainRtBrowserPanel );
    // вставляем выбранные объекты
    objectsList.setObjSkids( aSkidList );
    aParent.layout( true );
  }

}
