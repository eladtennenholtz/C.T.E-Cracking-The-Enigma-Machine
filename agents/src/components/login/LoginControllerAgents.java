package components.login;

import components.login.modal.AllTeamsData;
import components.main.AgentsMainController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.ConstantsAgents;
import utils.HttpClientUtilAgents;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;

import static utils.ConstantsAgents.FULL_SERVER_PATH;
import static utils.ConstantsAgents.REFRESH_RATE;

public class LoginControllerAgents implements Initializable {

    AgentsMainController agentsMainController;
    @FXML
    private Button loginButton;
    @FXML
    private TextField loginTextField;
    @FXML
    private Label loginErrorLabel;
    @FXML
    private ChoiceBox<String>teamChoiceBox;
    @FXML
    private Label chosenTeamLabel;
    @FXML
    private TextField tasksTextField;
    @FXML
    private TextField numberOfThreadsTextField;
    @FXML
    private Slider threadsSlider;
    private Timer timerLogin;
    private boolean resForChoiceBox;

    public void loginButtonListener(ActionEvent actionEvent) {
        String userName = loginTextField.getText();
        if (userName.isEmpty()) {
            loginErrorLabel.setText("User Agents name is empty. You can't login with empty user name");
            return;
        }
        for(int i=0;i<userName.length();i++){
            if(userName.charAt(i)=='\n'){
                loginErrorLabel.setText("You cant type the 'enter' key for user name.");
                return;
            }
        }

        String finalUrl = HttpUrl
                .parse(ConstantsAgents.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName).addQueryParameter("type","agents").addQueryParameter("team",chosenTeamLabel.getText())
                .addQueryParameter("numberOfThreads",numberOfThreadsTextField.getText()).addQueryParameter("sizeOfTasks",tasksTextField.getText())
                .build()
                .toString();

        //updateHttpStatusLine("New request is launched for: " + finalUrl);
        HttpClientUtilAgents.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        loginErrorLabel.setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            loginErrorLabel.setText("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        agentsMainController.setCurrentLoginAccount(userName,chosenTeamLabel.getText(),threadsSlider.getValue(),tasksTextField.getText());
                        timerLogin.cancel();
                        agentsMainController.setPageAfterLoginSuccess();
                    });
                }
                response.close();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        teamChoiceBox.setOnAction(this::setTeamsChoiceBox);
        tasksTextField.setOnKeyTyped(this::keyPressedForTasks);
        threadsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                long number = Math.round(threadsSlider.getValue());
                numberOfThreadsTextField.setText(String.valueOf(number));
                //numberOfAgentsTextField.setText(String.valueOf(Math.floor(sliderForAgents.getValue())));
            }
        });
    }

    private void keyPressedForTasks(KeyEvent keyEvent) {
        try {
            int x = Integer.parseInt(keyEvent.getCharacter());
            tasksTextField.editableProperty().set(true);
        } catch (NumberFormatException e) {

            if(keyEvent.getCharacter().equals("\b")){
                tasksTextField.editableProperty().set(true);
                return;
            }
        }
        tasksTextField.editableProperty().set(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");

        try {
            int x = Integer.parseInt(keyEvent.getCharacter());
            tasksTextField.editableProperty().set(true);
            tasksTextField.appendText(keyEvent.getText());


        } catch (NumberFormatException e) {
            if(keyEvent.getCharacter().equals("\b")){
                tasksTextField.editableProperty().set(true);
                return;
            }

            alert.setContentText("Input is not an int value");
            alert.showAndWait();
        }
        if(!chosenTeamLabel.getText().equals("")){
            loginButton.disableProperty().set(false);

        }
    }

    private void setTeamsChoiceBox(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");
        if(!teamChoiceBox.isShowing()){
            return;
        }
        chosenTeamLabel.setText(teamChoiceBox.getValue());
        if(!tasksTextField.getText().equals("")){
            try {
                int x = Integer.parseInt(tasksTextField.getText());

                    loginButton.disableProperty().set(false);

            } catch (NumberFormatException e) {
                alert.setContentText("Tasks have to be an integer number");
                alert.showAndWait();
                    return;
                }
            }
        }


    public void setAlliesMainController(AgentsMainController agentsMainController) {
        this.agentsMainController=agentsMainController;
        loginTextField.setText("");
        loginErrorLabel.setText("");
        numberOfThreadsTextField.setText("1");
    }


    public void getAllDataFromUser() {
        
        startRefresherForTeamsData();

    }

    private void startRefresherForTeamsData() {
        TeamsDataForAgentRefresher dataContestsRefresher = new TeamsDataForAgentRefresher(this::updateChoiceBoxTeams);
         timerLogin = new Timer();
        timerLogin.schedule(dataContestsRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateChoiceBoxTeams(AllTeamsData allTeamsData) {

        ArrayList<String>allTeams=allTeamsData.getAllTeams();
        resForChoiceBox=false;
        Platform.runLater(()->{
            for(int i=0;i<allTeamsData.getAllTeams().size();i++){
                if(!teamChoiceBox.getItems().contains(allTeamsData.getAllTeams().get(i))){
                    resForChoiceBox=true;
                }
            }
            if(resForChoiceBox) {
                teamChoiceBox.getItems().clear();
                teamChoiceBox.getItems().addAll(allTeams);
            }
        });


    }


}
