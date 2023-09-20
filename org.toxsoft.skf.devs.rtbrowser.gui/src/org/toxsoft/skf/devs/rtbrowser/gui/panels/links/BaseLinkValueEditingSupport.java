package org.toxsoft.skf.devs.rtbrowser.gui.panels.links;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.uskat.core.*;

/**
 * Базовый редактор для редактирования связей в ячейках {@link LinksBrowser}
 *
 * @author dima
 */
public class BaseLinkValueEditingSupport
    extends EditingSupport {

  protected final ISkCoreApi    coreApi;
  protected final ITsGuiContext tsContext;

  protected LinkCellEditor currEditor;

  BaseLinkValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aViewer );
    coreApi = aCoreApi;
    tsContext = aTsContext;
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
