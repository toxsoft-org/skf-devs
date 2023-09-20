package org.toxsoft.skf.devs.rtbrowser.skide.main;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.panels.TsPanel;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.RtBrowserPanel;
import org.toxsoft.skide.core.api.ISkideUnit;
import org.toxsoft.skide.core.api.impl.AbstractSkideUnitPanel;

/**
 * SkiDE unit: RtBrowser unit panel.
 *
 * @author dima
 */

class SkideUnitPanelRtBrowser
    extends AbstractSkideUnitPanel {

  public SkideUnitPanelRtBrowser( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    TsPanel backPanel = new TsPanel( aParent, tsContext() );
    backPanel.setLayout( new BorderLayout() );
    RtBrowserPanel panel = new RtBrowserPanel( backPanel, tsContext() );
    panel.setLayoutData( BorderLayout.CENTER );

    return backPanel;
  }
}
