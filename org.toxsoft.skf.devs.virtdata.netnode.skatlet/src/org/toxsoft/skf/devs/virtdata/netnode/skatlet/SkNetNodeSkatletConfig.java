package org.toxsoft.skf.devs.virtdata.netnode.skatlet;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.s5.server.backend.supports.skatlets.*;

/**
 * Параметры конфигурации подсистемы
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
public final class SkNetNodeSkatletConfig {

  /**
   * Префикс идентфикаторов подсистемы
   * <p>
   * Хранение значений параметров конфигурации возможно во внешнем хранилище подсистемы скатлетов поэтому префикс имеет
   * предопределенное значение.
   */
  public static final String SYBSYSTEM_ID_PREFIX = S5BackendSkatletsConfig.SYBSYSTEM_ID_PREFIX + ".virtdata.netnode";

  /**
   * Префикс имени параметра конкретного ({@link Gwid#isAbstract()}=false) идентификатора ресурса представляющего
   * параметр "Интегральная оценка состояния подключенных узлов" {@link ISkNetNode#RTDID_HEALTH} (или его аналога в
   * другом классе) у которого устанавливается значение параметра {@link ISkNetNode#RTDID_HEALTH}.
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link Gwid}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.output0=@Gwid[sk.Server[valcom.server.main]$rtdata(health)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.inputs0=@GwidList[sk.Server[valcom.server.local1]$rtdata(health),sk.Server[valcom.server.local2]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.inputs0=@Gwid[val.Registrator[owen_VK1]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output0=@Gwid[val.Registrator[owen_VK1]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.inputs1=@Gwid[val.Registrator[owen_VK2]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output1=@Gwid[val.Registrator[owen_VK2]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.inputs2=@Gwid[val.Registrator[owen_VK3]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output2=@Gwid[val.Registrator[owen_VK3]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.inputs3=@Gwid[val.Registrator[owen_VK4]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output3=@Gwid[val.Registrator[owen_VK4]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.inputs4=@Gwid[val.Registrator[owen_VK5]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output4=@Gwid[val.Registrator[owen_VK5]$rtdata(online)]
   *
   * </code>
   */
  public static final String NETNODE_HEALTH_OUTPUT_PREFIX = SYBSYSTEM_ID_PREFIX + ".health.output";

  /**
   * Префикс имени параметра-требования публикации в службе качества данных ресурса представляющего параметр
   * "Интегральная оценка состояния подключенных узлов" {@link ISkNetNode#RTDID_HEALTH} (или его аналога в другом
   * классе) у которого устанавливается значение параметра {@link ISkNetNode#RTDID_HEALTH}.
   * <p>
   * Тип: {@link EAtomicType#BOOLEAN}.
   */
  public static final String NETNODE_HEALTH_OUTPUT_PUBLIC_PREFIX = SYBSYSTEM_ID_PREFIX + ".health.output.public";

  /**
   * Префикс имен списка параметров идентификаторов любых подключенных к сетевому узлу ресурсов с помощью которого
   * формируется значение параметра {@link ISkNetNode#RTDID_HEALTH}.
   * <p>
   * Префикс имен списка параметров конкретных ({@link Gwid#isAbstract()}=false) идентификаторов подключенных к сетевому
   * узлу ресурсов и представляющих параметр "Интегральная оценка состояния подключенных узлов"
   * {@link ISkNetNode#RTDID_HEALTH} (или его аналога в другом классе) с помощью которого формируется значение параметра
   * {@link ISkNetNode#RTDID_HEALTH}.
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link IGwidList}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.output0=@Gwid[sk.Server[valcom.server.main]$rtdata(health)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.inputs0=@GwidList[sk.Server[valcom.server.local1]$rtdata(health),sk.Server[valcom.server.local2]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input0=@Gwid[sk.Server[valcom.server.local1]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input1=@Gwid[val.Registrator[owen_VK1]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input2=@Gwid[val.Registrator[owen_VK2]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input3=@Gwid[val.Registrator[owen_VK3]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input4=@Gwid[val.Registrator[owen_VK4]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input5=@Gwid[val.Registrator[owen_VK5]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output1=@Gwid[val.Registrator[owen_VK1]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output2=@Gwid[val.Registrator[owen_VK2]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output3=@Gwid[val.Registrator[owen_VK3]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output4=@Gwid[val.Registrator[owen_VK4]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output5=@Gwid[val.Registrator[owen_VK5]$rtdata(online)]
   * </code>
   */
  public static final String NETNODE_HEALTH_INPUTS_PREFIX = SYBSYSTEM_ID_PREFIX + ".health.inputs";

  /**
   * Имя параметра НСИ объектов {@link #NETNODE_HEALTH_INPUTS_PREFIX} используемого для отключения этих объектов из
   * расчета (если он существует для данного объекта).
   */
  public static final String NETNODE_HEALTH_INPUTS_ENABLED_PREFIX = SYBSYSTEM_ID_PREFIX + ".health.inputs.enabled";

  /**
   * Префикс имени параметра конкретного ({@link Gwid#isAbstract()}=false) идентификатора ресурса представляющего
   * параметр "Включен" {@link ISkNetNode#RTDID_ONLINE} (или его аналога в другом классе) у которого устанавливается
   * значение параметра {@link ISkNetNode#RTDID_ONLINE}.
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link Gwid}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.output0=@Gwid[sk.Server[valcom.server.main]$rtdata(health)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.inputs0=@GwidList[sk.Server[valcom.server.local1]$rtdata(health),sk.Server[valcom.server.local2]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input0=@Gwid[sk.Server[valcom.server.local1]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input1=@Gwid[val.Registrator[owen_VK1]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input2=@Gwid[val.Registrator[owen_VK2]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input3=@Gwid[val.Registrator[owen_VK3]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input4=@Gwid[val.Registrator[owen_VK4]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input5=@Gwid[val.Registrator[owen_VK5]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output1=@Gwid[val.Registrator[owen_VK1]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output2=@Gwid[val.Registrator[owen_VK2]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output3=@Gwid[val.Registrator[owen_VK3]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output4=@Gwid[val.Registrator[owen_VK4]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output5=@Gwid[val.Registrator[owen_VK5]$rtdata(online)]
   * </code>
   */
  public static final String NETNODE_ONLINE_OUTPUT_PREFIX = SYBSYSTEM_ID_PREFIX + ".online.output";

  /**
   * Префикс имен списка параметров идентификаторов любых подключенных к сетевому узлу ресурсов с помощью которого
   * формируется значение параметра {@link ISkNetNode#RTDID_ONLINE}.
   * <p>
   * Если идентификтор в списке представляет класс объекта ({@link EGwidKind#GW_CLASS}, {@link Gwid#isAbstract()}=true),
   * то ЛЮБЫЕ {@link EGwidKind#GW_RTDATA}-данные класса ЛЮБЫХ объектов могут быть источником для формирования значения
   * параметра {@link ISkNetNode#RTDID_ONLINE}.
   * <p>
   * Если идентификтор в списке представляет конкретный объект ({@link EGwidKind#GW_CLASS},
   * {@link Gwid#isAbstract()}=false), то ЛЮБЫЕ {@link EGwidKind#GW_RTDATA}-данные его класса могут быть источником для
   * формирования значения параметра {@link ISkNetNode#RTDID_ONLINE}.
   * <p>
   * Если идентификтор в списке представляет {@link EGwidKind#GW_RTDATA}-данное абстрактного объекта
   * ({@link Gwid#isAbstract()}=true), то значит это {@link EGwidKind#GW_RTDATA}-данное ЛЮБОГО объекта может быть
   * источником для формирования значения параметра {@link ISkNetNode#RTDID_ONLINE}.
   * <p>
   * Если идентификтор в списке представляет {@link EGwidKind#GW_RTDATA}-данное конкретного объекта
   * ({@link Gwid#isAbstract()}=false), то значит это {@link EGwidKind#GW_RTDATA}-данное ЭТОГО объекта может быть
   * источником для формирования значения параметра {@link ISkNetNode#RTDID_ONLINE}.
   * <p>
   * Если идентификтор в списке представляет абстрактную связь объекта ({@link EGwidKind#GW_LINK},
   * {@link Gwid#isAbstract()}=true), то ЛЮБЫЕ {@link EGwidKind#GW_RTDATA}-данные объектов на которые указывают связи
   * объектов могут быть источником для формирования значения параметра {@link ISkNetNode#RTDID_ONLINE}.
   * <p>
   * Если идентификтор в списке представляет конкретную связь объекта ({@link EGwidKind#GW_LINK},
   * {@link Gwid#isAbstract()}=false), то ЛЮБЫЕ {@link EGwidKind#GW_RTDATA}-данные объектов на который указывает связь
   * объекта могут быть источником для формирования значения параметра {@link ISkNetNode#RTDID_ONLINE}.
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link IGwidList}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.output0=@Gwid[sk.Server[valcom.server.main]$rtdata(health)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.inputs0=@GwidList[sk.Server[valcom.server.local1]$rtdata(health),sk.Server[valcom.server.local2]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output0=@Gwid[sk.Server[valcom.server.local1]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input0=@Gwid[sk.Server[valcom.server.local1]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input1=@Gwid[val.Registrator[owen_VK1]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input2=@Gwid[val.Registrator[owen_VK2]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input3=@Gwid[val.Registrator[owen_VK3]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input4=@Gwid[val.Registrator[owen_VK4]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input5=@Gwid[val.Registrator[owen_VK5]$link(lnkAnalogChannels)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output1=@Gwid[val.Registrator[owen_VK1]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output2=@Gwid[val.Registrator[owen_VK2]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output3=@Gwid[val.Registrator[owen_VK3]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output4=@Gwid[val.Registrator[owen_VK4]$rtdata(online)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output5=@Gwid[val.Registrator[owen_VK5]$rtdata(online)]
   * </code>
   */
  public static final String NETNODE_ONLINE_INPUTS_PREFIX = SYBSYSTEM_ID_PREFIX + ".online.inputs";
}
