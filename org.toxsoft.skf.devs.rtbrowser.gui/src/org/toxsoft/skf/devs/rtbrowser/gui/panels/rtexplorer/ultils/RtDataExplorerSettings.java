package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.gw.IGwHardConstants.*;
import static org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils.ISkResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Rонтейнер для хранения набора настроек {@link RtDataExplorerPanel}
 *
 * @author dima
 */
public class RtDataExplorerSettings
    extends SkObject
    implements IRtDataExplorerSettings {

  /**
   * id для хранения списка RtData Gwids в Clob
   */
  private transient Gwid clobIdRtGwids = null;

  /**
   * Clob id to store GwidList of rtData params
   */
  static String CLBID_RTDATAEXPLORER_PARAMS = "rtDataExplorerParams"; //$NON-NLS-1$

  /**
   * CLOB {@link RtDataExplorerSettings#listUserRtGwids()}.
   */
  static IDtoClobInfo CLBINF_RTDATAEXPLORER_PARAMS = DtoClobInfo.create2( CLBID_RTDATAEXPLORER_PARAMS, //
      TSID_NAME, STR_N_RTDATAEXPLORER_PARAMS, //
      TSID_DESCRIPTION, STR_D_RTDATAEXPLORER_PARAMS //
  );

  /**
   * attr id width of panel
   */
  static String ATRID_RTDATAEXPLORER_WIDTH = "rtDataExplorerWidth"; //$NON-NLS-1$

  /**
   * Attribute {@link RtDataExplorerSettings#size()}.
   */
  static IDtoAttrInfo ATRINF_RTDATAEXPLORER_WIDTH = DtoAttrInfo.create2( ATRID_RTDATAEXPLORER_WIDTH, DDEF_INTEGER, //
      TSID_NAME, STR_N_RTDATAEXPLORER_WIDTH, //
      TSID_DESCRIPTION, STR_D_RTDATAEXPLORER_WIDTH );

  /**
   * attr id height of panel
   */
  static String ATRID_RTDATAEXPLORER_HEIGHT = "rtDataExplorerHeight"; //$NON-NLS-1$

  /**
   * Attribute {@link RtDataExplorerSettings#size()}.
   */
  static IDtoAttrInfo ATRINF_RTDATAEXPLORER_HEIGHT = DtoAttrInfo.create2( ATRID_RTDATAEXPLORER_HEIGHT, DDEF_INTEGER, //
      TSID_NAME, STR_N_RTDATAEXPLORER_HEIGHT, //
      TSID_DESCRIPTION, STR_D_RTDATAEXPLORER_HEIGHT );

  /**
   * attr id location x of panel
   */
  static String ATRID_RTDATAEXPLORER_X = "rtDataExplorerX"; //$NON-NLS-1$

  /**
   * Attribute {@link RtDataExplorerSettings#location()}.
   */
  static IDtoAttrInfo ATRINF_RTDATAEXPLORER_X = DtoAttrInfo.create2( ATRID_RTDATAEXPLORER_X, DDEF_INTEGER, //
      TSID_NAME, STR_N_RTDATAEXPLORER_X, //
      TSID_DESCRIPTION, STR_D_RTDATAEXPLORER_X );

  /**
   * attr id location y of panel
   */
  static String ATRID_RTDATAEXPLORER_Y = "rtDataExplorerY"; //$NON-NLS-1$

  /**
   * Attribute {@link RtDataExplorerSettings#location()}.
   */
  static IDtoAttrInfo ATRINF_RTDATAEXPLORER_Y = DtoAttrInfo.create2( ATRID_RTDATAEXPLORER_Y, DDEF_INTEGER, //
      TSID_NAME, STR_N_RTDATAEXPLORER_Y, //
      TSID_DESCRIPTION, STR_D_RTDATAEXPLORER_Y );

  private ISkObject shadowObj;

  RtDataExplorerSettings( Skid aSkid ) {
    super( aSkid );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  RtDataExplorerSettings( ISkObject aSettingsPack ) {
    super( aSettingsPack.skid() );
    shadowObj = aSettingsPack;
  }

  protected Gwid clobIdRtGwids() {
    if( clobIdRtGwids == null ) {
      clobIdRtGwids = Gwid.createClob( shadowObj.classId(), shadowObj.strid(), CLBID_RTDATAEXPLORER_PARAMS );
    }
    return clobIdRtGwids;
  }

  @Override
  public String localName() {
    return shadowObj.attrs().getStr( AID_NAME, TsLibUtils.EMPTY_STRING );
  }

  @Override
  public IGwidList listUserRtGwids() {
    String paramsStr = shadowObj.coreApi().clobService().readClob( clobIdRtGwids() );
    return GwidList.KEEPER.str2ent( paramsStr );
  }

  @Override
  public Point size() {
    int width = shadowObj.attrs().getInt( ATRID_RTDATAEXPLORER_WIDTH );
    int height = shadowObj.attrs().getInt( ATRID_RTDATAEXPLORER_HEIGHT );
    return new Point( width, height );
  }

  @Override
  public Point location() {
    int x = shadowObj.attrs().getInt( ATRID_RTDATAEXPLORER_X );
    int y = shadowObj.attrs().getInt( ATRID_RTDATAEXPLORER_Y );
    return new Point( x, y );
  }

  /**
   * Creates DTO of {@link RtDataExplorerSettings#CLASS_ID} class.
   *
   * @return {@link IDtoClassInfo} - {@link RtDataExplorerSettings#CLASS_ID} class info
   */
  public static IDtoClassInfo internalCreatRtDataExplorerSettingsDto() {
    DtoClassInfo cinf = new DtoClassInfo( IRtDataExplorerSettings.CLASS_ID, GW_ROOT_CLASS_ID, IOptionSet.NULL );
    OPDEF_SK_IS_SOURCE_CODE_DEFINED_CLASS.setValue( cinf.params(), AV_TRUE );
    OPDEF_SK_IS_SOURCE_USKAT_CORE_CLASS.setValue( cinf.params(), AV_TRUE );
    cinf.attrInfos().add( ATRINF_RTDATAEXPLORER_WIDTH );
    cinf.attrInfos().add( ATRINF_RTDATAEXPLORER_HEIGHT );
    cinf.attrInfos().add( ATRINF_RTDATAEXPLORER_X );
    cinf.attrInfos().add( ATRINF_RTDATAEXPLORER_Y );
    cinf.clobInfos().add( CLBINF_RTDATAEXPLORER_PARAMS );
    return cinf;
  }

}
