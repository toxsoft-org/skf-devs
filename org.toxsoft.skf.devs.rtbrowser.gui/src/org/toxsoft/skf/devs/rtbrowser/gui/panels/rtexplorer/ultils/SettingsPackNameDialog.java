package org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils;

import static org.toxsoft.skf.devs.rtbrowser.gui.panels.rtexplorer.ultils.ISkResources.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Dialog to enter settings pack name
 *
 * @author dima
 */
public class SettingsPackNameDialog
    extends TitleAreaDialog {

  private Text txtSettingPackName;
  private Text txtSettingPackDescr;

  private String settingPackName;
  private String settingPackDescr;

  /**
   * Constructor.
   *
   * @param aParent - parent shell
   */
  public SettingsPackNameDialog( Shell aParent ) {
    super( aParent );
  }

  @Override
  public void create() {
    super.create();
    setTitle( STR_DLG_TITLE );
    setMessage( STR_DLG_MESSAGE, IMessageProvider.INFORMATION );
  }

  @Override
  protected Control createDialogArea( Composite parent ) {
    Composite area = (Composite)super.createDialogArea( parent );
    Composite container = new Composite( area, SWT.NONE );
    container.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    GridLayout layout = new GridLayout( 2, false );
    container.setLayout( layout );

    createSettingsPackName( container );
    // enough just name
    // createSettingsPackDescr( container );

    return area;
  }

  private void createSettingsPackName( Composite container ) {
    Label lbtFirstName = new Label( container, SWT.NONE );
    lbtFirstName.setText( STR_N_SETTINGS_NAME );

    GridData dataFirstName = new GridData();
    dataFirstName.grabExcessHorizontalSpace = true;
    dataFirstName.horizontalAlignment = GridData.FILL;

    txtSettingPackName = new Text( container, SWT.BORDER );
    txtSettingPackName.setLayoutData( dataFirstName );
  }

  private void createSettingsPackDescr( Composite container ) {
    Label lbtLastName = new Label( container, SWT.NONE );
    lbtLastName.setText( STR_N_SETTINGS_DESCR );

    GridData dataLastName = new GridData();
    dataLastName.grabExcessHorizontalSpace = true;
    dataLastName.horizontalAlignment = GridData.FILL;
    txtSettingPackDescr = new Text( container, SWT.BORDER );
    txtSettingPackDescr.setLayoutData( dataLastName );
  }

  @Override
  protected boolean isResizable() {
    return true;
  }

  // save content of the Text fields because they get disposed
  // as soon as the Dialog closes
  private void saveInput() {
    settingPackName = txtSettingPackName.getText();
    // settingPackDescr = txtSettingPackDescr.getText();

  }

  @Override
  protected void okPressed() {
    saveInput();
    super.okPressed();
  }

  /**
   * @return human readable name
   */
  public String getName() {
    return settingPackName;
  }

  /**
   * @return human readable description
   */
  public String getDescr() {
    return settingPackDescr;
  }
}
