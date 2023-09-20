/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.events;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Реализация компоненты просмотра и генерации событий {@link ICommandBrowserTable}
 *
 * @author dima
 */
public class EventsBrowser
    extends BaseBrowser {

  /**
   * Создаем компоненту
   *
   * @param aCoreApi серверное API
   * @param aTsContext контекст приложения
   */
  public EventsBrowser( ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aCoreApi, aTsContext );
  }

  @Override
  public void setObjColumnEditingSupport( TableViewerColumn aObjColumn, Skid aObjSkid ) {
    aObjColumn.setEditingSupport( new ObjectEventValueEditingSupport( viewer, coreApi, tsContext(), aObjSkid ) );
  }

  @Override
  protected void createRows( ISkClassInfo aClassInfo ) {
    IStridablesList<IDtoEventInfo> eventInfoes = aClassInfo.events().list();

    for( String eventId : rowPropIds ) {
      IDtoEventInfo eventInfo = eventInfoes.findByKey( eventId );
      rows.add( new EventBrowserRow( coreApi, skidList, eventInfo ) );
    }

  }

  @Override
  protected String nameColumnTitle() {
    return ISkResources.STR_EVENT_NAME;
  }

}
