package bfh.easychat.client.fx;

import bfh.easychat.client.core.Protocol;
import bfh.easychat.client.serverclient.ProtocolImpl;
import bfh.easychat.client.viewmodel.ConnectionViewModel;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());
    private static final Protocol CLIENT = new ProtocolImpl();

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/MainScene.fxml"));
            loader.setControllerFactory(instantiatedClass
                    -> getInstance(instantiatedClass));

            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Easy Chat Client");
            primaryStage.show();

            MainSceneController controller = loader.getController();
            controller.setMainApp(this);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public boolean showConnectDialog(ConnectionViewModel model) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/LoginScene.fxml"));
            AnchorPane loginScene = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Connect to a Server");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(loginScene);
            dialogStage.setScene(scene);

            LoginSceneController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            
            dialogStage.showAndWait();
            
            model.setHost(controller.getConnectionModel().getHost());
            model.setPort(controller.getConnectionModel().getPort());
            model.setUser(controller.getConnectionModel().getUser());
            
            return controller.isOkClicked();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    // Simple dependency resolution
    private Object getInstance(Class<?> clazz) {

        try {
            Constructor<?> constructor = clazz.getConstructor(Protocol.class);
            return constructor.newInstance(CLIENT);
        } catch (Exception ex) {
            // ... ignoring exception is fine here
        }

        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}