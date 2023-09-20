package org.toxsoft.skf.devs.rtbrowser.gui.panels.attrs;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Редактор значения атрибута для колонки Summary
 *
 * @author dima
 */
public class SummaryAttrValueEditingSupport
    extends BaseAttrValueEditingSupport {

  private final ISkidList skidList;

  SummaryAttrValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext,
      ISkidList aSkidList ) {
    super( aViewer, aCoreApi, aTsContext );
    skidList = aSkidList;
  }

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    CellEditor retVal = null;
    AttrBrowserRow browserRow = (AttrBrowserRow)aElement;
    IAtomicValue dfltCellVal = IAtomicValue.NULL;
    IAtomicValue cellVal = browserRow.objCellVal( skidList.first() );
    IDtoAttrInfo attrInfo = (IDtoAttrInfo)browserRow.propInfo();
    EAtomicType cellValueType = attrInfo.dataType().atomicType();
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
        retVal = currEditor;
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
    AttrBrowserRow browserRow = (AttrBrowserRow)aElement;
    IAtomicValue avValue = (IAtomicValue)aValue;
    for( Skid cellSkid : skidList ) {
      DtoFullObject dto = DtoFullObject.createDtoFullObject( cellSkid, coreApi );
      dto.attrs().setValue( browserRow.propInfo().id(), avValue );
      coreApi.objService().defineObject( dto );
    }
  }
}
