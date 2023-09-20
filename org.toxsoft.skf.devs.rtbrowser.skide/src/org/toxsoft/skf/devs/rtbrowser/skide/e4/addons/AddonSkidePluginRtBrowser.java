package org.toxsoft.skf.devs.rtbrowser.skide.e4.addons;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.IQuantRegistrator;
import org.toxsoft.core.tsgui.mws.bases.MwsAbstractAddon;
import org.toxsoft.skf.devs.rtbrowser.gui.*;
import org.toxsoft.skf.devs.rtbrowser.skide.main.SkidePluginRtBrowser;
import org.toxsoft.skide.core.api.ISkideEnvironment;

/**
 * @author dima
 */
public class AddonSkidePluginRtBrowser
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkidePluginRtBrowser() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skEnv = aAppContext.get( ISkideEnvironment.class );
    skEnv.pluginsRegistrator().registerPlugin( SkidePluginRtBrowser.INSTANCE );
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantRtBrowserGui() );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IRtBrowserGuiConstants.init( aWinContext );
  }

}
