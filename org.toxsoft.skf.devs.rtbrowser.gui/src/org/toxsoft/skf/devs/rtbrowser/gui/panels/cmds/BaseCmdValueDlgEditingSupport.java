package org.toxsoft.skf.devs.rtbrowser.gui.panels.cmds;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.uskat.core.*;

/**
 * Базовый редактор для редактирования событий в ячейках {@link CommandsBrowser}
 *
 * @author dima
 */
public class BaseCmdValueDlgEditingSupport
    extends EditingSupport {

  protected final ISkCoreApi    coreApi;
  protected final ITsGuiContext tsContext;

  protected CommandCellEditor currEditor;

  BaseCmdValueDlgEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
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
