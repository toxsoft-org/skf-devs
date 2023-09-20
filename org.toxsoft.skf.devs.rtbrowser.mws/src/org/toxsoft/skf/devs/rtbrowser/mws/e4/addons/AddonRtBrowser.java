package org.toxsoft.skf.devs.rtbrowser.mws.e4.addons;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.IQuantRegistrator;
import org.toxsoft.core.tsgui.mws.bases.MwsAbstractAddon;
import org.toxsoft.skf.devs.rtbrowser.gui.IRtBrowserGuiConstants;
import org.toxsoft.skf.devs.rtbrowser.gui.QuantRtBrowserGui;
import org.toxsoft.skf.devs.rtbrowser.mws.Activator;
import org.toxsoft.skf.devs.rtbrowser.mws.e4.IRtBrowserConstants;
import org.toxsoft.uskat.core.gui.QuantSkCoreGui;

/**
 * Plugin addon.
 *
 * @author dima
 */
public class AddonRtBrowser
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonRtBrowser() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IRtBrowserConstants.init( aWinContext );
    IRtBrowserGuiConstants.init( aWinContext );
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantSkCoreGui() );
    aQuantRegistrator.registerQuant( new QuantRtBrowserGui() );
  }
}
