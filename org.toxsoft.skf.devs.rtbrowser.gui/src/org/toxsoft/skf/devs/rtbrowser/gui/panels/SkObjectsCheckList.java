package org.toxsoft.skf.devs.rtbrowser.gui.panels;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Панель выбора объектов одного класса
 *
 * @author vs
 */
class SkObjectsCheckList
    implements IGenericChangeEventCapable {

  ViewerFilter mpFilter = new ViewerFilter() {

    @Override
    public boolean select( Viewer aViewer, Object aParentElement, Object aElement ) {
      if( mPath == null ) {
        return true;
      }
      ISkObject skObj = (ISkObject)aElement;
      return isInPath( skObj );
    }
  };

  ViewerFilter textFilter = new ViewerFilter() {

    @Override
    public boolean select( Viewer aViewer, Object aParentElement, Object aElement ) {
      if( filterText == null || filterText.isBlank() ) {
        return true;
      }

      ISkObject skObj = (ISkObject)aElement;
      if( skObj.nmName().toLowerCase().contains( filterText.toLowerCase() ) ) {
        return true;
      }
      if( skObj.id().toLowerCase().contains( filterText.toLowerCase() ) ) {
        return true;
      }
      return false;
    }
  };

  TableViewer viewer;

  private final ISkCoreApi coreApi;

  String targetClassId;

  String filterText = TsLibUtils.EMPTY_STRING;

  IGwidPath mPath = null;

  private final GenericChangeEventer eventer;

  TableViewerColumn columnName;

  TableViewerColumn columnId;

  public static SkObjectsCheckList createCheckList( Composite aParent, String aClassId, ISkCoreApi aCoreApi ) {
    return new SkObjectsCheckList( aParent, aClassId, aCoreApi, SWT.CHECK );
  }

  public static SkObjectsCheckList createSimpleList( Composite aParent, String aClassId, ISkCoreApi aCoreApi ) {
    return new SkObjectsCheckList( aParent, aClassId, aCoreApi, 0 );
  }

  private SkObjectsCheckList( Composite aParent, String aClassId, ISkCoreApi aCoreApi, int aStyle ) {
    coreApi = aCoreApi;

    eventer = new GenericChangeEventer( this );

    if( aStyle == SWT.CHECK ) {
      viewer = CheckboxTableViewer.newCheckList( aParent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
      ((CheckboxTableViewer)viewer).addCheckStateListener( aEvent -> eventer.fireChangeEvent() );
    }
    else {
      viewer = new TableViewer( aParent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
    }
    initViewer( viewer );
    viewer.setContentProvider( new ArrayContentProvider() );
    viewer.addFilter( textFilter );

    if( aClassId != null && !aClassId.isBlank() ) {
      IList<ISkObject> objects = coreApi.objService().listObjs( aClassId, true );
      viewer.setInput( objects.toArray() );
    }

  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public IList<ISkObject> checkedObjects() {
    IListEdit<ISkObject> result = new ElemArrayList<>();
    for( Object obj : checkboxViewer().getCheckedElements() ) {
      result.add( (ISkObject)obj );
    }
    return result;
  }

  CheckboxTableViewer checkboxViewer() {
    return (CheckboxTableViewer)viewer;
  }

  TableViewer viewer() {
    return viewer;
  }

  public void setClassId( String aClassId ) {
    targetClassId = aClassId;
    if( aClassId != null && !aClassId.isBlank() ) {
      if( mPath == null ) {
        IList<ISkObject> objects = coreApi.objService().listObjs( aClassId, true );
        viewer.setInput( objects.toArray() );
      }
      else {
        setGwidPath( mPath );
      }
    }
    else {
      viewer.setInput( null );
    }
  }

  public void setObjSkids( ISkidList aSkidList ) {
    IList<ISkObject> objects = coreApi.objService().getObjs( aSkidList );
    viewer.setInput( objects.toArray() );
    checkAll();
    eventer.fireChangeEvent();
  }

  public void checkAll() {
    checkboxViewer().setAllChecked( true );
  }

  public void uncheckAll() {
    checkboxViewer().setAllChecked( false );
  }

  public void setGwidPath( IGwidPath aGwidPath ) {
    mPath = aGwidPath;

    IListEdit<ISkObject> objects = new ElemArrayList<>();
    Gwid gwid = aGwidPath.gwids().last();

    if( !gwid.isAbstract() ) {
      fillLinkedList( gwid, objects );
    }
    else {
      if( gwid.toString().contains( "$link" ) ) { // это ссылка //$NON-NLS-1$
        Gwid parentGwid = aGwidPath.prevGwid( gwid );
        if( parentGwid != null ) {
          IListEdit<ISkObject> leftObjects = new ElemArrayList<>();
          for( ISkObject obj : coreApi.objService().listObjs( parentGwid.classId(), true ) ) {
            IDtoLinkFwd link;
            link = coreApi.linkService().getLinkFwd( Gwid.createLink( obj.classId(), obj.strid(), gwid.propId() ) );
            leftObjects.addAll( coreApi.objService().getObjs( link.rightSkids() ) );
          }
          for( ISkObject obj : leftObjects ) {
            fillLinkedList( Gwid.createObj( obj.skid() ), objects );
          }
        }
      }
      else { // это класс
        for( ISkObject obj : coreApi.objService().listObjs( gwid.classId(), true ) ) {
          fillLinkedList( Gwid.createObj( obj.skid() ), objects );
        }
      }
    }
    viewer.setInput( objects.toArray() );
  }

  public void setFilterText( String aText ) {
    filterText = aText;
    viewer.refresh();
  }

  // ------------------------------------------------------------------------------------
  // Imlementation
  //

  private void initViewer( TableViewer aViewer ) {
    aViewer.getTable().setHeaderVisible( true );
    aViewer.getTable().setLinesVisible( true );

    columnName = new TableViewerColumn( aViewer, SWT.NONE );
    columnName.getColumn().setText( "Имя" );
    columnName.getColumn().setWidth( 300 );
    columnName.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ISkObject skObj = (ISkObject)aCell.getElement();
        aCell.setText( skObj.nmName() );
      }
    } );
    columnName.getColumn().addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        if( aViewer.getTable().getSortDirection() == SWT.UP ) {
          aViewer.getTable().setSortDirection( SWT.DOWN );
        }
        else {
          aViewer.getTable().setSortDirection( SWT.UP );
        }
        aViewer.getTable().setSortColumn( columnName.getColumn() );
        aViewer.refresh();
      }
    } );

    columnId = new TableViewerColumn( aViewer, SWT.NONE );
    columnId.getColumn().setText( "Идентификатор" );
    columnId.getColumn().setWidth( 200 );
    columnId.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ISkObject skObj = (ISkObject)aCell.getElement();
        aCell.setText( skObj.strid() );
      }
    } );
    columnId.getColumn().addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        if( aViewer.getTable().getSortDirection() == SWT.UP ) {
          aViewer.getTable().setSortDirection( SWT.DOWN );
        }
        else {
          aViewer.getTable().setSortDirection( SWT.UP );
        }
        aViewer.getTable().setSortColumn( columnId.getColumn() );
        aViewer.refresh();
      }
    } );

    viewer.setComparator( new ViewerComparator() {

      @Override
      public int compare( Viewer aV, Object aObj1, Object aObj2 ) {
        ISkObject skObj1 = (ISkObject)aObj1;
        ISkObject skObj2 = (ISkObject)aObj2;
        TableColumn column = ((TableViewer)aV).getTable().getSortColumn();
        if( column != null ) {
          if( column.equals( columnName.getColumn() ) ) {
            int result = skObj1.nmName().compareTo( skObj2.nmName() );
            if( ((TableViewer)aV).getTable().getSortDirection() == SWT.UP ) {
              return result;
            }
            return -result;
          }
          if( column.equals( columnId.getColumn() ) ) {
            int result = skObj1.id().compareTo( skObj2.id() );
            if( ((TableViewer)aV).getTable().getSortDirection() == SWT.UP ) {
              return result;
            }
            return -result;
          }
        }
        return 0;
      }
    } );

  }

  boolean isInPath( ISkObject aObj ) {
    String masterClassId = "ci.CStation";
    for( ISkObject sko : coreApi.objService().listObjs( masterClassId, true ) ) {
      // Gwid gwid = mPath.resolve( sko, coreApi );
      Gwid gwid = mPath.gwids().last();
      for( ISkObject skobj : coreApi.objService().listObjs( gwid.classId(), true ) ) {
        IStringMap<IDtoLinkFwd> links = coreApi.linkService().getAllLinksFwd( skobj.skid() );
        for( IDtoLinkFwd lf : links.values() ) {
          if( lf.rightSkids().hasElem( aObj.skid() ) ) {
            return true;
          }
        }
      }
      if( gwid.skid().equals( aObj.skid() ) ) {
        return true;
      }
    }
    return false;
  }

  void fillLinkedList( Skid aLeftSkid, IListEdit<ISkObject> aObjects ) {
    for( ISkObject sko : coreApi.objService().listObjs( aLeftSkid.classId(), true ) ) {
      IStringMap<IDtoLinkFwd> links = coreApi.linkService().getAllLinksFwd( sko.skid() );
      for( IDtoLinkFwd lf : links.values() ) {
        for( Skid lSkid : lf.rightSkids() ) {
          if( lSkid.classId().equals( targetClassId ) ) {
            aObjects.add( coreApi.objService().find( lSkid ) );
          }
          else {
            fillLinkedList( lSkid, aObjects );
          }
        }
      }
    }
  }

  void fillLinkedList( Gwid aGwid, IListEdit<ISkObject> aObjects ) {
    System.out.println( aGwid );
    if( !aGwid.isAbstract() ) {
      IStringMap<IDtoLinkFwd> links = coreApi.linkService().getAllLinksFwd( aGwid.skid() );
      if( aGwid.classId().equals( targetClassId ) ) {
        aObjects.add( coreApi.objService().find( aGwid.skid() ) );
        return;
      }
      for( IDtoLinkFwd lf : links.values() ) {
        for( ISkObject skObj : coreApi.objService().getObjs( lf.rightSkids() ) ) {
          if( skObj.classId().equals( targetClassId ) ) {
            aObjects.add( skObj );
          }
          else {
            fillLinkedList( Gwid.createObj( skObj.skid() ), aObjects );
          }
        }
      }
    }
  }

}
