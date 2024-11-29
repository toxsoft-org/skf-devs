package org.toxsoft.skf.devs.rtbrowser.mws.e4;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.skf.devs.rtbrowser.mws.*;

/**
 * Plugin constants.
 *
 * @author dima
 */
@SuppressWarnings( "javadoc" )
public interface IRtBrowserConstants {

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_RTBROWSER = "org.toxsoft.skf.devs.rtbrowser.mws.perps.rtbrowser"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";  //$NON-NLS-1$
  String ICONID_APP                = "app-icon"; //$NON-NLS-1$

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
