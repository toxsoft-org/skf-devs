package org.toxsoft.skf.devs.rtbrowser.gui.widgets;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;

/**
 * Элемент панели быстрого доступа, представляющий собой однострочный текст {@link CLabel}.
 * <p>
 *
 * @author vs
 */
public class CLabelContribution
    extends ControlContribution {

  private final int width;
  private final int swtStyle;
  private String    text;
  CLabel            label;

  /**
   * Конструктор.
   *
   * @param aId String - ИД элемента
   * @param aWidth int - ширина текстового поля
   * @param aText String - текст
   * @param aSwtStyle int - swt стиль
   */
  public CLabelContribution( String aId, int aWidth, String aText, int aSwtStyle ) {
    super( aId );
    width = aWidth;
    swtStyle = aSwtStyle;
    text = aText;
  }

  // ------------------------------------------------------------------------------------
  // ControlContribution
  //

  @Override
  protected Control createControl( Composite aParent ) {
    label = new CLabel( aParent, swtStyle );
    label.setText( text );
    return label;
  }

  @Override
  protected int computeWidth( Control aControl ) {
    if( width == SWT.DEFAULT ) {
      return super.computeWidth( aControl );
    }
    return width;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает текстовое поле.
   *
   * @return CLabel - текстовое поле
   */
  public CLabel label() {
    return label;
  }

}
