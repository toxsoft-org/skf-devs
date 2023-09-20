package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.ValedAvFloatSpinner;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.ValedCellEditor;
import org.toxsoft.uskat.core.ISkCoreApi;

/**
 * Базовый редактор для редактирования значения/значений RtData в ячейках {@link RtDataBrowser}
 *
 * @author dima
 */
public class BaseRtDataValueEditingSupport
    extends EditingSupport {

  protected final ISkCoreApi coreApi;

  protected ValedCellEditor<IAtomicValue> currEditor;
  protected ValedCellEditor<IAtomicValue> intEditor;
  protected ValedCellEditor<IAtomicValue> textEditor;
  protected ValedCellEditor<IAtomicValue> boolEditor;
  protected ValedCellEditor<IAtomicValue> floatEditor;

  BaseRtDataValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aViewer );
    coreApi = aCoreApi;

    textEditor = new ValedCellEditor<>( new ValedAvAnytypeText( aTsContext ), aViewer, aTsContext );
    intEditor = new ValedCellEditor<>( new ValedAvIntegerSpinner( aTsContext ), aViewer, aTsContext );
    boolEditor = new ValedCellEditor<>( new ValedAvBooleanCheck( aTsContext ), aViewer, aTsContext );
    floatEditor = new ValedCellEditor<>( new ValedAvFloatSpinner( aTsContext ), aViewer, aTsContext );
  }

  @Override
  protected boolean canEdit( Object aElement ) {
    RtDataBrowserRow browserRow = (RtDataBrowserRow)aElement;
    EAtomicType cellValueType = browserRow.dataInfo().dataType().atomicType();
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
  protected CellEditor getCellEditor( Object aElement ) {
    return null;
  }

  @Override
  protected void setValue( Object aElement, Object aValue ) {
    // nop
  }

}
