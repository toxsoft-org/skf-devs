package org.toxsoft.skf.devs.rtbrowser.skide.main;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.skide.IRtBrowserSkideSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tslib.av.opset.impl.OptionSetUtils;
import org.toxsoft.skf.devs.rtbrowser.gui.IRtBrowserGuiConstants;
import org.toxsoft.skide.core.api.AbstractSkidePlugin;
import org.toxsoft.skide.core.api.AbstractSkideUnit;
import org.toxsoft.skide.core.api.impl.AbstractSkideUnitPanel;

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
        TSID_ICON_ID, IRtBrowserGuiConstants.ICONID_RTBROWSER_APP //
    ), aContext, aCreator );
    unitActions().add( ACDEF_ABOUT );
  }

  @Override
  protected void doHandleAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_ABOUT: {
        // TODO display complete info about unit
        TsDialogUtils.info( getShell(), id() + '\n' + nmName() + '\n' + description() );
        break;
      }
      default: {
        // TODO display info about known but unhandled action
        TsDialogUtils.info( getShell(), aActionId );
      }
    }
  }

  @Override
  protected AbstractSkideUnitPanel doCreateUnitPanel( ITsGuiContext aContext ) {
    return new SkideUnitPanelRtBrowser( aContext, this );
  }

}
