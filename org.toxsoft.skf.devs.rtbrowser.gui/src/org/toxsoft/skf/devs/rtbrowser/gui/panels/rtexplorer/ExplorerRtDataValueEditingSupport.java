package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.devs.rtbrowser.gui.editors.*;
import org.toxsoft.skf.devs.rtbrowser.gui.panels.rtDatas.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.rtdserv.*;

public class ExplorerRtDataValueEditingSupport
    extends BaseRtDataValueEditingSupport {

  ExplorerRtDataValueEditingSupport( TableViewer aViewer, ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    super( aViewer, aCoreApi, aTsContext );

  }

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    CellEditor retVal = null;

    RtDataExplorerRow browserRow = (RtDataExplorerRow)aElement;
    Skid skid = browserRow.getSkid();
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
    RtDataExplorerRow browserRow = (RtDataExplorerRow)aElement;
    Skid skid = browserRow.getSkid();
    IAtomicValue avValue = (IAtomicValue)aValue;
    Gwid cellGwid = Gwid.createRtdata( skid.classId(), skid.strid(), browserRow.dataInfo().id() );
    IMap<Gwid, ISkWriteCurrDataChannel> wCurrDataSet =
        coreApi.rtdService().createWriteCurrDataChannels( new GwidList( cellGwid ) );
    wCurrDataSet.getByKey( cellGwid ).setValue( avValue );
    // закрываем каналы
    for( ISkWriteCurrDataChannel channel : wCurrDataSet.values() ) {
      channel.close();
    }
  }
}
