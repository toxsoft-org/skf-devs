/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds;

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
 * Реализация компоненты просмотра и генерации команд {@link ICommandBrowserTable}
 *
 * @author dima
 */
public class CommandsBrowser
    extends BaseBrowser {

  /**
   * Создаем компоненту
   *
   * @param aCoreApi серверное API
   * @param aTsContext контекст приложения
   */
  public CommandsBrowser( ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aCoreApi, aTsContext );
  }

  @Override
  public void setObjColumnEditingSupport( TableViewerColumn aObjColumn, Skid aObjSkid ) {
    // dima 30.08.23 не используем диалог потому что он сыроват
    // aObjColumn.setEditingSupport( new ObjectCmdValueDlgEditingSupport( viewer, coreApi, tsContext(), aObjSkid ) );
    aObjColumn.setEditingSupport( new ObjectCmdValueEditingSupport( viewer, coreApi, tsContext(), aObjSkid ) );

  }

  @Override
  public void setSummaryEditingSupport() {
    summaryColumn.setEditingSupport( new SummaryCmdValueEditingSupport( viewer, coreApi, tsContext(), skidList ) );
  }

  @Override
  protected void createRows( ISkClassInfo aClassInfo ) {
    IStridablesList<IDtoCmdInfo> cmdInfoes = aClassInfo.cmds().list();
    rows.clear();
    for( String cmdId : rowPropIds ) {
      IDtoCmdInfo cmdInfo = cmdInfoes.findByKey( cmdId );
      rows.add( new CmdBrowserRow( coreApi, skidList, cmdInfo ) );
    }

  }

  @Override
  protected String nameColumnTitle() {
    return ISkResources.STR_CMD_NAME;
  }

}
