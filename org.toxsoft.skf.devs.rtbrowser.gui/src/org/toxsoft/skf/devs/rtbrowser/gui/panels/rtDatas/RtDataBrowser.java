/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Реализация компоненты просмотра и редактирования текущих данных {@link IRtBrowserTable}
 *
 * @author dima
 */
public class RtDataBrowser
    extends BaseBrowser {

  /**
   * Создаем компоненту
   *
   * @param aCoreApi серверное API
   * @param aTsContext контекст приложения
   */
  public RtDataBrowser( ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aCoreApi, aTsContext );
  }

  @Override
  public void setObjColumnEditingSupport( TableViewerColumn aObjColumn, Skid aObjSkid ) {
    aObjColumn.setEditingSupport( new ObjectRtDataValueEditingSupport( viewer, coreApi, tsContext(), aObjSkid ) );
  }

  @Override
  public void setSummaryEditingSupport() {
    summaryColumn.setEditingSupport( new SummaryRtDataValueEditingSupport( viewer, coreApi, tsContext(), skidList ) );
  }

  @Override
  protected void createRows( ISkClassInfo aClassInfo ) {
    // получаем полное описание rtData
    IStridablesList<IDtoRtdataInfo> dataInfoes = aClassInfo.rtdata().list();

    for( String rtDataId : rowPropIds ) {
      IDtoRtdataInfo rtDataInfo = dataInfoes.findByKey( rtDataId );
      rows.add( new RtDataBrowserRow( coreApi, skidList, rtDataInfo ) );
    }
  }

  @Override
  protected String nameColumnTitle() {
    return ISkResources.STR_RTDATA_NAME;
  }

}
