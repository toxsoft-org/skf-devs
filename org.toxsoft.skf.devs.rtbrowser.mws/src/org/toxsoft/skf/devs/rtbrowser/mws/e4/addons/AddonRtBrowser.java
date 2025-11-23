package org.toxsoft.skf.devs.rtbrowser.mws.e4.addons;

import static org.toxsoft.skf.devs.rtbrowser.gui.IRtBrowserGuiConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.mws.IRtBrowserConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.devs.rtbrowser.gui.*;
import org.toxsoft.skf.devs.rtbrowser.mws.*;
import org.toxsoft.skf.devs.rtbrowser.mws.Activator;
import org.toxsoft.skf.devs.rtbrowser.mws.e4.*;
import org.toxsoft.uskat.core.gui.*;
import org.toxsoft.uskat.core.gui.utils.*;
import org.toxsoft.uskat.core.impl.*;

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
    // implement access rights
    GuiE4ElementsToAbilitiesBinder binder = new GuiE4ElementsToAbilitiesBinder( new TsGuiContext( aWinContext ) );
    binder.bindPerspective( ABILITYID_RTBROWSER_PERSP_ACCESS, E4_VISUAL_ELEM_ID_PERSPID_RTBROWSER );
    binder.bindMenuElement( ABILITYID_RTBROWSER_PERSP_ACCESS, E4_VISUAL_ELEM_ID_MENU_ITEEM_RTBROWSER );
    binder.bindToolItem( ABILITYID_RTBROWSER_PERSP_ACCESS, E4_VISUAL_ELEM_ID_TOOL_ITEEM_RTBROWSER );
    SkCoreUtils.registerCoreApiHandler( binder );
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantSkCoreGui() );
    aQuantRegistrator.registerQuant( new QuantRtBrowserGui() );
  }
}
