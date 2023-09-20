package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.rtdserv.*;

/**
 * Редактор значения RtData для колонки Summary
 *
 * @author dima
 */
public class SummaryRtDataValueEditingSupport
    extends BaseRtDataValueEditingSupport {

  private final ISkidList skidList;

  SummaryRtDataValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext,
      ISkidList aSkidList ) {
    super( aViewer, aCoreApi, aTsContext );
    skidList = aSkidList;
  }

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    CellEditor retVal = null;
    RtDataBrowserRow browserRow = (RtDataBrowserRow)aElement;
    IAtomicValue dfltCellVal = IAtomicValue.NULL;
    IAtomicValue cellVal = browserRow.objCellVal( skidList.first() );
    EAtomicType cellValueType = browserRow.dataInfo().dataType().atomicType();
    switch( cellValueType ) {
      case BOOLEAN:
        currEditor = boolEditor;
        if( cellVal.atomicType() != EAtomicType.BOOLEAN ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        retVal = currEditor;
        break;
      case FLOATING:
        currEditor = floatEditor;
        if( cellVal.atomicType() != EAtomicType.FLOATING ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        retVal = currEditor;
        break;
      case INTEGER:
        currEditor = intEditor;
        if( cellVal.atomicType() != EAtomicType.INTEGER ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        retVal = currEditor;
        break;
      case NONE:
        break;
      case STRING:
        currEditor = textEditor;
        if( cellVal.atomicType() != EAtomicType.STRING ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        break;
      case TIMESTAMP:
        break;
      case VALOBJ:
        break;
      default:
        break;

    }
    return retVal;
  }

  @Override
  protected Object getValue( Object aElement ) {
    return currEditor.getValue();
  }

  @Override
  protected void setValue( Object aElement, Object aValue ) {
    RtDataBrowserRow browserRow = (RtDataBrowserRow)aElement;
    IAtomicValue avValue = (IAtomicValue)aValue;
    GwidList gwidList = new GwidList();
    for( Skid cellSkid : skidList ) {
      gwidList.add( Gwid.createRtdata( cellSkid.classId(), cellSkid.strid(), browserRow.dataInfo().id() ) );
    }
    IMap<Gwid, ISkWriteCurrDataChannel> wCurrDataSet = coreApi.rtdService().createWriteCurrDataChannels( gwidList );
    for( Gwid cellGwid : gwidList ) {
      wCurrDataSet.getByKey( cellGwid ).setValue( avValue );
    }
    // закрываем каналы
    for( ISkWriteCurrDataChannel channel : wCurrDataSet.values() ) {
      channel.close();
    }
  }
}
