package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.*;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.ValedAvFloatSpinner;
import org.toxsoft.uskat.core.*;

/**
 * Базовый редактор для редактирования значения/значений RtData в ячейках {@link RtDataBrowser}
 *
 * @author dima
 */
public class BaseRtDataValueEditingSupport
    extends EditingSupport {

  protected final ISkCoreApi    coreApi;
  protected final ITsGuiContext tsGuiContext;
  protected final TableViewer   tableViewer;

  protected ValedCellEditor<IAtomicValue> currEditor;
  protected ValedCellEditor<IAtomicValue> intEditor;
  protected ValedCellEditor<IAtomicValue> textEditor;
  protected ValedCellEditor<IAtomicValue> boolEditor;
  protected ValedCellEditor<IAtomicValue> floatEditor;

  protected BaseRtDataValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aViewer );
    coreApi = aCoreApi;
    tsGuiContext = aTsContext;
    tableViewer = aViewer;

    textEditor = new ValedCellEditor<>( new ValedAvAnytypeText( aTsContext ), aViewer, aTsContext );
    intEditor = new ValedCellEditor<>( new ValedAvIntegerSpinner( aTsContext ), aViewer, aTsContext );
    boolEditor = new ValedCellEditor<>( new ValedAvBooleanCheck( aTsContext ), aViewer, aTsContext );
    floatEditor = new ValedCellEditor<>( new ValedAvFloatSpinner( aTsContext ), aViewer, aTsContext );
  }

  @Override
  protected boolean canEdit( Object aElement ) {
    RtDataBrowserRow browserRow = (RtDataBrowserRow)aElement;
    EAtomicType cellValueType = browserRow.dataInfo().dataType().atomicType();
    return switch( cellValueType ) {
      case BOOLEAN, FLOATING, INTEGER, STRING, VALOBJ -> true;
      case TIMESTAMP, NONE -> false;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
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
