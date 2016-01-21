package bfh.easychat.client.fx;

import bfh.easychat.client.core.Protocol;

import bfh.easychat.client.core.ProtocolListener;
import bfh.easychat.client.viewmodel.ConnectionViewModel;
import ch.bfh.easychat.common.EasyMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;

public class MainSceneController implements ProtocolListener {

    private final Protocol client;
    private final ObservableList<String> messages = FXCollections.observableArrayList();
    private MainApp mainApp = null;
    private ConnectionViewModel connectionModel = null;

    @FXML
    private Button btnSendMsg;
    @FXML
    private Button btnConnectTo;
    @FXML
    private Button btnDisconnectFrom;
    @FXML
    private Button btnReconnect;
    @FXML
    private ListView<String> chatText;
    @FXML
    private TextArea chatInputText;

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
        client.setProtocolListener(this);
    }

    /**
     * This method is called by the FXMLLoader when initialization is complete.
     */
    @FXML
    private void initialize() {
        assert btnSendMsg != null : "fx:id=\"btnSendMsg\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert chatText != null : "fx:id=\"chatText\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert chatInputText != null : "fx:id=\"chatInputText\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnReconnect != null : "fx:id=\"btnReconnect\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnDisconnectFrom != null : "fx:id=\"btnDisconnectFrom\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnConnectTo != null : "fx:id=\"btnDisconnectFrom\" was not injected: check your FXML file 'MainScene.fxml'.";

        // configure controls
        btnDisconnectFrom.setDisable(true);
        btnSendMsg.setDisable(true);
        chatInputText.setDisable(true);
        btnReconnect.setVisible(false);

        chatText.setCellFactory((ListView<String> param) -> {
            return new ListCell<String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    setWrapText(true);
                    setText(item);
                    super.updateItem(item, empty);
                }
            };
        });

        chatText.setItems(messages);
    }

    @FXML
    void handleSend(ActionEvent event) {
        //condition implementieren
        //create a new message object and add it to the Listview
        EasyMessage mymsg = new EasyMessage(chatInputText.getText(), connectionModel.getUser());
        messages.add("Me: " + mymsg.getMessage());
        client.sendMessage(mymsg);
        chatInputText.clear();
    }

    /**
     * Open the login dialog and connect to the server.
     *
     * @param event
     */
    @FXML
    private void handleConnect(ActionEvent event) {
        connectionModel = new ConnectionViewModel();
        if (mainApp.showConnectDialog(connectionModel)) {
            btnReconnect.setVisible(false);
            boolean success = client.connect(
                    connectionModel.getHost(),
                    connectionModel.getPort(),
                    connectionModel.getUser());
            if (!success) {
                messages.clear();
                messages.add("Connection to " + connectionModel.getHost() + ":"
                        + connectionModel.getPort() + " failed.");
                btnConnectTo.setDisable(false);
                btnDisconnectFrom.setDisable(true);
                chatInputText.setDisable(true);
            } else {
                messages.add("Successfully connected to " + connectionModel.getHost() + ":"
                        + connectionModel.getPort() + ".");
                btnConnectTo.setDisable(true);
                btnDisconnectFrom.setDisable(false);
                btnSendMsg.setDisable(false);
                chatInputText.setDisable(false);
            }
        }
    }

    /**
     * Disconnect from the server.
     *
     * @param event
     */
    @FXML
    private void handleDisconnect(ActionEvent event) {
        client.disconnect();
        btnConnectTo.setDisable(false);
        btnDisconnectFrom.setDisable(true);
        btnSendMsg.setDisable(true);
        chatInputText.setDisable(true);
        messages.clear();
        messages.add("Successfully disconnected from server");
    }

    /**
     * Try reconnect to the server.
     *
     * @param event
     */
    @FXML
    private void handleReconnect(ActionEvent event) {
        if(connectionModel == null){
            return;
        }
        
        if (client.connect(connectionModel.getHost(), connectionModel.getPort(), connectionModel.getUser())) {
            btnReconnect.setVisible(false);
            btnConnectTo.setDisable(true);
            btnDisconnectFrom.setDisable(false);
            messages.add("Reconnect successfull.");
        }else{
            messages.add("Reconnect failed.");
        }
    }

    /**
     * Handles the connection lost event from the Protocol.
     */
    private void handleConnectionLost() {
        btnReconnect.setVisible(true);
    }

    /**
     * Show chat message.
     *
     * @param msg the chat message to show
     */
    private void displayMessage(EasyMessage msg) {
        messages.add(String.format("%s: %s",
                msg.getSender(),
                msg.getMessage()));
    }

    /**
     * Invoked by the Protocol when a message was received.
     *
     * @param msg
     */
    @Override
    public void messageRecieved(EasyMessage msg) {
        Platform.runLater(() -> displayMessage(msg));
    }

    /**
     * Invoked by the Protocol when the connection to the server was lost.
     */
    @Override
    public void connectionLost() {
        Platform.runLater(() -> handleConnectionLost());
    }
}
