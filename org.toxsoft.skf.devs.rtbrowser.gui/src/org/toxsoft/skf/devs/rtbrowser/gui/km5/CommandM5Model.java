package org.toxsoft.skf.devs.rtbrowser.gui.km5;

import static org.toxsoft.core.tsgui.bricks.gw.IGwM5Constants.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.gui.km5.ISkResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.models.av.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.reports.gui.panels.valed.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * M5-model of the {@link SkCommand}.
 *
 * @author dima
 */
public class CommandM5Model
    extends KM5ConnectedModelBase<SkCommand> {

  /**
   * ID of m5 model SkCommand.
   */
  public static String MID_SKCOMMAND_M5MODEL = "rtbrowser.mid.SkCommand"; //$NON-NLS-1$

  /**
   * The ID of the field {@link SkCommand#argValues()}.
   */
  public static final String FID_ARGS = "args"; //$NON-NLS-1$

  /**
   * Attribute {@link SkCommand#cmdGwid() } Command Green world ID
   */
  public M5AttributeFieldDef<SkCommand> EV_GWID = new M5AttributeFieldDef<>( FID_GWID, VALOBJ, //
      TSID_NAME, STR_N_CMD_GWID, //
      TSID_DESCRIPTION, STR_D_CMD_GWID, //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjGwidEditor.FACTORY_NAME //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_READ_ONLY );
    }

    protected IAtomicValue doGetFieldValue( SkCommand aEntity ) {
      return AvUtils.avValobj( aEntity.cmdGwid() );
    }

  };

  /**
   * Field {@link SkCommand#argValues()}.
   */
  public static final IM5MultiModownFieldDef<SkCommand, IdValue> EV_ARGS =
      new M5MultiModownFieldDef<>( FID_ARGS, IdValueM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_CMD_ARGS, STR_D_CMD_ARGS );
          setFlags( M5FF_DETAIL );
        }

        protected IList<IdValue> doGetFieldValue( SkCommand aEntity ) {
          return IdValue.makeIdValuesCollFromOptionSet( aEntity.argValues() ).values();
        }

      };

  /**
   * LM for this model.
   *
   * @author dima
   */
  static class LifecycleManager
      extends M5LifecycleManager<SkCommand, ISkConnection> {

    public LifecycleManager( IM5Model<SkCommand> aModel, ISkConnection aMaster ) {
      super( aModel, false, true, false, false, aMaster );
    }

    private static IDtoCommand makeCommand( IM5Bunch<SkCommand> aValues ) {
      IOptionSetEdit params = new OptionSet();
      IList<IdValue> idvals = aValues.getAs( FID_ARGS, IList.class );
      IdValue.fillOptionSetFromIdValuesColl( idvals, params );

      return new DtoCommand( aValues.originalEntity().timestamp(), aValues.originalEntity().instanceId(),
          aValues.originalEntity().cmdGwid(), aValues.originalEntity().authorSkid(), params );
    }

    @Override
    protected SkCommand doCreate( IM5Bunch<SkCommand> aValues ) {
      return null;
    }

    @Override
    protected SkCommand doEdit( IM5Bunch<SkCommand> aValues ) {
      DtoCommand dtoCmd = (DtoCommand)makeCommand( aValues );
      ISkCommand retVal =
          master().coreApi().cmdService().sendCommand( dtoCmd.cmdGwid(), dtoCmd.authorSkid(), dtoCmd.argValues() );
      return (SkCommand)retVal;
    }

    @Override
    protected void doRemove( SkCommand aEntity ) {
      // nop
    }

  }

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - Sk-connection to be used in lm
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public CommandM5Model( ISkConnection aConn ) {
    super( MID_SKCOMMAND_M5MODEL, SkCommand.class, aConn );
    setNameAndDescription( ESkClassPropKind.EVENT.nmName(), ESkClassPropKind.EVENT.description() );
    addFieldDefs( EV_GWID, EV_ARGS );
  }

  @Override
  protected IM5LifecycleManager<SkCommand> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this, skConn() );
  }

  @Override
  protected IM5LifecycleManager<SkCommand> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
