package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.eclipse.jface.window.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Utility methods.
 *
 * @author dima
 */
public class RtDataExplorerUtils {

  private static String PREFIX_TEMPLATE = "rtDataExplorerSettingsPack"; //$NON-NLS-1$

  private static IStridGenerator stridGenerator =
      new UuidStridGenerator( UuidStridGenerator.createState( PREFIX_TEMPLATE ) );

  /**
   * Save an actual settings.
   * <p>
   *
   * @param aCoreApi - server API
   * @param aPanel - panel to store settings
   */
  public static void saveActualSettings( ISkCoreApi aCoreApi, RtDataExplorerPanel aPanel ) {
    // get data to store
    GwidList gwidList = aPanel.allGwids();
    Point location = aPanel.getShell().getLocation();
    Point size = aPanel.getShell().getSize();
    // get name of settings
    var dialog = new SettingsPackNameDialog( aPanel.getShell() );
    // just to demonstrate how to set the title background color
    // dialog.setTitleAreaColor( Display.getDefault().getSystemColor( SWT.COLOR_DARK_GRAY ).getRGB() );

    // now open the dialog
    dialog.create();
    if( dialog.open() == Window.OK ) {
      System.out.println( dialog.getFirstName() );
      System.out.println( dialog.getLastName() );
      String settingsName = dialog.getFirstName();
      IDtoFullObject dtoSetting = makeSettingsPackDto( aCoreApi, settingsName, gwidList, location, size );
      DtoFullObject.defineFullObject( aCoreApi, dtoSetting );
    }
  }

  /**
   * Remove selected settings.
   * <p>
   *
   * @param aCoreApi - server API
   * @param aContext - app context
   */
  public static void removeSettings( ISkCoreApi aCoreApi, TsGuiContext aContext ) {
    // get setting pack id
    IRtDataExplorerSettings selPack = RtDataExplorerSettingSelector.selectSettingsPack( null, aContext );
    if( selPack != null ) {
      aCoreApi.objService().removeObject( new Skid( IRtDataExplorerSettings.CLASS_ID, selPack.strid() ) );
    }
  }

  /**
   * Load selected settings.
   * <p>
   *
   * @param aCoreApi - server API
   * @param aContext - app context
   * @return {@link RtDataExplorerSettings} - selected settings pack
   */
  public static IRtDataExplorerSettings selectSettings( ISkCoreApi aCoreApi, ITsGuiContext aContext ) {
    IRtDataExplorerSettings retVal = null;
    // get setting pack id
    IRtDataExplorerSettings selPack = RtDataExplorerSettingSelector.selectSettingsPack( null, aContext );
    if( selPack != null ) {
      // retVal = aCoreApi.objService().find( new Skid( IRtDataExplorerSettings.CLASS_ID, selPack.strid() ) );
      retVal = selPack;
    }
    return retVal;
  }

  private static IDtoFullObject makeSettingsPackDto( ISkCoreApi aCoreApi, String aName, GwidList aRtGwids,
      Point aLocation, Point aSize ) {
    Skid skid = new Skid( IRtDataExplorerSettings.CLASS_ID, stridGenerator.nextId() );
    DtoFullObject dtoSettingsPack = DtoFullObject.createDtoFullObject( skid, aCoreApi );
    dtoSettingsPack.attrs().setValue( AID_NAME, avStr( aName ) );
    dtoSettingsPack.attrs().setValue( RtDataExplorerSettings.ATRID_RTDATAEXPLORER_X, AvUtils.avInt( aLocation.x ) );
    dtoSettingsPack.attrs().setValue( RtDataExplorerSettings.ATRID_RTDATAEXPLORER_Y, AvUtils.avInt( aLocation.y ) );
    dtoSettingsPack.attrs().setValue( RtDataExplorerSettings.ATRID_RTDATAEXPLORER_WIDTH, AvUtils.avInt( aSize.x ) );
    dtoSettingsPack.attrs().setValue( RtDataExplorerSettings.ATRID_RTDATAEXPLORER_HEIGHT, AvUtils.avInt( aSize.y ) );

    String rtGwidsStr = GwidList.KEEPER.ent2str( aRtGwids );
    dtoSettingsPack.clobs().put( RtDataExplorerSettings.CLBID_RTDATAEXPLORER_PARAMS, rtGwidsStr );

    return dtoSettingsPack;
  }

  private static RtDataExplorerSettings findSettingsPack( ISkCoreApi aCoreApi, String aPackId ) {
    TsNullArgumentRtException.checkNull( aPackId );
    return aCoreApi.objService().find( new Skid( IRtDataExplorerSettings.CLASS_ID, aPackId ) );
  }

  /**
   * Prohibition of descendants creation.
   */
  private RtDataExplorerUtils() {
    // nop
  }

}
