/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.links;

import static org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.ISkResources;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Реализация компоненты просмотра и генерации команд {@link ICommandBrowserTable}
 *
 * @author dima
 */
public class LinksBrowser
    extends BaseBrowser {

  private final RtBrowserPanel rtBrowserPanel;

  /**
   * Создаем компоненту
   *
   * @param aCoreApi серверное API
   * @param aTsContext контекст приложения
   * @param aRtBrowserPanel основная панель редактора
   */
  public LinksBrowser( ISkCoreApi aCoreApi, ITsGuiContext aTsContext, RtBrowserPanel aRtBrowserPanel ) {
    super( aCoreApi, aTsContext );
    rtBrowserPanel = aRtBrowserPanel;
  }

  @Override
  public void setObjColumnEditingSupport( TableViewerColumn aObjColumn, Skid aObjSkid ) {
    aObjColumn.setEditingSupport( new ObjectLinkValueEditingSupport( viewer, coreApi, tsContext(), aObjSkid ) );
  }

  @Override
  public void setSummaryEditingSupport() {
    // nop
  }

  @Override
  protected void createRows( ISkClassInfo aClassInfo ) {
    // получаем полное описание links
    IStridablesList<IDtoLinkInfo> linkInfoes = aClassInfo.links().list();
    for( String linkId : rowPropIds ) {
      IDtoLinkInfo linkInfo = linkInfoes.findByKey( linkId );
      rows.add( new LinkBrowserRow( coreApi, skidList, linkInfo ) );
    }

  }

  @Override
  protected void createTableViewer() {
    super.createTableViewer();
    createPopupMenu();
  }

  private void createPopupMenu() {
    // Create context menu
    Menu menuTable = new Menu( viewer.getTable() );
    viewer.getTable().setMenu( menuTable );

    // Create menu item
    MenuItem miTest = new MenuItem( menuTable, SWT.NONE );
    miTest.setText( STR_OPEN_SUBEDITOR );
    miTest.addSelectionListener( new SelectionListener() {

      @SuppressWarnings( "synthetic-access" )
      @Override
      public void widgetSelected( SelectionEvent aE ) {
        // получим объекты по выбранным связям
        SkidList selSkids = new SkidList();
        for( int selIndex : viewer.getTable().getSelectionIndices() ) {
          TableItem ti = viewer.getTable().getItem( selIndex );
          LinkBrowserRow selRow = (LinkBrowserRow)ti.getData();
          for( Skid cellSkid : selRow.skidList() ) {
            IDtoLinkFwd linkFwd = selRow.linkCellVal( cellSkid );
            for( Skid leftSkid : linkFwd.rightSkids() ) {
              selSkids.addAll( leftSkid );
              System.out.println( leftSkid );
            }
          }
        }
        if( selSkids.classIds().size() == 1 ) {
          rtBrowserPanel.addSubRtBrowser( selSkids );
        }
        else {
          TsDialogUtils.warn( getShell(), STR_ERR_TOO_MANY_CLASSES, Integer.valueOf( selSkids.classIds().size() ) );
        }
      }

      @Override
      public void widgetDefaultSelected( SelectionEvent aE ) {
        // nop
      }
    } );

    // Do not show menu, when no item is selected
    viewer.getTable().addListener( SWT.MenuDetect, event -> {
      if( viewer.getTable().getSelectionCount() <= 0 ) {
        event.doit = false;
      }
    } );
  }

  @Override
  protected String nameColumnTitle() {
    return ISkResources.STR_LINK_NAME;
  }

}
