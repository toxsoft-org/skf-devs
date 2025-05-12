/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.*;
import org.toxsoft.skf.devs.rtbrowser.gui.km5.*;
import org.toxsoft.skf.reports.gui.panels.valed.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * @author dima
 */
public class QuantRtBrowserGui
    extends AbstractQuant
    implements ISkCoreExternalHandler {

  /**
   * Constructor.
   */
  public QuantRtBrowserGui() {
    super( QuantRtBrowserGui.class.getSimpleName() );
    // регистрация нужных М5 моделей
    KM5Utils.registerContributorCreator( KM5RtBrowserContributor.CREATOR );
    SkCoreUtils.registerCoreApiHandler( this );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IRtBrowserGuiConstants.init( aWinContext );

    ValedControlFactoriesRegistry vcReg = aWinContext.get( ValedControlFactoriesRegistry.class );
    if( !vcReg.hasFactory( ValedSkidListEditor.FACTORY_NAME ) ) {
      vcReg.registerFactory( ValedSkidListEditor.FACTORY );
    }
    if( !vcReg.hasFactory( ValedAvValobjSkidListEditor.FACTORY_NAME ) ) {
      vcReg.registerFactory( ValedAvValobjSkidListEditor.FACTORY );
    }
    if( !vcReg.hasFactory( ValedAvValobjGwidEditor.FACTORY_NAME ) ) {
      vcReg.registerFactory( ValedAvValobjGwidEditor.FACTORY );
    }
  }

  @Override
  public void processSkCoreInitialization( IDevCoreApi aCoreApi ) {
    // register abilities
    aCoreApi.userService().abilityManager().defineKind( IRtBrowserGuiConstants.ABKIND_RTBROWSER );
    aCoreApi.userService().abilityManager().defineAbility( IRtBrowserGuiConstants.ABILITY_ACCESS_RTBROWSER );
  }

}
