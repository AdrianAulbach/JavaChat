/**
 * Sample Skeleton for 'MainScene.fxml' Controller Class
 */

package ch.bfh.easychat.client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;


public class MainSceneController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML // fx:id="MainAnchorPane"
    private AnchorPane mainAnchorPane; // Value injected by FXMLLoader
    
    @FXML // fx:id="btnSendMsg"
    private Button btnSendMsg; // Value injected by FXMLLoader

    @FXML // fx:id="chatText"
    private ListView<?> chatText; // Value injected by FXMLLoader

    @FXML // fx:id="chatInputText"
    private TextArea chatInputText; // Value injected by FXMLLoader

    @FXML // fx:id="connectBar"
    private Menu connectBar; // Value injected by FXMLLoader
    
    @FXML
    private MenuItem connectMenuitem;

    @FXML
    void sendMessage(ActionEvent event) {
    	System.out.println("Test");
    }
    
    //starts the login GUI
    @FXML
    private void openLoginWindow(ActionEvent event) {
    	LoginSceneController gui = new LoginSceneController();
    	gui.startLoginWindow();

    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnSendMsg != null : "fx:id=\"btnSendMsg\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert chatText != null : "fx:id=\"chatText\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert chatInputText != null : "fx:id=\"chatInputText\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert connectBar != null : "fx:id=\"connectBar\" was not injected: check your FXML file 'MainScene.fxml'.";

    }
}
