package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer;

import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Модель (в парадигме MVC) одного ряда данных компонента {@link RtDataExplorer}
 *
 * @author max
 */
public class RtDataExplorerRow
    extends RtDataBrowserRow {

  private Skid skid;

  private IDtoRtdataInfo rtDataInfo;

  private String objName;

  private String className;

  /**
   * Конструктор
   *
   * @param aCoreApi server's API
   * @param aSkid {@link Skid} данное которого отображается
   * @param aRtdataInfo описание данного
   */
  public RtDataExplorerRow( ISkCoreApi aCoreApi, Skid aSkid, IDtoRtdataInfo aRtdataInfo ) {
    super( aCoreApi, new SkidList( aSkid ), aRtdataInfo );
    skid = aSkid;
    rtDataInfo = aRtdataInfo;

    ISkObject obj = coreApi.objService().find( skid );
    objName = obj != null ? obj.nmName() + " (" + obj.description() + ")" : skid.strid();

    ISkClassInfo classInfo = coreApi.sysdescr().findClassInfo( skid.classId() );
    className = classInfo != null ? classInfo.nmName() : skid.classId();
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

  String getObjName() {
    return objName;
  }

  String getClassName() {
    return className;
  }

  IDtoRtdataInfo dataInfo() {
    return rtDataInfo;
  }

}
