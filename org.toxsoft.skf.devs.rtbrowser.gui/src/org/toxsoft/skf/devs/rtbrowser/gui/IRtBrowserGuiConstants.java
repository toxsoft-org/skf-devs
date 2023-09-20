package org.toxsoft.skf.devs.rtbrowser.gui;

import static org.toxsoft.skf.devs.rtbrowser.gui.IRtBrowserSharedResources.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author dima
 */
@SuppressWarnings( "javadoc" )
public interface IRtBrowserGuiConstants {

  int LEFT_WEIGHT  = 30;
  int RIGHT_WEIGHT = 70;

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICONID_FIELD_NAME = "ICONID_";           //$NON-NLS-1$
  String ICONID_RTBROWSER_LOGO       = "3d-logo-vetrol-2";  //$NON-NLS-1$
  String ICONID_RTBROWSER_APP        = "app-rt-browser";    //$NON-NLS-1$
  String ICONID_SDED_CLASS           = "sded-class";        //$NON-NLS-1$
  String ICONID_SDED_CLASSES_LIST    = "sded-classes-list"; //$NON-NLS-1$
  String ICONID_SDED_OBJS_LIST       = "s5-objs-list";      //$NON-NLS-1$
  String ICONID_SDED_CLASS_ATTR      = "s5-class-attr";     //$NON-NLS-1$
  String ICONID_SDED_CLASS_DATA      = "s5-class-data";     //$NON-NLS-1$
  String ICONID_SDED_CLASS_EVENT     = "s5-class-event";    //$NON-NLS-1$
  String ICONID_SDED_CLASS_CMD       = "s5-class-cmd";      //$NON-NLS-1$
  String ICONID_SDED_CLASS_LINK      = "s5-class-link";     //$NON-NLS-1$
  String ICONID_PAUSE_REFRESH        = "refresh-pause";     //$NON-NLS-1$
  String ICONID_RESUME_REFRESH       = "refresh-run";       //$NON-NLS-1$
  String ICONID_SUMMARY_COLUMN       = "col-summary";       //$NON-NLS-1$
  String ICONID_OBJ_DATA_EDIT        = "s5-obj-data-edit";  //$NON-NLS-1$
  String ICONID_OBJ_ATTR_EDIT        = "s5-obj-attr-edit";  //$NON-NLS-1$
  String ICONID_OBJ_CMD_EDIT         = "s5-obj-cmd-edit";   //$NON-NLS-1$
  String ICONID_OBJ_EVT_EDIT         = "s5-obj-event-edit"; //$NON-NLS-1$
  String ICONID_OBJ_LINK_EDIT        = "s5-obj-link-edit";  //$NON-NLS-1$
  String ICONID_GRADIENT             = "gradient";          //$NON-NLS-1$
  String ICONID_CHECK_ALL            = "check-all";         //$NON-NLS-1$
  String ICONID_UNCHECK_ALL          = "uncheck-all";       //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Actions
  /**
   * id действия "pause"
   */
  String UPDATE_PAUSE_ID = "update_pause_actId"; //$NON-NLS-1$

  /**
   * id действия "resume"
   */
  String UPDATE_RESUME_ID = "update_resume_actId"; //$NON-NLS-1$

  /**
   * id действия "summary"
   */
  String SUMMARY_COLUMN_ID = "summary_column_actId"; //$NON-NLS-1$

  TsActionDef ACDEF_UPDATE_PAUSE =
      TsActionDef.ofRadio2( UPDATE_PAUSE_ID, STR_N_UPDATE_PAUSE, STR_D_UPDATE_PAUSE, ICONID_PAUSE_REFRESH );

  TsActionDef ACDEF_UPDATE_RESUME =
      TsActionDef.ofRadio2( UPDATE_RESUME_ID, STR_N_UPDATE_RESUME, STR_D_UPDATE_RESUME, ICONID_RESUME_REFRESH );

  TsActionDef ACDEF_SUMMARY_COLUMN =
      TsActionDef.ofCheck2( SUMMARY_COLUMN_ID, STR_N_SUMMARY_COLUMN, STR_D_SUMMARY_COLUMN, ICONID_SUMMARY_COLUMN );

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IRtBrowserGuiConstants.class, PREFIX_OF_ICONID_FIELD_NAME );
  }

}
