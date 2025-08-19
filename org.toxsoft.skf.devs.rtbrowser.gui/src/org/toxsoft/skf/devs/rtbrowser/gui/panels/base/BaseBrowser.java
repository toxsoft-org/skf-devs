/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.base;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.skf.devs.rtbrowser.gui.IRtBrowserGuiConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * Реализация компоненты просмотра и редактирования текущих данных {@link IRtBrowserTable}
 *
 * @author dima
 */
abstract public class BaseBrowser
    extends AbstractLazyPanel<Control>
    implements IRtBrowserTable, IRealTimeSensitive {

  protected TsPanel           backPanel;
  protected TableViewer       viewer;
  protected IListEdit<Object> rows = new ElemArrayList<>();

  protected final ISkCoreApi             coreApi;
  protected ISkidList                    skidList      = ISkidList.EMPTY;
  protected IStringList                  rowPropIds    = IStringList.EMPTY;
  protected TableViewerColumn            summaryColumn = null;
  protected TsToolbar                    toolbar;
  protected IListEdit<TableViewerColumn> objColumns    = new ElemArrayList<>();

  /**
   * Создаем компоненту
   *
   * @param aCoreApi серверное API
   * @param aTsContext контекст приложения
   */
  public BaseBrowser( ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aTsContext );
    coreApi = aCoreApi;
  }

  private void contentNdInput( boolean isRecreate ) {
    if( !isRecreate ) {
      viewer.setContentProvider( new ArrayContentProvider() );
    }
    viewer.setInput( rows.toArray() );
  }

  protected void createTableColumns() {
    // первая колонка - название параметра
    TableViewerColumn nameColumn = new TableViewerColumn( viewer, SWT.NONE );
    nameColumn.getColumn().setText( nameColumnTitle() );
    nameColumn.getColumn().setWidth( 150 );
    nameColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        BaseBrowserRow row = (BaseBrowserRow)aCell.getElement();
        aCell.setText( row.propNameCell() );
      }
    } );
    // сортируем по щелчку на заголовке колонки
    nameColumn.getColumn().addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        if( viewer.getTable().getSortDirection() == SWT.UP ) {
          viewer.getTable().setSortDirection( SWT.DOWN );
        }
        else {
          viewer.getTable().setSortDirection( SWT.UP );
        }
        viewer.getTable().setSortColumn( nameColumn.getColumn() );
        viewer.refresh();
      }
    } );
    // вторая колонка - Gwid параметра
    TableViewerColumn idColumn = new TableViewerColumn( viewer, SWT.NONE );
    idColumn.getColumn().setText( idColumnTitle() );
    idColumn.getColumn().setWidth( 100 );
    idColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        BaseBrowserRow row = (BaseBrowserRow)aCell.getElement();
        aCell.setText( row.propIdCell() );
      }
    } );
    // сортируем по щелчку на заголовке колонки
    idColumn.getColumn().addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        if( viewer.getTable().getSortDirection() == SWT.UP ) {
          viewer.getTable().setSortDirection( SWT.DOWN );
        }
        else {
          viewer.getTable().setSortDirection( SWT.UP );
        }
        viewer.getTable().setSortColumn( idColumn.getColumn() );
        viewer.refresh();
      }
    } );

    viewer.setComparator( new ViewerComparator() {

      @Override
      public int compare( Viewer aV, Object aObj1, Object aObj2 ) {
        BaseBrowserRow row1 = (BaseBrowserRow)aObj1;
        BaseBrowserRow row2 = (BaseBrowserRow)aObj2;
        TableColumn column = ((TableViewer)aV).getTable().getSortColumn();
        if( column != null ) {
          if( column.equals( nameColumn.getColumn() ) ) {
            int result = row1.propNameCell().compareTo( row2.propNameCell() );
            if( ((TableViewer)aV).getTable().getSortDirection() == SWT.UP ) {
              return result;
            }
            return -result;
          }
          if( column.equals( idColumn.getColumn() ) ) {
            int result = row1.propIdCell().compareTo( row2.propIdCell() );
            if( ((TableViewer)aV).getTable().getSortDirection() == SWT.UP ) {
              return result;
            }
            return -result;
          }
        }
        return 0;
      }
    } );

    // третьей колонкой идет "Summary"
    if( skidList.size() > 1 ) {
      // column Summary
      summaryColumn = new TableViewerColumn( viewer, SWT.NONE );
      summaryColumn.getColumn().setText( ISkResources.STR_SUMMARY );
      summaryColumn.getColumn().setWidth( 90 );
      summaryColumn.setLabelProvider( new CellLabelProvider() {

        @Override
        public void update( ViewerCell aCell ) {
          BaseBrowserRow row = (BaseBrowserRow)aCell.getElement();
          aCell.setText( row.summaryCellVal().asString() );
        }
      } );
      setSummaryEditingSupport();
      toolbar.getAction( ACDEF_SUMMARY_COLUMN.id() ).setEnabled( true );
      toolbar.getAction( ACDEF_SUMMARY_COLUMN.id() ).setChecked( true );
    }
    else {
      toolbar.getAction( ACDEF_SUMMARY_COLUMN.id() ).setEnabled( false );
      toolbar.getAction( ACDEF_SUMMARY_COLUMN.id() ).setChecked( false );
    }

    // далее колонки - название объекта
    for( Skid objSkid : skidList ) {
      TableViewerColumn objColumn = new TableViewerColumn( viewer, SWT.NONE );
      // название колонки берем по имени объекта
      ISkObject obj = coreApi.objService().find( objSkid );
      // String columnName = obj.nmName().isBlank() ? obj.id() : obj.nmName() + " [" + objSkid + "]";
      String columnName = obj.id();
      objColumn.getColumn().setText( columnName );
      objColumn.getColumn().setToolTipText( obj.nmName() );
      objColumn.getColumn().setWidth( 90 );
      objColumn.setLabelProvider( new CellLabelProvider() {

        @Override
        public void update( ViewerCell aCell ) {
          BaseBrowserRow row = (BaseBrowserRow)aCell.getElement();
          aCell.setText( row.objCellVal( objSkid ).asString() );
        }

      } );
      // настройка редакторов ячеек
      setObjColumnEditingSupport( objColumn, objSkid );
    }

  }

  protected abstract String nameColumnTitle();

  protected String idColumnTitle() {
    return ISkResources.STR_GWID;
  }

  /**
   * Установка редактора для колонки объекта
   *
   * @param aObjColumn колонка
   * @param aObjSkid объект
   */
  abstract public void setObjColumnEditingSupport( TableViewerColumn aObjColumn, Skid aObjSkid );

  /**
   * Установка редактора для колонки "Summary"
   */
  public void setSummaryEditingSupport() {
    // nop
  }

  protected void createTableViewer() {
    viewer = new TableViewer( backPanel, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.MULTI );
    viewer.getTable().setHeaderVisible( true );
    viewer.getTable().setLinesVisible( true );
    viewer.getTable().addDisposeListener( aE -> disposeResources() );
  }

  @Override
  public boolean isPaused() {
    return guiTimersService().slowTimers().isListenerMuted( this );
  }

  @Override
  public void pause() {
    guiTimersService().slowTimers().muteListener( this );
  }

  @Override
  public void resume() {
    guiTimersService().slowTimers().unmuteListener( this );
  }

  @Override
  public ISkidList columnObjctsSkids() {
    return skidList;
  }

  @Override
  public void setColumnObjectSkids( ISkidList aObjIds ) {
    pause();
    disposeResources();
    skidList = aObjIds;
    createModel();
    recreateView();
    resume();
  }

  protected void recreateView() {
    if( isControlValid() ) {
      removeColumns();
      createTableColumns();
      contentNdInput( true );
      viewer.update( rows.toArray(), null );
      guiTimersService().slowTimers().addListener( this );
      backPanel.layout( true );
    }
  }

  /**
   * утилизация ранее задействованных ресурсов
   */
  protected void disposeResources() {
    for( Object row : rows ) {
      ((BaseBrowserRow)row).dispose();
    }
  }

  @Override
  public IStringList rowPropIds() {
    return rowPropIds;
  }

  @Override
  public void setRowPropIds( IStringList aRowPropIds ) {
    pause();
    disposeResources();
    rowPropIds = aRowPropIds;
    createModel();
    recreateView();
    resume();
  }

  /**
   * инициализация модели
   */
  protected void createModel() {
    if( !skidList.isEmpty() ) {
      ISkClassInfo classInfo = coreApi.sysdescr().getClassInfo( skidList.first().classId() );
      rows.clear();
      createRows( classInfo );
    }
  }

  abstract protected void createRows( ISkClassInfo aClassInfo );

  @Override
  public boolean isCommonColumnVisible() {
    return summaryColumn.getColumn().getWidth() > 0;
  }

  @Override
  public void setCommonColumnVisible( boolean aVisible ) {
    if( aVisible ) {
      summaryColumn.getColumn().setWidth( 100 );
    }
    else {
      summaryColumn.getColumn().setWidth( 0 );
    }
    backPanel.layout( true );
  }

  @Override
  public ITsSelectionProvider<Gwid> columnSelectionProvider() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ITsSelectionProvider<Gwid> rowSelectionProvider() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ITsSelectionProvider<Gwid> cellSelectionProvider() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    viewer.update( rows.toArray(), null );
  }

  /**
   * Физически создаем и вставляем визуальный компонент
   *
   * @param aParent родитель в который будет встраиваться таблица
   */
  @Override
  protected Control doCreateControl( Composite aParent ) {
    backPanel = new TsPanel( aParent, tsContext() );
    backPanel.setLayout( new BorderLayout() );

    // toolbar
    toolbar = new TsToolbar( tsContext() );
    // toolbar.setNameLabelText( "управление RtData" );
    toolbar.setIconSize( EIconSize.IS_32X32 );
    toolbar.addActionDefs( ACDEF_UPDATE_RESUME, ACDEF_UPDATE_PAUSE, ACDEF_SEPARATOR, ACDEF_SUMMARY_COLUMN );
    toolbar.createControl( backPanel );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( aActionId -> {
      if( aActionId.equals( ACDEF_UPDATE_PAUSE.id() ) ) {
        pause();
      }
      if( aActionId.equals( ACDEF_UPDATE_RESUME.id() ) ) {
        resume();
      }
      if( aActionId.equals( ACDEF_SUMMARY_COLUMN.id() ) ) {
        setCommonColumnVisible( toolbar.getAction( ACDEF_SUMMARY_COLUMN.id() ).isChecked() );
      }
    } );
    // сразу выставим правильно положение кнопок
    toolbar.getAction( ACDEF_UPDATE_RESUME.id() ).setChecked( true );
    toolbar.getAction( ACDEF_UPDATE_PAUSE.id() ).setChecked( false );
    toolbar.getAction( ACDEF_SUMMARY_COLUMN.id() ).setChecked( false );
    toolbar.getAction( ACDEF_SUMMARY_COLUMN.id() ).setEnabled( false );
    createTable();
    return backPanel;
  }

  protected void createTable() {
    createTableViewer();
    createTableColumns();
    contentNdInput( false );
    viewer.update( rows.toArray(), null );
    guiTimersService().slowTimers().addListener( this );
    backPanel.layout( true );
  }

  void removeColumns() {
    Table table = viewer.getTable();
    table.setRedraw( false );
    while( table.getColumnCount() > 0 ) {
      table.getColumns()[0].dispose();
    }
    table.setRedraw( true );
  }

}
