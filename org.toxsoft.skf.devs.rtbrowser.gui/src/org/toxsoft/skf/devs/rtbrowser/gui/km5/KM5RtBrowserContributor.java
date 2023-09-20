package org.toxsoft.skf.devs.rtbrowser.gui.km5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * Contributes M5-models for RtBrowser entities.
 *
 * @author dima
 */
public class KM5RtBrowserContributor
    extends KM5AbstractContributor {

  /**
   * Creator singleton.
   */
  public static final IKM5ContributorCreator CREATOR = KM5RtBrowserContributor::new;

  private static final IStringList CONRTIBUTED_MODEL_IDS = new StringArrayList( //
      DtoLinkFwdM5Model.MID_LINK_FWD_M5MODEL, //
      EventM5Model.MID_SKEVENT_M5MODEL, //
      CommandM5Model.MID_SKCOMMAND_M5MODEL //
  );

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @param aDomain {@link IM5Domain} - connection domain
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public KM5RtBrowserContributor( ISkConnection aConn, IM5Domain aDomain ) {
    super( aConn, aDomain );
  }

  @Override
  protected IStringList papiCreateModels() {
    m5().addModel( new DtoLinkFwdM5Model( skConn() ) );
    m5().addModel( new EventM5Model( skConn() ) );
    m5().addModel( new CommandM5Model( skConn() ) );
    return CONRTIBUTED_MODEL_IDS;
  }

}
