package org.toxsoft.skf.devs.rtbrowser.gui.editors;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Копия {@link ValedAvFloatSpinner} для ввода значений с плавающей запятой больше 1000.
 *
 * @author vs
 */
public class ValedAvFloatSpinner
    extends AbstractAvWrapperValedControl<Double> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvFloatSpinner"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvFloatSpinner( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      return aAtomicType == EAtomicType.FLOATING;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvFloatSpinner( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.FLOATING, DoubleSpinner.FACTORY );
  }

  @Override
  protected Double av2tv( IAtomicValue aAtomicValue ) {
    return Double.valueOf( aAtomicValue.asDouble() );
  }

  @Override
  protected IAtomicValue tv2av( Double aValue ) {
    return AvUtils.avFloat( aValue.doubleValue() );
  }

}
