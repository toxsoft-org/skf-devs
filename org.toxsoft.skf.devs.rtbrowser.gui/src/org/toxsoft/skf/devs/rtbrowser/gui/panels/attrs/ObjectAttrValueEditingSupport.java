package org.toxsoft.skf.devs.rtbrowser.gui.panels.attrs;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Редактор значения атрибута для одного объекта
 *
 * @author dima
 */
public class ObjectAttrValueEditingSupport
    extends BaseAttrValueEditingSupport {

  private final Skid skid;

  ObjectAttrValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext, Skid aSkid ) {
    super( aViewer, aCoreApi, aTsContext );
    skid = aSkid;
  }

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    CellEditor retVal = null;
    AttrBrowserRow browserRow = (AttrBrowserRow)aElement;
    IAtomicValue dfltCellVal = IAtomicValue.NULL;
    IAtomicValue cellVal = browserRow.objCellVal( skid );
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
    DtoFullObject dto = DtoFullObject.createDtoFullObject( skid, coreApi );
    dto.attrs().setValue( browserRow.propInfo().id(), avValue );
    coreApi.objService().defineObject( dto );
  }
}
