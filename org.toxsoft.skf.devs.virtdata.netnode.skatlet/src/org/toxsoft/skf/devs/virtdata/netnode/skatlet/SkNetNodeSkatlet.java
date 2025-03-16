package org.toxsoft.skf.devs.virtdata.netnode.skatlet;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.devs.virtdata.netnode.skatlet.ISkResources.*;
import static org.toxsoft.skf.devs.virtdata.netnode.skatlet.SkNetNodeSkatletConfig.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.threadexec.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.gwids.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.virtdata.*;

/**
 * Virtual Data Skatlet Writer for {@link ISkNetNode}.
 *
 * @author mvk
 */
public class SkNetNodeSkatlet
    extends SkatletBase {

  private final IListEdit<SkAbstractVirtDataCurrDataWriter> writers = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public SkNetNodeSkatlet() {
    super( SkNetNodeSkatlet.class.getSimpleName(), OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_SKATLET, //
        TSID_DESCRIPTION, STR_D_SKATLET //
    ) );
  }

  // ------------------------------------------------------------------------------------
  // SkatletBase
  //
  @Override
  public void start() {
    super.start();
    IOptionSet configs = environ().params();
    ISkCoreApi coreApi = getSharedConnection().coreApi();
    ITsThreadExecutor threadExecutor = SkThreadExecutorService.getExecutor( coreApi );
    threadExecutor.syncExec( () -> {
      // health
      int i = 0;
      while( true ) {
        int index = i++;
        IAtomicValue avHealthOutput = configs.findValue( NETNODE_HEALTH_OUTPUT_PREFIX + index );
        IAtomicValue avHealthInputs = configs.findValue( NETNODE_HEALTH_INPUTS_PREFIX + index );
        IAtomicValue avHealthInputsEnabled = configs.findValue( NETNODE_HEALTH_INPUTS_ENABLED_PREFIX + index );

        if( avHealthOutput == null && avHealthInputs == null && avHealthInputsEnabled == null ) {
          break;
        }
        if( avHealthOutput == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_OUTPUT_PREFIX + index );
        }
        if( avHealthInputs == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_INPUTS_PREFIX + index );
        }
        if( avHealthInputsEnabled == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_INPUTS_ENABLED_PREFIX + index );
        }
        Gwid healthOutput = avHealthOutput.asValobj();
        IGwidList healthInputs = getConcreteInputs( coreApi, (IGwidList)avHealthInputs.asValobj() );
        String healthEnabled = avHealthInputs.asValobj();
        writers.add( new SkNetNodeRtdHealthWriter( coreApi, healthOutput, healthInputs, healthEnabled, logger() ) );
      }
      // online
      i = 0;
      while( true ) {
        int index = i++;
        IAtomicValue avOnlineOutput = configs.findValue( NETNODE_ONLINE_OUTPUT_PREFIX + index );
        IAtomicValue avOnlineInputs = configs.findValue( NETNODE_ONLINE_INPUTS_PREFIX + index );
        if( avOnlineOutput == null && avOnlineInputs == null ) {
          break;
        }
        if( avOnlineOutput == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_ONLINE_OUTPUT_PREFIX + index );
        }
        if( avOnlineInputs == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_ONLINE_INPUTS_PREFIX + index );
        }
        Gwid onlineOutput = avOnlineOutput.asValobj();
        IGwidList onlineInputs = getConcreteInputs( coreApi, (IGwidList)avOnlineInputs.asValobj() );
        writers.add( new SkNetNodeRtdOnlineWriter( coreApi, onlineOutput, onlineInputs ) );
      }
      // Register dataquality list
      GwidList writeDataIds = new GwidList();
      for( SkAbstractVirtDataCurrDataWriter writer : writers ) {
        writeDataIds.add( writer.writeDataId() );
      }
      if( writeDataIds.size() > 0 ) {
        ISkDataQualityService dataQualityService = coreApi.getService( ISkDataQualityService.SERVICE_ID );
        dataQualityService.addConnectedResources( writeDataIds );
      }
    } );
    logger().info( "%s: start(). writers count = %d", id(), Integer.valueOf( writers.size() ) ); //$NON-NLS-1$
  }

  @Override
  public boolean queryStop() {
    super.queryStop();
    ISkCoreApi coreApi = getSharedConnection().coreApi();
    ITsThreadExecutor threadExecutor = SkThreadExecutorService.getExecutor( coreApi );
    threadExecutor.syncExec( () -> {
      // Deregister dataquality list
      GwidList writeDataIds = new GwidList();
      for( SkAbstractVirtDataCurrDataWriter writer : writers ) {
        writeDataIds.add( writer.writeDataId() );
        writer.close();
      }
      if( writeDataIds.size() > 0 ) {
        ISkDataQualityService dataQualityService = coreApi.getService( ISkDataQualityService.SERVICE_ID );
        dataQualityService.removeConnectedResources( writeDataIds );
      }
    } );
    logger().info( "%s: queryStop(). virtual data writers (%d) are closed", id(), Integer.valueOf( writers.size() ) ); //$NON-NLS-1$

    return true;
  }

  // ------------------------------------------------------------------------------------
  // private methods
  //
  private static IGwidList getConcreteInputs( ISkCoreApi aCoreApi, IGwidList aInputs ) {
    TsNullArgumentRtException.checkNulls( aCoreApi, aInputs );
    ISkSysdescr sysdescr = aCoreApi.sysdescr();
    ISkLinkService linkService = aCoreApi.linkService();
    ISkGwidService gwidService = aCoreApi.gwidService();
    GwidList retValue = new GwidList();
    for( Gwid g : aInputs ) {
      switch( g.kind() ) {
        case GW_CLASS: {
          retValue.addAll( getObjRtdataGwids( sysdescr, gwidService.expandGwid( g ).objIds() ) );
          break;
        }
        case GW_LINK: {
          for( Gwid link : gwidService.expandGwid( g ) ) {
            retValue.addAll( getObjRtdataGwids( sysdescr, linkService.getLinkFwd( link ).rightSkids() ) );
          }
          break;
        }
        case GW_RTDATA:
          retValue.addAll( gwidService.expandGwid( g ) );
          break;
        case GW_ATTR:
        case GW_RIVET:
        case GW_CLOB:
        case GW_CMD:
        case GW_CMD_ARG:
        case GW_EVENT:
        case GW_EVENT_PARAM:
          throw new TsIllegalArgumentRtException();
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
    return retValue;
  }

  private static IGwidList getObjRtdataGwids( ISkSysdescr aSysdescr, ISkidList aObjIds ) {
    GwidList retValue = new GwidList();
    for( Skid objId : aObjIds ) {
      IStridablesList<IDtoRtdataInfo> rtDataInfos = aSysdescr.getClassInfo( objId.classId() ).rtdata().list();
      for( String rtDataId : rtDataInfos.keys() ) {
        retValue.add( Gwid.createRtdata( objId, rtDataId ) );
      }
    }
    return retValue;
  }
}
