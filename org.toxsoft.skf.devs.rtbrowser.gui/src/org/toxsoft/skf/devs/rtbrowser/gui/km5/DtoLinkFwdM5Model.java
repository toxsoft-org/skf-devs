package org.toxsoft.skf.devs.rtbrowser.gui.km5;

import static org.toxsoft.core.tsgui.bricks.gw.IGwM5Constants.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.gui.km5.ISkResources.*;
import static org.toxsoft.uskat.core.api.sysdescr.ESkClassPropKind.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.*;
import org.toxsoft.skf.reports.gui.panels.valed.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * M5-model of the {@link IDtoLinkFwd}.
 *
 * @author dima
 */
public class DtoLinkFwdM5Model
    extends KM5ConnectedModelBase<IDtoLinkFwd> {

  /**
   * ID of field right Skids of link.
   */
  public static String MID_LINK_FWD_M5MODEL = "rtbrowser.mid.DtoLinkFwd"; //$NON-NLS-1$

  /**
   * ID of field right Skids of link.
   */
  public String FID_RIGHT_SKIDS = "rightSkids"; //$NON-NLS-1$

  /**
   * link id
   */
  public static final String FID_LINK_ID = "linkId"; //$NON-NLS-1$

  /**
   * left Skid of fwd link
   */
  public static final String FID_LEFT_SKID = "leftSkid"; //$NON-NLS-1$

  /**
   * Attribute field {@link Gwid#classId()}
   */
  public final M5AttributeFieldDef<IDtoLinkFwd> CLASS_ID = new M5AttributeFieldDef<>( FID_CLASS_ID, DDEF_IDPATH, //
      TSID_NAME, STR_LINK_FWD_CLASS_ID, //
      TSID_DESCRIPTION, STR_LINK_FWD_CLASS_ID_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_DETAIL ), //
      TSID_DEFAULT_VALUE, avStr( Skid.NONE.classId() ) //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoLinkFwd aEntity ) {
      return avStr( aEntity.classId() );
    }

  };

  /**
   * Attribute {@link IDtoLinkFwd#gwid() } Green world ID
   */
  public M5AttributeFieldDef<IDtoLinkFwd> GWID = new M5AttributeFieldDef<>( FID_GWID, VALOBJ, //
      TSID_NAME, STR_N_FWD_LINK_GWID, //
      TSID_DESCRIPTION, STR_D_FWD_LINK_GWID, //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjGwidEditor.FACTORY_NAME //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_READ_ONLY );
    }

    protected IAtomicValue doGetFieldValue( IDtoLinkFwd aEntity ) {
      return avValobj( aEntity.gwid() );
    }

  };

  /**
   * Attribute {@link IDtoLinkFwd#gwid() } Green world ID
   */
  public M5AttributeFieldDef<IDtoLinkFwd> LEFT_SKID = new M5AttributeFieldDef<>( FID_LEFT_SKID, VALOBJ, //
      TSID_NAME, STR_N_FWD_LINK_LEFT_SKID, //
      TSID_DESCRIPTION, STR_D_FWD_LINK_LEFT_SKID, //
      TSID_KEEPER_ID, Skid.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSkidEditor.FACTORY_NAME //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_READ_ONLY );
    }

    protected IAtomicValue doGetFieldValue( IDtoLinkFwd aEntity ) {
      return avValobj( aEntity.leftSkid() );
    }

  };

  /**
   * Attribute {@link IDtoLinkFwd#linkId() } id of link
   */
  public M5AttributeFieldDef<IDtoLinkFwd> LINK_ID = new M5AttributeFieldDef<>( FID_LINK_ID, EAtomicType.STRING, //
      TSID_NAME, STR_N_FWD_LINK_LINK_ID, //
      TSID_DESCRIPTION, STR_D_FWD_LINK_LINK_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_READ_ONLY );
    }

    protected IAtomicValue doGetFieldValue( IDtoLinkFwd aEntity ) {
      return avStr( aEntity.linkId() );
    }

  };

  /**
   * Attribute {@link IDtoLinkFwd#rightSkids() } Green world ID
   */
  public M5AttributeFieldDef<IDtoLinkFwd> RIGHT_SKIDS = new M5AttributeFieldDef<>( FID_RIGHT_SKIDS, VALOBJ, //
      TSID_NAME, STR_N_RIGHT_SKIDS, //
      TSID_DESCRIPTION, STR_D_RIGHT_SKIDS, //
      TSID_KEEPER_ID, SkidListKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSkidListEditor.FACTORY_NAME //
  ) {

    protected IAtomicValue doGetFieldValue( IDtoLinkFwd aEntity ) {
      return AvUtils.avValobj( aEntity.rightSkids() );
    }

  };

  /**
   * LM for this model.
   *
   * @author dima
   */
  class LifecycleManager
      extends M5LifecycleManager<IDtoLinkFwd, ISkConnection> {

    public LifecycleManager( IM5Model<IDtoLinkFwd> aModel, ISkConnection aMaster ) {
      super( aModel, false, true, false, false, aMaster );
    }

    private IDtoLinkFwd makeDtoLinkFwd( IM5Bunch<IDtoLinkFwd> aValues ) {

      IList<Skid> rightSkids = aValues.getAsAv( FID_RIGHT_SKIDS ).asValobj();

      DtoLinkFwd inf = DtoLinkFwd.createDirect( aValues.originalEntity().gwid(), aValues.originalEntity().leftSkid(),
          new SkidList( rightSkids ) );
      return inf;
    }

    @Override
    protected IDtoLinkFwd doCreate( IM5Bunch<IDtoLinkFwd> aValues ) {
      return makeDtoLinkFwd( aValues );
    }

    @Override
    protected IDtoLinkFwd doEdit( IM5Bunch<IDtoLinkFwd> aValues ) {
      IDtoLinkFwd retVal = makeDtoLinkFwd( aValues );
      master().coreApi().linkService().setLink( retVal );
      return retVal;
    }

    @Override
    protected void doRemove( IDtoLinkFwd aEntity ) {
      // nop
    }

  }

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - Sk-connection to be used in constructor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public DtoLinkFwdM5Model( ISkConnection aConn ) {
    super( MID_LINK_FWD_M5MODEL, IDtoLinkFwd.class, aConn );
    setNameAndDescription( LINK.nmName(), LINK.description() );
    addFieldDefs( CLASS_ID, LEFT_SKID, LINK_ID, GWID, RIGHT_SKIDS );
  }

  @Override
  protected IM5LifecycleManager<IDtoLinkFwd> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this, skConn() );
  }

  @Override
  protected IM5LifecycleManager<IDtoLinkFwd> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
