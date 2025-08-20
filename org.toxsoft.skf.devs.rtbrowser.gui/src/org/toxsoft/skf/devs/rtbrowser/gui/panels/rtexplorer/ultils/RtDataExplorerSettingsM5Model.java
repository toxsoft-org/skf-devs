package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils.ISkResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * M5-model of the {@link IRtDataExplorerSettings}.
 *
 * @author dima
 */
public class RtDataExplorerSettingsM5Model
    extends KM5ConnectedModelBase<IRtDataExplorerSettings> {

  /**
   * ID of m5 model IRtDataExplorerSettings.
   */
  public static String MID_RTDATA_EXPLORER_SETTINGS_M5MODEL = "m5.mid.IRtDataExplorerSettings"; //$NON-NLS-1$

  /**
   * Attribute {@link IRtDataExplorerSettings#nmName()}.
   */
  public final IM5AttributeFieldDef<IRtDataExplorerSettings> NAME = new M5AttributeFieldDef<>( FID_NAME, DDEF_NAME ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_SETTINGS_NAME, STR_D_SETTINGS_NAME );
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( IRtDataExplorerSettings aEntity ) {
      return avStr( aEntity.nmName() );
    }

  };

  /**
   * LM for this model.
   *
   * @author dima
   */
  static class LifecycleManager
      extends M5LifecycleManager<IRtDataExplorerSettings, ISkConnection>
      implements IGenericChangeListener {

    public LifecycleManager( IM5Model<IRtDataExplorerSettings> aModel, ISkConnection aMaster ) {
      super( aModel, false, true, false, true, aMaster );
    }

    @Override
    protected void doRemove( IRtDataExplorerSettings aEntity ) {
      master().coreApi().objService().removeObject( aEntity.skid() );
    }

    @Override
    protected IList<IRtDataExplorerSettings> doListEntities() {
      IListEdit<IRtDataExplorerSettings> retVal = new ElemArrayList<>();
      ISkObjectService objService = master().coreApi().objService();
      for( ISkObject settingsPack : objService.listObjs( IRtDataExplorerSettings.CLASS_ID, false ) ) {
        retVal.add( (IRtDataExplorerSettings)settingsPack );
      }
      return retVal;
    }

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      // nop
    }

  }

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - Sk-connection to be used in lm
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public RtDataExplorerSettingsM5Model( ISkConnection aConn ) {
    super( MID_RTDATA_EXPLORER_SETTINGS_M5MODEL, IRtDataExplorerSettings.class, aConn );
    addFieldDefs( NAME );
  }

  @Override
  protected IM5LifecycleManager<IRtDataExplorerSettings> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this, skConn() );
  }

  @Override
  protected IM5LifecycleManager<IRtDataExplorerSettings> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
