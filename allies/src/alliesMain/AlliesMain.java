package alliesMain;

import components.main.AlliesMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static utils.ConstantsAllies.LOGIN_PAGE_FXML_RESOURCE_LOCATION;
import static utils.ConstantsAllies.MAIN_PAGE_FXML_RESOURCE_LOCATION;

public class AlliesMain extends Application {
    private AlliesMainController alliesMainController;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Allies");

        URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            alliesMainController=fxmlLoader.getController();
            Scene scene = new Scene(root, 1100, 800);
            alliesMainController.setPrimaryStage(primaryStage);
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
