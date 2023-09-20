package org.toxsoft.skf.devs.rtbrowser.gui.widgets;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Элемент панели быстрого доступа, представляющий собой однострочный текст {@link CLabel}.
 * <p>
 *
 * @author vs
 */
public class TextContribution
    extends ControlContribution {

  private final int width;
  private final int swtStyle;
  Text              text;

  /**
   * Конструктор.
   *
   * @param aId String - ИД элемента
   * @param aWidth int - ширина текстового поля
   * @param aSwtStyle int - swt стиль
   */
  public TextContribution( String aId, int aWidth, int aSwtStyle ) {
    super( aId );
    width = aWidth;
    swtStyle = aSwtStyle;
  }

  // ------------------------------------------------------------------------------------
  // ControlContribution
  //

  @Override
  protected Control createControl( Composite aParent ) {
    text = new Text( aParent, swtStyle );
    text.setText( "text" );
    text.setBackground( new Color( new RGB( 255, 255, 255 ) ) );
    text.setSize( SWT.DEFAULT, 24 );
    return text;
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
   * @return Text - текстовое поле
   */
  public Text textField() {
    return text;
  }

}
