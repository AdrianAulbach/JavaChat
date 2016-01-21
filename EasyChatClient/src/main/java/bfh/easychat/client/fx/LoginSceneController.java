package bfh.easychat.client.fx;

import java.util.ResourceBundle;

import bfh.easychat.client.viewmodel.ConnectionViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginSceneController {

    private Stage dialogStage;
    private boolean okClicked;

    @FXML
    private ResourceBundle resources;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnConnect;
    @FXML
    private TextField txtFldSrvName;
    @FXML
    private TextField txtFldUserName;
    @FXML
    private TextField txtFldSrvPort;
    @FXML
    private Label lblInvalidServerName;
    @FXML
    private Label lblInvalidUserName;
    @FXML
    private Label lblInvalidInvalidPort;

    /**
     * @return true if the user clicked ok, otherwise false
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return a ConnectionViewModel object with the user input
     */
    public ConnectionViewModel getConnectionModel() {
        ConnectionViewModel model = new ConnectionViewModel();

        try {
            model.setPort(Integer.parseInt(txtFldSrvPort.getText()));
        } catch (NumberFormatException ex) {
            // TODO: Handle invalid user data (e.g. show a message or something).
        }

        model.setUser(txtFldUserName.getText());
        model.setHost(txtFldSrvName.getText());

        return model;
    }

    /**
     * Called when user clicks the OK button.
     */
    @FXML
    public void handleOk() {
        if (validateUserInput()) {
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when user clicks the Cancel button;
     */
    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input and displays appropriate error messages.
     *
     * @return true if the input is valid, otherwise false
     */
    private boolean validateUserInput() {
        boolean valid = true;

        if (txtFldSrvPort.getText() == null || txtFldSrvPort.getText().isEmpty()) {
            valid = false;
            lblInvalidInvalidPort.setVisible(true);
        } else {
            try {
                Integer.parseInt(txtFldSrvPort.getText());
                lblInvalidInvalidPort.setVisible(false);
            } catch (NumberFormatException ex) {
                valid = false;
                lblInvalidInvalidPort.setVisible(true);
            }
        }

        if (txtFldSrvName.getText() == null || txtFldSrvName.getText().isEmpty()) {
            valid = false;
            lblInvalidServerName.setVisible(true);
        } else {
            lblInvalidServerName.setVisible(false);
        }

        if (txtFldUserName.getText() == null || txtFldUserName.getText().isEmpty()) {
            valid = false;
            lblInvalidUserName.setVisible(true);
        } else {
            lblInvalidUserName.setVisible(false);
        }

        return valid;
    }

    /**
     * This method is called by the FXMLLoader when initialization is complete.
     */
    @FXML
    private void initialize() {
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert btnConnect != null : "fx:id=\"btnConnect\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert txtFldSrvName != null : "fx:id=\"txtFldSrvName\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert txtFldUserName != null : "fx:id=\"txtFldUserName\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert txtFldSrvPort != null : "fx:id=\"txtFldSrvPort\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert lblInvalidServerName != null : "fx:id=\"lblInvalidServerName\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert lblInvalidUserName != null : "fx:id=\"lblInvalidUserName\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert lblInvalidInvalidPort != null : "fx:id=\"lblInvalidInvalidPort\" was not injected: check your FXML file 'LoginScene.fxml'.";

        lblInvalidServerName.setVisible(false);
        lblInvalidUserName.setVisible(false);
        lblInvalidInvalidPort.setVisible(false);
    }
}
