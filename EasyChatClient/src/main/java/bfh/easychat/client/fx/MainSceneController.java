/**
 * Sample Skeleton for 'MainScene.fxml' Controller Class
 */

package bfh.easychat.client.fx;

import java.net.URL;
import java.util.ResourceBundle;

import bfh.easychat.client.core.ProtocolListener;
import ch.bfh.easychat.common.EasyMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;


public class MainSceneController implements ProtocolListener {
		
	//class for testing Messages, can be deleted after client is implemented
	public class Message {
		String userName;
		String txt;
		
		public Message(String txt, String userName) {
			this.txt = txt;
			this.userName = userName;
		}
		
		public String getTxt() {
			return txt.toString();
		}
		
		public String getUserName() {
			return userName.toString();
		}
		
		public void setTxt(String myTxt) {
			txt = myTxt;
		}
		
		public void setUserName(String myName) {
			userName = myName;
		}
		
		public String getMessage() {
			return "" + txt +" "+ userName + "";
		}
	}

	private ObservableList<String> messages = FXCollections.observableArrayList();  
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML // fx:id="MainAnchorPane"
    private AnchorPane mainAnchorPane; // Value injected by FXMLLoader
    
    @FXML // fx:id="btnSendMsg"
    private Button btnSendMsg; // Value injected by FXMLLoader

    @FXML // fx:id="chatText"
    private ListView<String> chatText; // Value injected by FXMLLoader

    @FXML // fx:id="chatInputText"
    private TextArea chatInputText; // Value injected by FXMLLoader

    @FXML // fx:id="connectBar"
    private Menu connectBar; // Value injected by FXMLLoader
    
    @FXML
    private MenuItem connectMenuitem;

    @FXML
    void sendMessage(ActionEvent event) {
    	
    	//create a new message object and add it to the Listview
    	Message mymsg = new Message("USERNAME:",chatInputText.getText());
    	messages.add(mymsg.getMessage());
    	chatText.setItems(messages);
    	chatInputText.clear();
    	
    }
    
    //starts the login GUI
    @FXML
    private void openLoginWindow(ActionEvent event) {
    	
    	//Create a new Object of LoginSceneController and call the method to start the login GUI
    	LoginSceneController gui = new LoginSceneController();
    	gui.startLoginWindow();

    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    private void initialize() {
        assert btnSendMsg != null : "fx:id=\"btnSendMsg\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert chatText != null : "fx:id=\"chatText\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert chatInputText != null : "fx:id=\"chatInputText\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert connectBar != null : "fx:id=\"connectBar\" was not injected: check your FXML file 'MainScene.fxml'.";

    }

    private void displayMessage(EasyMessage msg){
    	// your code here...
    }
    
	@Override
	public void messageRecieved(EasyMessage msg) {
		Platform.runLater(() -> displayMessage(msg));
	}
}
