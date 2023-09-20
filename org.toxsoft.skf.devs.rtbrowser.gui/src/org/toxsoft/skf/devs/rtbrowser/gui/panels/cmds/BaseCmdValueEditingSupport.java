package org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.ValedAvFloatSpinner;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.ValedCellEditor;
import org.toxsoft.uskat.core.ISkCoreApi;

/**
 * Базовый редактор для генерации команд в ячейках {@link CommandsBrowser}
 *
 * @author dima
 */
public class BaseCmdValueEditingSupport
    extends EditingSupport {

  protected final ISkCoreApi    coreApi;
  protected final ITsGuiContext tsContext;

  protected ValedCellEditor<IAtomicValue> currEditor;
  protected ValedCellEditor<IAtomicValue> intEditor;
  protected ValedCellEditor<IAtomicValue> textEditor;
  protected ValedCellEditor<IAtomicValue> boolEditor;
  protected ValedCellEditor<IAtomicValue> floatEditor;

  BaseCmdValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aViewer );
    coreApi = aCoreApi;
    tsContext = aTsContext;

    textEditor = new ValedCellEditor<>( new ValedAvAnytypeText( aTsContext ), aViewer, aTsContext );
    intEditor = new ValedCellEditor<>( new ValedAvIntegerSpinner( aTsContext ), aViewer, aTsContext );
    boolEditor = new ValedCellEditor<>( new ValedAvBooleanCheck( aTsContext ), aViewer, aTsContext );
    floatEditor = new ValedCellEditor<>( new ValedAvFloatSpinner( aTsContext ), aViewer, aTsContext );
  }

  @Override
  protected boolean canEdit( Object aElement ) {
    return true;
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
