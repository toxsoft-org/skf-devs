package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

public class RtDataExplorer
    extends BaseBrowser {

  private GwidList gwids = new GwidList();

  /**
   * Создаем компоненту
   *
   * @param aCoreApi серверное API
   * @param aTsContext контекст приложения
   */
  public RtDataExplorer( ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aCoreApi, aTsContext );
  }

  @Override
  public void setObjColumnEditingSupport( TableViewerColumn aObjColumn, Skid aObjSkid ) {
    aObjColumn.setEditingSupport( new ExplorerRtDataValueEditingSupport( viewer, coreApi, tsContext() ) );
  }

  @Override
  protected void createTableColumns() {
    TableViewerColumn clsColumn = new TableViewerColumn( viewer, SWT.NONE );
    // название колонки берем по имени объекта
    // ISkObject obj = coreApi.objService().find( objSkid );
    // String columnName = obj.nmName().isBlank() ? obj.id() : obj.nmName() + " [" + objSkid + "]";
    String columnName = "Класс";
    clsColumn.getColumn().setText( columnName );
    clsColumn.getColumn().setToolTipText( "Класс" );
    clsColumn.getColumn().setWidth( 90 );
    clsColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        RtDataExplorerRow row = (RtDataExplorerRow)aCell.getElement();
        aCell.setText( row.getClassId() );
      }

    } );
    TableViewerColumn objColumn = new TableViewerColumn( viewer, SWT.NONE );
    // название колонки берем по имени объекта
    // ISkObject obj = coreApi.objService().find( objSkid );
    // String columnName = obj.nmName().isBlank() ? obj.id() : obj.nmName() + " [" + objSkid + "]";
    columnName = "Объект";
    objColumn.getColumn().setText( columnName );
    objColumn.getColumn().setToolTipText( "Объект" );
    objColumn.getColumn().setWidth( 90 );
    objColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        RtDataExplorerRow row = (RtDataExplorerRow)aCell.getElement();
        aCell.setText( row.getObjId() );
      }

    } );
    super.createTableColumns();
    TableViewerColumn valColumn = new TableViewerColumn( viewer, SWT.NONE );
    // название колонки берем по имени объекта
    // ISkObject obj = coreApi.objService().find( objSkid );
    // String columnName = obj.nmName().isBlank() ? obj.id() : obj.nmName() + " [" + objSkid + "]";
    columnName = "Значение";
    valColumn.getColumn().setText( columnName );
    valColumn.getColumn().setToolTipText( "Значение RT данного" );
    valColumn.getColumn().setWidth( 90 );
    valColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        RtDataExplorerRow row = (RtDataExplorerRow)aCell.getElement();
        aCell.setText( row.objCellVal( row.getSkid() ).asString() );
      }

    } );
    // настройка редакторов ячеек
    setObjColumnEditingSupport( valColumn, null );
  }

  @Override
  protected void createRows( ISkClassInfo aClassInfo ) {

    for( Gwid rtDataGwid : gwids ) {

      ISkClassInfo classInfo = coreApi.sysdescr().getClassInfo( rtDataGwid.classId() );
      // получаем полное описание rtData
      IStridablesList<IDtoRtdataInfo> dataInfoes = classInfo.rtdata().list();
      IDtoRtdataInfo rtDataInfo = dataInfoes.findByKey( rtDataGwid.propId() );
      rows.add( new RtDataExplorerRow( coreApi, new SkidList( rtDataGwid.skid() ), rtDataInfo ) );
    }
  }

  @Override
  protected String nameColumnTitle() {
    return ISkResources.STR_RTDATA_NAME;
  }

  public void addGwid( Gwid aSelGwid ) {

    pause();
    disposeResources();
    gwids.add( aSelGwid );
    createModel();
    recreateView();
    resume();
  }

  public void addGwids( IGwidList aSelGwids ) {

    pause();
    disposeResources();
    gwids.addAll( aSelGwids );
    createModel();
    recreateView();
    resume();
  }

  public void removeAllGwids() {

    pause();
    disposeResources();
    gwids.clear();
    createModel();
    recreateView();
    resume();
  }

  @Override
  protected void createModel() {
    rows.clear();
    createRows( null );
  }

}
