package org.toxsoft.skf.devs.rtbrowser.gui.panels.links;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.*;

/**
 * Редактор значения в ячейке таблицы просмотра связей
 *
 * @author dima
 */
public class ObjectLinkValueEditingSupport
    extends BaseLinkValueEditingSupport {

  private final Skid skid;

  ObjectLinkValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext, Skid aSkid ) {
    super( aViewer, aCoreApi, aTsContext );
    skid = aSkid;
  }

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    currEditor = new LinkCellEditor( (Composite)getViewer().getControl(), tsContext );
    LinkBrowserRow browserRow = (LinkBrowserRow)aElement;
    currEditor.setValue( browserRow.linkCellVal( skid ) );
    return currEditor;
  }

  @Override
  protected Object getValue( Object aElement ) {
    return currEditor.getValue();
  }

  @Override
  protected void setValue( Object aElement, Object aValue ) {
    // здесь ничего не нужно делать поскольку в редактировании используется технология М5 и внутри нее реализуется
    // редактирование связи
  }
}
