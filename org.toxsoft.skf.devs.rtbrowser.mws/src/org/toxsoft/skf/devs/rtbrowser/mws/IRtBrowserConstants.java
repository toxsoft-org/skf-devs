package org.toxsoft.skf.devs.rtbrowser.mws;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author dima
 */
@SuppressWarnings( "javadoc" )
public interface IRtBrowserConstants {

  // ------------------------------------------------------------------------------------
  // E4

  // visual elements ids
  String E4_VISUAL_ELEM_ID_PERSPID_RTBROWSER    = "org.toxsoft.skf.devs.rtbrowser.mws.perps.rtbrowser";           //$NON-NLS-1$
  String E4_VISUAL_ELEM_ID_MENU_ITEEM_RTBROWSER = "org.toxsoft.skf.devs.rtbrowser.mws.handledmenuitem.rtbrowser"; //$NON-NLS-1$
  String E4_VISUAL_ELEM_ID_TOOL_ITEEM_RTBROWSER = "org.toxsoft.skf.devs.rtbrowser.mws.handledtoolitem.rtbrowser"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";        //$NON-NLS-1$
  String ICONID_APP_RT_BROWSER     = "app-rt-browser"; //$NON-NLS-1$
  String ICONID_FILE_GENERAL       = "file-general";   //$NON-NLS-1$
  String ICONID_FOLDER_GENERAL     = "folder-general"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IRtBrowserConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
