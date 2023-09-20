/**
 *
 */
package org.toxsoft.skf.devs.rtbrowser.gui.panels.base;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Базовый класс - модель (в парадигме MVC) одного ряда данных компонента {@link RtDataBrowser}
 *
 * @author dima
 */
public abstract class BaseBrowserRow {

  protected final IDtoClassPropInfoBase сlassPropInfoBase;
  protected final ISkCoreApi            coreApi;
  protected final ISkidList             skidList;

  /**
   * @param aCoreApi server's API
   * @param aSkidList список {@link Skid} данные которых отображаются
   * @param aClassPropInfoBase описание свойства класса
   */
  public BaseBrowserRow( ISkCoreApi aCoreApi, ISkidList aSkidList, IDtoClassPropInfoBase aClassPropInfoBase ) {
    сlassPropInfoBase = aClassPropInfoBase;
    coreApi = aCoreApi;
    skidList = aSkidList;
    doInit( aCoreApi, aSkidList );
  }

  @SuppressWarnings( "unused" )
  protected void doInit( ISkCoreApi aCoreApi, ISkidList aSkidList ) {
    // nop
  }

  protected void dispose() {
    // nop
  }

  abstract protected IAtomicValue objCellVal( Skid aCellSkid );

  /**
   * @return описание одного свойства класса { @link IDtoClassPropInfoBase }
   */
  public IDtoClassPropInfoBase propInfo() {
    return сlassPropInfoBase;
  }

  /**
   * @return название свойства класса { @link IDtoClassPropInfoBase#nmName() }
   */
  public String propNameCell() {
    // return String.format( "%s [%s]", сlassPropInfoBase.nmName(), сlassPropInfoBase.id() ); //$NON-NLS-1$
    return String.format( "%s", сlassPropInfoBase.nmName() ); //$NON-NLS-1$
  }

  protected String propIdCell() {
    return сlassPropInfoBase.id();
  }

  abstract protected IAtomicValue summaryCellVal();

}
