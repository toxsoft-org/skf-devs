package org.toxsoft.skf.devs.rtbrowser.skide.main;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.skide.IRtBrowserSkideSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.api.ucateg.ISkideUnitCategoryConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.skf.devs.rtbrowser.gui.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * SkiDE unit: RtBrowser unit.
 *
 * @author dima
 */
public class SkideUnitRtBrowser
    extends AbstractSkideUnit {

  /**
   * The plugin ID.
   */
  public static final String UNIT_ID = SKIDE_FULL_ID + ".unit.rtBrowser"; //$NON-NLS-1$

  SkideUnitRtBrowser( ITsGuiContext aContext, AbstractSkidePlugin aCreator ) {
    super( UNIT_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_N_RT_BROWSER, //
        TSID_DESCRIPTION, STR_SKIDE_D_RT_BROWSER, //
        OPDEF_SKIDE_UNIT_CATEGORY, UCATEGID_DEVELOPMENT_DEBUG, //
        TSID_ICON_ID, IRtBrowserGuiConstants.ICONID_RTBROWSER_APP //
    ), aContext, aCreator );
    unitActions().add( ACDEF_ABOUT );
  }

  @Override
  protected AbstractSkideUnitPanel doCreateUnitPanel( ITsGuiContext aContext ) {
    return new SkideUnitPanelRtBrowser( aContext, this );
  }

}
