/**
 * Sample Skeleton for 'MainScene.fxml' Controller Class
 */
package bfh.easychat.client.fx;

import bfh.easychat.client.core.Protocol;
import java.net.URL;
import java.util.ResourceBundle;

import bfh.easychat.client.core.ProtocolListener;
import bfh.easychat.client.viewmodel.ConnectionViewModel;
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

    private final Protocol client;
    private final ObservableList<String> messages = FXCollections.observableArrayList();
    private MainApp mainApp = null;

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

    /**
     * The setter is invoked by the MainApp to give access to its public 
     * functions.
     * 
     * @param mainApp 
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Default constructor used to inject Protocol dependency.
     *
     * @param client An implementation of the Protocol interface.
     */
    public MainSceneController(Protocol client) {
        this.client = client;
    }

    @FXML
    void sendMessage(ActionEvent event) {

        //create a new message object and add it to the Listview
        EasyMessage mymsg = new EasyMessage("USERNAME: " + chatInputText.getText());
        messages.add(mymsg.getMessage());
        chatText.setItems(messages);
        chatInputText.clear();
    }

    //starts the login GUI
    @FXML
    private void openLoginWindow(ActionEvent event) {
        ConnectionViewModel model = new ConnectionViewModel();
        if(mainApp.showConnectDialog(model)){
            // TODO: Initiate connection
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    private void initialize() {
        assert btnSendMsg != null : "fx:id=\"btnSendMsg\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert chatText != null : "fx:id=\"chatText\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert chatInputText != null : "fx:id=\"chatInputText\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert connectBar != null : "fx:id=\"connectBar\" was not injected: check your FXML file 'MainScene.fxml'.";

    }

    private void displayMessage(EasyMessage msg) {
        // your code here...
        messages.add("USERNAME: " + msg.getMessage());
        chatText.setItems(messages);
    }

    @Override
    public void messageRecieved(EasyMessage msg) {
        Platform.runLater(() -> displayMessage(msg));
    }
}
