package org.toxsoft.skf.devs.rtbrowser.gui.panels.attrs;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.ValedAvFloatSpinner;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.ValedCellEditor;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas.RtDataBrowser;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoAttrInfo;

/**
 * Базовый редактор для редактирования значения/значений RtData в ячейках {@link RtDataBrowser}
 *
 * @author dima
 */
abstract public class BaseAttrValueEditingSupport
    extends EditingSupport {

  protected final ISkCoreApi coreApi;

  protected ValedCellEditor<IAtomicValue> currEditor;
  protected ValedCellEditor<IAtomicValue> intEditor;
  protected ValedCellEditor<IAtomicValue> textEditor;
  protected ValedCellEditor<IAtomicValue> boolEditor;
  protected ValedCellEditor<IAtomicValue> floatEditor;

  BaseAttrValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aViewer );
    coreApi = aCoreApi;

    textEditor = new ValedCellEditor<>( new ValedAvAnytypeText( aTsContext ), aViewer, aTsContext );
    intEditor = new ValedCellEditor<>( new ValedAvIntegerSpinner( aTsContext ), aViewer, aTsContext );
    boolEditor = new ValedCellEditor<>( new ValedAvBooleanCheck( aTsContext ), aViewer, aTsContext );
    floatEditor = new ValedCellEditor<>( new ValedAvFloatSpinner( aTsContext ), aViewer, aTsContext );
  }

  @Override
  protected boolean canEdit( Object aElement ) {
    AttrBrowserRow browserRow = (AttrBrowserRow)aElement;
    IDtoAttrInfo attrInfo = (IDtoAttrInfo)browserRow.propInfo();
    EAtomicType cellValueType = attrInfo.dataType().atomicType();
    switch( cellValueType ) {
      case BOOLEAN:
      case FLOATING:
      case INTEGER:
      case STRING:
        return true;
      case TIMESTAMP:
      case NONE:
      case VALOBJ:
        return false;
      default:
        throw new TsNotAllEnumsUsedRtException();

    }
  }

  @Override
  protected Object getValue( Object aElement ) {
    return currEditor.getValue();
  }

  @Override
  abstract protected CellEditor getCellEditor( Object aElement );

  @Override
  abstract protected void setValue( Object aElement, Object aValue );

}
