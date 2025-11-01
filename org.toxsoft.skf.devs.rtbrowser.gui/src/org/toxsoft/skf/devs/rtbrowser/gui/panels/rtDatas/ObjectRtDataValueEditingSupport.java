package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.rtdserv.*;

/**
 * Редактор значения RtData для одного объекта
 *
 * @author dima
 */
public class ObjectRtDataValueEditingSupport
    extends BaseRtDataValueEditingSupport {

  private final Skid skid;

  ObjectRtDataValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext, Skid aSkid ) {
    super( aViewer, aCoreApi, aTsContext );
    skid = aSkid;
  }

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    CellEditor retVal = null;
    RtDataBrowserRow browserRow = (RtDataBrowserRow)aElement;
    IAtomicValue dfltCellVal = IAtomicValue.NULL;
    IAtomicValue cellVal = browserRow.objCellVal( skid );
    EAtomicType cellValueType = browserRow.dataInfo().dataType().atomicType();
    switch( cellValueType ) {
      case BOOLEAN:
        currEditor = boolEditor;
        if( cellVal.atomicType() != EAtomicType.BOOLEAN ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        retVal = currEditor;
        break;
      case FLOATING:
        currEditor = floatEditor;
        if( cellVal.atomicType() != EAtomicType.FLOATING ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        retVal = currEditor;
        break;
      case INTEGER:
        currEditor = intEditor;
        if( cellVal.atomicType() != EAtomicType.INTEGER ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        retVal = currEditor;
        break;
      case NONE:
        break;
      case STRING:
        currEditor = textEditor;
        if( cellVal.atomicType() != EAtomicType.STRING ) {
          cellVal = dfltCellVal;
        }
        currEditor.setValue( cellVal );
        retVal = currEditor;
        break;
      case TIMESTAMP:
        break;
      case VALOBJ:
        // TODO dima 18.12.24 код просто как набросок. Требуется учесть что VALOBJ может быть не enum и проч.
        // тут приходится создавать на лету
        IDataType dt = browserRow.dataInfo().dataType();
        IEntityKeeper<?> keeper = TsValobjUtils.findKeeperById( dt.keeperId() );
        if( keeper != null ) {
          ITsGuiContext editorContext = new TsGuiContext( tsGuiContext );
          Class<?> rawClass = keeper.entityClass();
          if( rawClass != null && rawClass.isEnum() ) {
            IValedEnumConstants.REFDEF_ENUM_CLASS.setRef( editorContext, rawClass );
          }
          ValedCellEditor<IAtomicValue> enumEditor = new ValedCellEditor<>(
              ValedAvValobjEnumCombo.FACTORY.createEditor( editorContext ), tableViewer, editorContext );
          currEditor = enumEditor;
          if( cellVal.atomicType() != EAtomicType.VALOBJ ) {
            cellVal = dfltCellVal;
          }
          currEditor.setValue( cellVal );
          retVal = currEditor;
        }
        break;
      default:
        break;

    }
    return retVal;
  }

  @Override
  protected Object getValue( Object aElement ) {
    return currEditor.getValue();
  }

  @Override
  protected void setValue( Object aElement, Object aValue ) {
    RtDataBrowserRow browserRow = (RtDataBrowserRow)aElement;
    IAtomicValue avValue = (IAtomicValue)aValue;
    Gwid cellGwid = Gwid.createRtdata( skid.classId(), skid.strid(), browserRow.dataInfo().id() );
    ISkWriteCurrDataChannel writeChannel = coreApi.rtdService().createWriteCurrDataChannel( cellGwid );
    writeChannel.setValue( avValue );
    // закрываем каналы
    // не закрываем канал, ломает работу системы
    // writeChannel.close();
  }
}
