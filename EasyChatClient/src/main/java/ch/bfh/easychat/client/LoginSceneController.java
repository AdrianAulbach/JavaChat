/**
 * Sample Skeleton for 'LoginScene.fxml' Controller Class
 */

package ch.bfh.easychat.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginSceneController {
	// Load the fxml file and create a new stage for the popup
	//FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/LoginScene.fxml"));
	

    @FXML // fx:id="subAnchorPane"
    private AnchorPane subAnchorPane; // Value injected by FXMLLoader
    
    Stage loginStage;
  	Scene scene;
    
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
    
    //closes the login window after clicking on the login button
    @FXML
    void loginUser(ActionEvent event) {
    		loginStage.close();
    }
    //opens the login window    
    public void startLoginWindow() {
    	
    	Stage loginStage = new Stage();
    	try {
			subAnchorPane = (AnchorPane)FXMLLoader.load(getClass().getResource("/fxml/LoginScene.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Scene scene = new Scene(subAnchorPane);
    	loginStage.setTitle("Please log in");
    	loginStage.setScene(scene);
    	loginStage.show(); 	
    	
    }
    


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert txtFldSrvName != null : "fx:id=\"txtFldSrvName\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert txtFldUserName != null : "fx:id=\"txtFldUserName\" was not injected: check your FXML file 'LoginScene.fxml'.";
        assert txtFldSrvPort != null : "fx:id=\"txtFldSrvPort\" was not injected: check your FXML file 'LoginScene.fxml'.";

    }
}
