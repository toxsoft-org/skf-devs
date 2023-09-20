package org.toxsoft.skf.devs.rtbrowser.mws.e4.uiparts;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.widgets.TsComposite;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.RtBrowserPanel;
import org.toxsoft.uskat.core.gui.e4.uiparts.SkMwsAbstractPart;

/**
 * Просмотр и редактирование в реальном времени свойств ISkObject таких как:
 * <li>attributes</li>
 * <li>rtDatas</li>
 * <li>cmds</li>
 * <li>events</li>
 * <li>links</li>
 * <p>
 *
 * @author dima
 */
public class UipartRtBrowser
    extends SkMwsAbstractPart {

  RtBrowserPanel panel;

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    ITsGuiContext ctx = new TsGuiContext( getWindowContext() );
    panel = new RtBrowserPanel( aParent, ctx );
    panel.setLayoutData( BorderLayout.CENTER );
  }

}
