package org.toxsoft.skf.devs.virtdata.netnode.skatlet;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.bridge.s5.lib.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.skf.dq.virtdata.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.virtdata.*;

/**
 * Писатель виртуального данного: {@link ISkNetNode#RTDID_ONLINE} или его аналога.
 *
 * @author mvk
 */
class SkNetNodeRtdOnlineWriter
    extends SkAbstractVirtDataCurrDataWriter {

  private final SkVirtDataDataQualityReader dataQuality;

  /**
   * Признак передачи данных через шлюз сервера
   */
  private boolean trasmittedMark = false;

  /**
   * Конструктор.
   *
   * @param aCoreApi {@link ISkCoreApi} API соединения.
   * @param aOnlineOutput {@link Gwid} конкретный ({@link Gwid#isAbstract()}=false) идентификатор ресурса
   *          представляющего параметр "Интегральная оценка состояния подключенных узлов"
   *          {@link ISkNetNode#RTDID_HEALTH} (или его аналога в другом классе) у которого устанавливается значение
   *          параметра {@link ISkNetNode#RTDID_HEALTH}.
   * @param aOnlineInputs {@link IGwidList} список параметров конкретных ({@link Gwid#isAbstract()}=false)
   *          идентификаторов подключенных к сетевому узлу ресурсов и представляющих параметр "Интегральная оценка
   *          состояния подключенных узлов" {@link ISkNetNode#RTDID_HEALTH} (или его аналога в другом классе) с помощью
   *          которого формируется значение параметра {@link ISkNetNode#RTDID_HEALTH}.
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  SkNetNodeRtdOnlineWriter( ISkCoreApi aCoreApi, Gwid aOnlineOutput, IGwidList aOnlineInputs ) {
    super( aCoreApi, aOnlineOutput );
    dataQuality = new SkVirtDataDataQualityReader( aCoreApi, aOnlineInputs, this );
    // Write current value
    onGenericChangeEvent( this );
  }

  // ------------------------------------------------------------------------------------
  // SkAbstractVirtDataCurrDataWriter
  //
  @Override
  protected IAtomicValue doCalculateValue() {
    if( dataQuality.resourceIds().size() == 0 ) {
      // Нет подключенных ресурсов
      return avValobj( EConnState.ONLINE );
    }
    IMap<Gwid, IOptionSet> allMarks = dataQuality.getResourcesMarks();
    for( Gwid gwid : dataQuality.resourceIds() ) {
      IOptionSet marks = allMarks.findByKey( gwid );
      if( dataQuality.resourceIds().size() == 1 && dataQuality.resourceIds().first().equals( writeDataId() ) ) {
        // Вход совпадает с выходом, проверка того, что значения передаются через шлюз для формирования UNKNOWN
        IStringList route = marks.getValobj( ISkGatewayHardConstants.TICKET_ROUTE, IStringList.EMPTY );
        if( route.size() > 0 ) {
          // Установка признака передачи данного через шлюз
          trasmittedMark = true;
        }
      }
      // Проверка состояния соединения
      IAtomicValue notConnected = marks.findByKey( ISkDataQualityService.TICKET_ID_NO_CONNECTION );
      if( notConnected == null || !notConnected.asBool() ) {
        return avValobj( EConnState.ONLINE );
      }
    }
    return avValobj( trasmittedMark ? EConnState.UNKNOWN : EConnState.OFFLINE );
  }

  @Override
  protected void doHandleValueChanged( IAtomicValue aPrevValue, IAtomicValue aNewValue ) {
    ISkDataQualityService dataQualityService = coreApi().getService( ISkDataQualityService.SERVICE_ID );
    IGwidList gwids = new GwidList( writeDataId() );
    EConnState newState = aNewValue.asValobj();
    if( !trasmittedMark && newState == EConnState.ONLINE ) {
      // Установка качества данных connected (значение данного формируется на локальном узле)
      dataQualityService.addConnectedResources( gwids );
      return;
    }
    if( !trasmittedMark && newState == EConnState.OFFLINE ) {
      // Установка качества данных connected (значение данного формируется на локальном узле)
      dataQualityService.removeConnectedResources( gwids );
      return;
    }
    if( trasmittedMark ) {
      // Снятие качества данных connected (значение данного формируется на ДРУГОМ локальном узле)
      dataQualityService.removeConnectedResources( gwids );
    }
  }

  @Override
  public void doClose() {
    dataQuality.close();
  }

  // ------------------------------------------------------------------------------------
  // private methods
  //

}
