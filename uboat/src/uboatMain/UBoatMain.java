package uboatMain;

import components.main.UBoatMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static utils.Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION;

public class UBoatMain extends Application {

    private UBoatMainController uboatMainController;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("UBoat");

       URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            uboatMainController=fxmlLoader.getController();
            Scene scene = new Scene(root, 1000, 800);
            uboatMainController.setPrimaryStage(primaryStage);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        //HttpClientUtil.shutdown();
        //chatAppMainController.close();


    }

    public static void main(String[] args) {
        launch(args);
    }





}
