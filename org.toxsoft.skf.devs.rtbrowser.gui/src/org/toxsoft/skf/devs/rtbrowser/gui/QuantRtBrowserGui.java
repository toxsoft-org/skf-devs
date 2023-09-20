/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.*;
import org.toxsoft.skf.devs.rtbrowser.gui.km5.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * @author dima
 */
public class QuantRtBrowserGui
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantRtBrowserGui() {
    super( QuantRtBrowserGui.class.getSimpleName() );
    // регистрация нужных М5 моделей
    KM5Utils.registerContributorCreator( KM5RtBrowserContributor.CREATOR );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IRtBrowserGuiConstants.init( aWinContext );

    ValedControlFactoriesRegistry vcReg = aWinContext.get( ValedControlFactoriesRegistry.class );
    vcReg.registerFactory( ValedSkidListEditor.FACTORY );
    vcReg.registerFactory( ValedAvValobjSkidListEditor.FACTORY );
  }

}
