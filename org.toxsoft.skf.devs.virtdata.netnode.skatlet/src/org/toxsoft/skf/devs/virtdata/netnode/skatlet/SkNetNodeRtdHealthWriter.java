package org.toxsoft.skf.devs.virtdata.netnode.skatlet;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.devs.virtdata.netnode.skatlet.ISkResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.skf.dq.virtdata.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.virtdata.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.virtdata.*;

/**
 * Писатель виртуального данного: {@link ISkNetNode#RTDID_HEALTH} или его аналога.
 *
 * @author mvk
 */
class SkNetNodeRtdHealthWriter
    extends SkAbstractVirtDataCurrDataWriter {

  private static final String rriSectionId = "rri.section.id"; //$NON-NLS-1$

  private final SkVirtDataDataQualityReader dataQuality;
  private final SkVirtDataCurrDataReader    currdata;
  private final SkVirtDataRriReader         rri;

  private IMap<Skid, Pair<Gwid, GwidList>> inputsByObjs;

  /**
   * Конструктор.
   *
   * @param aCoreApi {@link ISkCoreApi} API соединения.
   * @param aOutput {@link Gwid} конкретный ({@link Gwid#isAbstract()}=false) идентификатор ресурса представляющего
   *          параметр "Интегральная оценка состояния подключенных узлов" {@link ISkNetNode#RTDID_HEALTH} (или его
   *          аналога в другом классе) у которого устанавливается значение параметра {@link ISkNetNode#RTDID_HEALTH}.
   * @param aInputs {@link IGwidList} список параметров конкретных ({@link Gwid#isAbstract()}=false) идентификаторов
   *          подключенных к сетевому узлу ресурсов.
   * @param aRriEnabled String имя rri-параметра используемого для отключения входов на runtime. Пустая строка - не
   *          используется.
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException неодинаковый размер списков идентикаторы параметра состояния не могут быть
   *           абстрактным {@link Gwid}.
   * @throws TsIllegalArgumentRtException неодинаковый размер списков состояния и весов.
   * @throws TsIllegalArgumentRtException в системе не найден параметр подключенного ресурса.
   */
  SkNetNodeRtdHealthWriter( ISkCoreApi aCoreApi, Gwid aOutput, IGwidList aInputs, String aRriEnabled,
      ILogger aLogger ) {
    super( aCoreApi, aOutput );
    TsNullArgumentRtException.checkNull( aInputs );
    dataQuality = new SkVirtDataDataQualityReader( aCoreApi, aInputs, this );
    currdata = new SkVirtDataCurrDataReader( aCoreApi, Skid.NONE, IStringList.EMPTY, this );
    currdata.addReadData( aInputs );
    rri = new SkVirtDataRriReader( aCoreApi, Skid.NONE, rriSectionId, IStringList.EMPTY, this );
    inputsByObjs = new ElemMap<>();
    GwidList tmpRriIds = new GwidList();
    IMapEdit<Skid, Pair<Gwid, GwidList>> inputsByObjsEdit = (IMapEdit<Skid, Pair<Gwid, GwidList>>)inputsByObjs;
    for( Gwid g : aInputs ) {
      Skid objId = g.skid();
      Pair<Gwid, GwidList> objInputs = inputsByObjsEdit.findByKey( objId );
      if( objInputs == null ) {
        Gwid rriAttr = getRriGwid( aCoreApi, objId, aRriEnabled, aLogger );
        objInputs = new Pair<>( rriAttr, new GwidList() );
        inputsByObjsEdit.put( objId, objInputs );
        if( !tmpRriIds.hasElem( rriAttr ) ) {
          tmpRriIds.add( rriAttr );
        }
      }
      objInputs.right().add( g );
    }
    rri.addRriIds( tmpRriIds );
    // Write current value
    onGenericChangeEvent( this );
  }

  // ------------------------------------------------------------------------------------
  // SkAbstractVirtDataCurrDataWriter
  //
  @Override
  protected IAtomicValue doCalculateValue() {
    IMap<Gwid, IOptionSet> marks = dataQuality.getResourcesMarks();
    // Общее количество подключенных ресурсов
    int totalCount = 0;
    // Количество подключенных ресурсов
    int connectedCount = 0;
    for( Skid objId : inputsByObjs.keys() ) {
      Pair<Gwid, GwidList> objInput = inputsByObjs.getByKey( objId );
      Gwid rriEnabled = objInput.left();
      if( rriEnabled != Gwid.NONE_CONCR_ATTR && !rri.getBool( rriEnabled ) ) {
        // Объект отключен
        continue;
      }
      IGwidList gwids = objInput.right();
      for( Gwid g : gwids ) {
        IAtomicValue health = currdata.get( g );
        if( !health.isAssigned() ) {
          continue;
        }
        IAtomicValue notConnected = marks.findByKey( g ).findByKey( ISkDataQualityService.TICKET_ID_NO_CONNECTION );
        if( notConnected.asBool() ) {
          continue;
        }
        connectedCount++;
      }
      totalCount += gwids.size();
    }
    if( totalCount == 0 ) {
      // Нет подключенных узлов
      return avInt( 100 );
    }
    return avInt( (100 * connectedCount) / totalCount );
  }

  @Override
  protected void doClose() {
    dataQuality.close();
    currdata.close();
    rri.close();
  }

  // ------------------------------------------------------------------------------------
  // private methods
  //
  private static Gwid getRriGwid( ISkCoreApi aCorApi, Skid aObjId, String aRriEnabled, ILogger aLogger ) {
    if( aRriEnabled.length() > 0 ) {
      String classId = aObjId.classId();
      ISkRegRefInfoService rriService = aCorApi.getService( ISkRegRefInfoService.SERVICE_ID );
      ISkRriSection rriSection = rriService.getSection( rriSectionId );
      IDtoRriParamInfo rriParamInfo = rriSection.listParamInfoes( classId ).findByKey( aRriEnabled );
      if( rriParamInfo == null ) {
        // Для класса не определен RRI
        aLogger.warning( ERR_DOES_NOT_HAVE_RRI, classId, aRriEnabled );
        return Gwid.NONE_CONCR_ATTR;
      }
      if( rriParamInfo.isLink() ) {
        // RRI не является атрибутом
        aLogger.warning( ERR_RRI_IS_NOT_ATTR, classId, aRriEnabled );
        return Gwid.NONE_CONCR_ATTR;
      }
      EAtomicType type = rriParamInfo.attrInfo().dataType().atomicType();
      if( type != EAtomicType.BOOLEAN ) {
        // RRI должен представлять boolean-тип
        aLogger.warning( ERR_RRI_IS_NOT_BOOLEAN, classId, aRriEnabled, type );
        return Gwid.NONE_CONCR_ATTR;
      }
    }
    Gwid retValue = (aRriEnabled.length() > 0 ? Gwid.createAttr( aObjId, aRriEnabled ) : Gwid.NONE_CONCR_ATTR);
    return retValue;
  }
}
