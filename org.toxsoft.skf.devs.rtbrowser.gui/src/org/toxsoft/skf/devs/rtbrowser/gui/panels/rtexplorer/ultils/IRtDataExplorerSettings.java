package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Container to store settings of {@link RtDataExplorerPanel}.
 *
 * @author dima
 */
@SuppressWarnings( "javadoc" )
public interface IRtDataExplorerSettings
    extends ISkObject {

  String CLASS_ID = ISkHardConstants.SK_ID + ".RtDataExplorerSettings"; //$NON-NLS-1$
  // ------------------------------------------------------------------------------------

  String localName();

  IGwidList listUserRtGwids();

  Point size();

  Point location();

}
