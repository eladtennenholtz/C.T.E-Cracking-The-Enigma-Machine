package uiMain;

import components.main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class UiMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //load main fxml
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/components/main/uBoatMain.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setPrimaryStage(primaryStage);
        mainController.setEngineMachine();
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {

        launch(args);
    }
}
