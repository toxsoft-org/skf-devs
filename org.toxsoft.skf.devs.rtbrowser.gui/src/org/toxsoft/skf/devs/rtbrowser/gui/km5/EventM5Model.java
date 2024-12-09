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
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * M5-model of the {@link SkEvent}.
 *
 * @author dima
 */
public class EventM5Model
    extends KM5ConnectedModelBase<SkEvent> {

  /**
   * ID of field right Skids of link.
   */
  public static String MID_SKEVENT_M5MODEL = "rtbrowser.mid.SkEvent"; //$NON-NLS-1$

  /**
   * The ID of the field {@link SkEvent#paramValues()}.
   */
  public static final String FID_PARAMS = "params"; //$NON-NLS-1$

  /**
   * Attribute {@link SkEvent#eventGwid() } Green world ID
   */
  public M5AttributeFieldDef<SkEvent> EV_GWID = new M5AttributeFieldDef<>( FID_GWID, VALOBJ, //
      TSID_NAME, STR_EV_GWID, //
      TSID_DESCRIPTION, STR_EV_GWID_D, //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjGwidEditor.FACTORY_NAME //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_READ_ONLY );
    }

    protected IAtomicValue doGetFieldValue( SkEvent aEntity ) {
      return AvUtils.avValobj( aEntity.eventGwid() );
    }

  };

  /**
   * Field {@link SkEvent#paramValues()}.
   */
  public final IM5MultiModownFieldDef<SkEvent, IdValue> EV_PARAMS =
      new M5MultiModownFieldDef<>( FID_PARAMS, IdValueM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_EVENT_PARAMS, STR_EVENT_PARAMS_D );
          setFlags( M5FF_DETAIL );
        }

        protected IList<IdValue> doGetFieldValue( SkEvent aEntity ) {
          return IdValue.makeIdValuesCollFromOptionSet( aEntity.paramValues() ).values();
        }

      };

  /**
   * LM for this model.
   *
   * @author dima
   */
  static class LifecycleManager
      extends M5LifecycleManager<SkEvent, ISkConnection> {

    public LifecycleManager( IM5Model<SkEvent> aModel, ISkConnection aMaster ) {
      super( aModel, false, true, false, false, aMaster );
    }

    private static SkEvent makeEvent( IM5Bunch<SkEvent> aValues ) {
      IOptionSetEdit params = new OptionSet();
      IList<IdValue> idvals = aValues.getAs( FID_PARAMS, IList.class );
      IdValue.fillOptionSetFromIdValuesColl( idvals, params );

      SkEvent event = new SkEvent( System.currentTimeMillis(), aValues.originalEntity().eventGwid(), params );

      return event;
    }

    @Override
    protected SkEvent doCreate( IM5Bunch<SkEvent> aValues ) {
      return makeEvent( aValues );
    }

    @Override
    protected SkEvent doEdit( IM5Bunch<SkEvent> aValues ) {
      SkEvent retVal = makeEvent( aValues );
      master().coreApi().eventService().fireEvent( retVal );
      return retVal;
    }

    @Override
    protected void doRemove( SkEvent aEntity ) {
      // nop
    }

  }

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - Sk-connection to be used in lm
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public EventM5Model( ISkConnection aConn ) {
    super( MID_SKEVENT_M5MODEL, SkEvent.class, aConn );
    setNameAndDescription( ESkClassPropKind.EVENT.nmName(), ESkClassPropKind.EVENT.description() );
    addFieldDefs( EV_GWID, EV_PARAMS );
  }

  @Override
  protected IM5LifecycleManager<SkEvent> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this, skConn() );
  }

  @Override
  protected IM5LifecycleManager<SkEvent> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
