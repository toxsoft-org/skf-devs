package org.toxsoft.skf.devs.rtbrowser.skide.main;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.skide.IRtBrowserSkideSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tslib.av.opset.impl.OptionSetUtils;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.skf.devs.rtbrowser.gui.IRtBrowserGuiConstants;
import org.toxsoft.skide.core.api.AbstractSkidePlugin;
import org.toxsoft.skide.core.api.ISkideUnit;

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
        TSID_NAME, STR_SKIDE_N_RT_BROWSER, //
        TSID_DESCRIPTION, STR_SKIDE_D_RT_BROWSER, //
        TSID_ICON_ID, IRtBrowserGuiConstants.ICONID_RTBROWSER_APP //
    ) );
  }

  @Override
  protected void doCreateUnits( ITsGuiContext aContext, IStridablesListEdit<ISkideUnit> aUnitsList ) {
    aUnitsList.add( new SkideUnitRtBrowser( aContext, this ) );
  }

}
