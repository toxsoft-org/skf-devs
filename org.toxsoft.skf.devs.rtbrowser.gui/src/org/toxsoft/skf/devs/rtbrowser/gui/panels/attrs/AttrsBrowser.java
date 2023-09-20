/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.attrs;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Реализация компоненты просмотра и редактирования значений атрибутов {@link IAttrBrowserTable}
 *
 * @author dima
 */
public class AttrsBrowser
    extends BaseBrowser {

  /**
   * Создаем компоненту
   *
   * @param aCoreApi серверное API
   * @param aTsContext контекст приложения
   */
  public AttrsBrowser( ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aCoreApi, aTsContext );
  }

  @Override
  public void setObjColumnEditingSupport( TableViewerColumn aObjColumn, Skid aObjSkid ) {
    aObjColumn.setEditingSupport( new ObjectAttrValueEditingSupport( viewer, coreApi, tsContext(), aObjSkid ) );

  }

  @Override
  public void setSummaryEditingSupport() {
    summaryColumn.setEditingSupport( new SummaryAttrValueEditingSupport( viewer, coreApi, tsContext(), skidList ) );
  }

  @Override
  protected void createRows( ISkClassInfo aClassInfo ) {
    IStridablesList<IDtoAttrInfo> attrInfoes = aClassInfo.attrs().list();
    rows.clear();
    for( String attrId : rowPropIds ) {
      IDtoAttrInfo attrInfo = attrInfoes.findByKey( attrId );
      rows.add( new AttrBrowserRow( coreApi, skidList, attrInfo ) );
    }

  }

  @Override
  protected String nameColumnTitle() {
    return ISkResources.STR_ATTR_NAME;
  }

}
