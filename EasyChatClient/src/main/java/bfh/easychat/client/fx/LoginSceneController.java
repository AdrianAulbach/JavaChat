/**
 * Sample Skeleton for 'LoginScene.fxml' Controller Class
 */
package bfh.easychat.client.fx;

import java.net.URL;
import java.util.ResourceBundle;

import bfh.easychat.client.serverclient.ProtocolImpl;
import bfh.easychat.client.viewmodel.ConnectionViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginSceneController extends ProtocolImpl {

    private Stage dialogStage;
    private boolean okClicked;

    @FXML // fx:id="subAnchorPane"
    private AnchorPane subAnchorPane; // Value injected by FXMLLoader

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnLogin"
    private Button btnLogin; // Value injected by FXMLLoader

    @FXML // fx:id="txtFldSrvName"
    private TextField txtFldSrvName; // Value injected by FXMLLoader

    @FXML // fx:id="txtFldUserName"
    private TextField txtFldUserName; // Value injected by FXMLLoader

    @FXML // fx:id="txtFldSrvPort"
    private TextField txtFldSrvPort; // Value injected by FXMLLoader

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
    
    public ConnectionViewModel getConnectionModel(){
        ConnectionViewModel model = new ConnectionViewModel();
        
        try{
            model.setPort(Integer.parseInt(txtFldSrvPort.getText()));
        }catch(NumberFormatException ex){
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
        // TODO: Validate user input (e.g. no characters in port field allowed)!
        okClicked = true;
        dialogStage.close();
    }

    /**
     * Called when user clicks the Cancel button;
     */
    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert txtFldSrvName != null : "fx:id=\"txtFldSrvName\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert txtFldUserName != null : "fx:id=\"txtFldUserName\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert txtFldSrvPort != null : "fx:id=\"txtFldSrvPort\" was not injected: check your FXML file 'LoginScene.fxml'.";
    }
}
