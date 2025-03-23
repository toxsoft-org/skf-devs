package org.toxsoft.skf.devs.virtdata.netnode.skatlet;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.devs.virtdata.netnode.skatlet.ISkResources.*;
import static org.toxsoft.skf.devs.virtdata.netnode.skatlet.SkNetNodeSkatletConfig.*;
import static org.toxsoft.uskat.core.utils.SkHelperUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.threadexec.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.core.*;
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
        IAtomicValue avHealthOutputPublic = configs.findValue( NETNODE_HEALTH_OUTPUT_PUBLIC_PREFIX + index );
        IAtomicValue avHealthInputs = configs.findValue( NETNODE_HEALTH_INPUTS_PREFIX + index );
        IAtomicValue avHealthInputsEnabled = configs.findValue( NETNODE_HEALTH_INPUTS_ENABLED_PREFIX + index );

        if( avHealthOutput == null && avHealthOutputPublic == null && avHealthInputs == null
            && avHealthInputsEnabled == null ) {
          break;
        }
        if( avHealthOutput == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_OUTPUT_PREFIX + index );
        }
        if( avHealthOutputPublic == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_OUTPUT_PUBLIC_PREFIX + index );
        }
        if( avHealthInputs == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_INPUTS_PREFIX + index );
        }
        if( avHealthInputsEnabled == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_INPUTS_ENABLED_PREFIX + index );
        }
        Gwid healthOutput = avHealthOutput.asValobj();
        boolean healthPublic = avHealthOutputPublic.asBool();
        IGwidList healthInputs = expandGwids( coreApi, (IGwidList)avHealthInputs.asValobj(), EGwidKind.GW_RTDATA );
        String healthEnabled = avHealthInputsEnabled.asString();
        writers.add( new SkNetNodeRtdHealthWriter( coreApi, healthOutput, healthInputs, healthEnabled, healthPublic,
            logger() ) );
        logger().info( MSG_HEALTH_ITEM, Integer.valueOf( index ), healthOutput, gwidsToString( healthInputs ) );
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
        IGwidList onlineInputs = expandGwids( coreApi, (IGwidList)avOnlineInputs.asValobj(), EGwidKind.GW_RTDATA );
        writers.add( new SkNetNodeRtdOnlineWriter( coreApi, onlineOutput, onlineInputs ) );
        logger().info( MSG_ONLINE_ITEM, Integer.valueOf( index ), onlineOutput, gwidsToString( onlineInputs ) );
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
  private static String gwidsToString( IGwidList aGwids ) {
    TsNullArgumentRtException.checkNull( aGwids );
    StringBuilder sb = new StringBuilder();
    for( Gwid gwid : aGwids ) {
      sb.append( gwid );
      sb.append( '\n' );
    }
    return sb.toString();
  }

}
