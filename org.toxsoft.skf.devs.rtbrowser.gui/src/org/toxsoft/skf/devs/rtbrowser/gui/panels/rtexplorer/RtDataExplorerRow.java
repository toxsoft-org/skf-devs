package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer;

import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

public class RtDataExplorerRow
    extends RtDataBrowserRow {

  private Skid skid;

  private IDtoRtdataInfo rtDataInfo;

  public RtDataExplorerRow( ISkCoreApi aCoreApi, ISkidList aSkidList, IDtoRtdataInfo aRtdataInfo ) {
    super( aCoreApi, aSkidList, aRtdataInfo );
    skid = aSkidList.first();
    rtDataInfo = aRtdataInfo;
  }

  Skid getSkid() {
    return skid;
  }

  String getClassId() {
    return skid.classId();
  }

  String getObjId() {
    return skid.strid();
  }

  IDtoRtdataInfo dataInfo() {
    return rtDataInfo;
  }

}
