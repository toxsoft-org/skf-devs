package org.toxsoft.skf.devs.rtbrowser.skide.main;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.skide.l10n.ISkfDevsRtBrowserSkideSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skf.devs.rtbrowser.gui.*;
import org.toxsoft.skide.core.api.*;

/**
 * SkIDE plugin: RtBrowser.
 *
 * @author dima
 */
public class SkidePluginRtBrowser
    extends AbstractSkidePlugin {

  /**
   * The plugin ID.
   */
  public static final String SKIDE_PLUGIN_ID = SKIDE_FULL_ID + ".plugin.rtBrowser"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final AbstractSkidePlugin INSTANCE = new SkidePluginRtBrowser();

  SkidePluginRtBrowser() {
    super( SKIDE_PLUGIN_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_PLUGIN_SKF_DEVS_RTBROWSER, //
        TSID_DESCRIPTION, STR_PLUGIN_SKF_DEVS_RTBROWSER_D, //
        TSID_ICON_ID, IRtBrowserGuiConstants.ICONID_RTBROWSER_APP //
    ) );
  }

  @Override
  protected void doCreateUnits( ITsGuiContext aContext, IStridablesListEdit<ISkideUnit> aUnitsList ) {
    aUnitsList.add( new SkideUnitRtBrowser( aContext, this ) );
  }

}
