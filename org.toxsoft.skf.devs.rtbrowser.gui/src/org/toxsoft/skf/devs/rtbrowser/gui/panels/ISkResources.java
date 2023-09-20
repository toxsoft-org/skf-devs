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
  String STR_RT_DATA        = "Rt данные";
  String STR_ATTRS          = "Атрибуты";
  String STR_CMDS           = "Команды";
  String STR_EVENTS         = "События";
  String STR_LINKS          = "Связи";
  String STR_FILTER         = "Фильтр: ";
  String STR_CHECK_ALL      = "Отметить всё";
  String STR_UNCHECK_ALL    = "Снять все отметки";
  String STR_TARGET_CLASS   = "Целевой класс: ";
  String STR_SELECT_CLASS   = "Выбрать класс...";
  String STR_MAIN_EDITOR    = "main editor";
  String STR_SUB_EDITOR     = "sub editor";
  String DLG_C_SELECT_CLASS = "Выбор класса";
  String DLG_T_SELECT_CLASS = "Выберите класс и нажмите Ok";

  /**
   * {@link AttrsBrowser}
   */
  String STR_ATTR_NAME = "Attribute name";

  /**
   * {@link RtDataBrowser}
   */
  String STR_RTDATA_NAME = "RtData name";

  /**
   * {@link BaseBrowser}
   */
  String STR_SUMMARY = "Summary";
  String STR_GWID    = "Gwid";

  /**
   * {@link EventsBrowser}
   */
  String STR_EVENT_NAME = "Event name";

  /**
   * {@link CommandsBrowser}
   */
  String STR_CMD_NAME            = "Command name";
  String STR_FMT_CMD_ARG_VAL_FMT = "c аргументом %s=%s";
  String STR_FMT_CMD_ID          = "Команда %s ";
  String STR_RUN                 = "\n Выполняем?";

  /**
   * {@link ObjectCmdValueEditingSupport}
   */
  String STR_CMD_CONFIRM_DLG_CAPTION = "Подтверждение посыла команды";
  String STR_CMD_CONFIRM_DLG_TITLE   = "Проверьте аргумены команды и нажмите Ok";

  /**
   * {@link LinksBrowser}
   */
  String STR_LINK_NAME            = "Link name";
  String STR_OPEN_SUBEDITOR       = "Open sub editor";
  String STR_ERR_TOO_MANY_CLASSES = "Can't open subeditor, there are %d classes in selection.\n Pick one.";
}
