package org.toxsoft.skf.devs.rtbrowser.gui.panels;

import org.toxsoft.skf.devs.rtbrowser.gui.panels.attrs.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.base.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.events.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.links.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas.*;

/**
 * Localizable resources.
 *
 * @author dima
 */
public interface ISkResources {

  /**
   * {@link RtBrowserPanel}
   */
  String STR_RT_DATA        = Messages.getString( "STR_RT_DATA" );        //$NON-NLS-1$
  String STR_ATTRS          = Messages.getString( "STR_ATTRS" );          //$NON-NLS-1$
  String STR_CMDS           = Messages.getString( "STR_CMDS" );           //$NON-NLS-1$
  String STR_EVENTS         = Messages.getString( "STR_EVENTS" );         //$NON-NLS-1$
  String STR_LINKS          = Messages.getString( "STR_LINKS" );          //$NON-NLS-1$
  String STR_FILTER         = Messages.getString( "STR_FILTER" );         //$NON-NLS-1$
  String STR_CHECK_ALL      = Messages.getString( "STR_CHECK_ALL" );      //$NON-NLS-1$
  String STR_UNCHECK_ALL    = Messages.getString( "STR_UNCHECK_ALL" );    //$NON-NLS-1$
  String STR_TARGET_CLASS   = Messages.getString( "STR_TARGET_CLASS" );   //$NON-NLS-1$
  String STR_SELECT_CLASS   = Messages.getString( "STR_SELECT_CLASS" );   //$NON-NLS-1$
  String STR_MAIN_EDITOR    = Messages.getString( "STR_MAIN_EDITOR" );    //$NON-NLS-1$
  String STR_SUB_EDITOR     = Messages.getString( "STR_SUB_EDITOR" );     //$NON-NLS-1$
  String DLG_C_SELECT_CLASS = Messages.getString( "DLG_C_SELECT_CLASS" ); //$NON-NLS-1$
  String DLG_T_SELECT_CLASS = Messages.getString( "DLG_T_SELECT_CLASS" ); //$NON-NLS-1$

  /**
   * {@link AttrsBrowser}
   */
  String STR_ATTR_NAME = Messages.getString( "STR_ATTR_NAME" ); //$NON-NLS-1$

  /**
   * {@link RtDataBrowser}
   */
  String STR_RTDATA_NAME = Messages.getString( "STR_RTDATA_NAME" ); //$NON-NLS-1$

  /**
   * {@link BaseBrowser}
   */
  String STR_SUMMARY = Messages.getString( "STR_SUMMARY" ); //$NON-NLS-1$
  String STR_GWID    = Messages.getString( "STR_GWID" );    //$NON-NLS-1$

  /**
   * {@link EventsBrowser}
   */
  String STR_EVENT_NAME = Messages.getString( "STR_EVENT_NAME" ); //$NON-NLS-1$

  /**
   * {@link CommandsBrowser}
   */
  String STR_CMD_NAME            = Messages.getString( "STR_CMD_NAME" );            //$NON-NLS-1$
  String STR_FMT_CMD_ARG_VAL_FMT = Messages.getString( "STR_FMT_CMD_ARG_VAL_FMT" ); //$NON-NLS-1$
  String STR_FMT_CMD_ID          = Messages.getString( "STR_FMT_CMD_ID" );          //$NON-NLS-1$
  String STR_RUN                 = Messages.getString( "STR_RUN" );                 //$NON-NLS-1$

  /**
   * {@link ObjectCmdValueEditingSupport}
   */
  String STR_CMD_CONFIRM_DLG_CAPTION = Messages.getString( "STR_CMD_CONFIRM_DLG_CAPTION" ); //$NON-NLS-1$
  String STR_CMD_CONFIRM_DLG_TITLE   = Messages.getString( "STR_CMD_CONFIRM_DLG_TITLE" );   //$NON-NLS-1$

  /**
   * {@link LinksBrowser}
   */
  String STR_LINK_NAME            = Messages.getString( "STR_LINK_NAME" );            //$NON-NLS-1$
  String STR_OPEN_SUBEDITOR       = Messages.getString( "STR_OPEN_SUBEDITOR" );       //$NON-NLS-1$
  String STR_ERR_TOO_MANY_CLASSES = Messages.getString( "STR_ERR_TOO_MANY_CLASSES" ); //$NON-NLS-1$
}
