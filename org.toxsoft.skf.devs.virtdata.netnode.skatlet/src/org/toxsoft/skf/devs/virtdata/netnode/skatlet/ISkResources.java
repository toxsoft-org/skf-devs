package org.toxsoft.skf.devs.virtdata.netnode.skatlet;

/**
 * Локализуемые ресурсы.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
interface ISkResources {

  String STR_N_SKATLET = "Виртуальные данные проекта VAL (AnalogChannel)";
  String STR_D_SKATLET = "Формирование виртуальных данных проекта VAL (AnalogChannel)";

  String STR_CONNECTED   = "Ресурсы";
  String STR_CONNECTED_D = "Список идентификаторов проверяемых подключенных к системе ресурсов.";

  String STR_RTD_HEALTH_GWID   = "Состояние";
  String STR_RTD_HEALTH_GWID_D =
      "Идентификатор текущего данного представляющего интегральную оценку состояния подключенных ресурсов.";

  String ERR_NOT_FOUND = "%s is not found";

  String ERR_DOES_NOT_HAVE_RRI  = "The class %s does not have the rri-param %s (rriParamInfo = null).";
  String ERR_RRI_IS_NOT_ATTR    = "For class %s, rri-parameter %s is not an attribute (it's link).";
  String ERR_RRI_IS_NOT_BOOLEAN = "For class %s, rri-parameter %s must be of type boolean (not %s).";
}
