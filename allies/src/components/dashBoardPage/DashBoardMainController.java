package components.dashBoardPage;

import components.main.AlliesMainController;
import components.main.model.AllContestsData;
import components.main.model.AllTeamsAgentsData;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;
import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class DashBoardMainController implements Initializable, Closeable {

    private AlliesMainController alliesMainController;

    @FXML
    private ChoiceBox<String>choiceBoxContest;

    @FXML
    private Label chosenContestLabel;
    @FXML
    private Label isReadyLabel;

    @FXML
    private HBox hBoxTableView;
    @FXML
    private HBox hBoxTeamsAgents;
    @FXML
    private Button readyToRegisterButton;

    @FXML
    private TableView<AllContestDataForTableView>contestsDataTableView;
    @FXML
    private TableColumn colBattle;
    @FXML
    private TableColumn colUBoat;
    @FXML
    private TableColumn colStatus;
    @FXML
    private TableColumn colAlliesMax;
    @FXML
    private TableColumn colLevel;
    @FXML
    private TableColumn colAlliesLogged;
    @FXML
    private TableView<AllTeamsAgentsDataForTableView>teamsAgentsTableView;
    @FXML
    private TableColumn colName;
    @FXML
     private TableColumn colThreads;
    @FXML
    private TableColumn colTaskSize;



    SimpleBooleanProperty readyToRegisterButtonProperty;

    public DashBoardMainController(){
        readyToRegisterButtonProperty=new SimpleBooleanProperty();
    }

    public void setMainController(AlliesMainController alliesMainController){
        this.alliesMainController=alliesMainController;
        readyToRegisterButton.disableProperty().bind(readyToRegisterButtonProperty.not());
        chosenContestLabel.setText("");
        colBattle.setCellValueFactory(
                new PropertyValueFactory<AllContestDataForTableView, String>("colBattle"));

        colUBoat.setCellValueFactory(
                new PropertyValueFactory<AllContestDataForTableView, String>("colUBoat"));


        colStatus.setCellValueFactory(
                new PropertyValueFactory<AllContestDataForTableView, String>("colStatus"));


        colLevel.setCellValueFactory(new PropertyValueFactory<AllContestDataForTableView,String>("colLevel"));

        colAlliesMax.setCellValueFactory(new PropertyValueFactory<AllContestDataForTableView,String>("colAlliesMax"));

        colAlliesLogged.setCellValueFactory(new PropertyValueFactory<AllContestDataForTableView,String>("colAlliesLogged"));


        colName.setCellValueFactory(
                new PropertyValueFactory<AllTeamsAgentsDataForTableView,String>("colName"));

        colThreads.setCellValueFactory(new PropertyValueFactory<AllContestDataForTableView,String>("colThreads"));
        colTaskSize.setCellValueFactory(new PropertyValueFactory<AllContestDataForTableView,String>("colTaskSize"));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        choiceBoxContest.setOnAction(this::setContestChoiceBox);
    }


    public void showContestsData(AllContestsData contestsData) {

        Platform.runLater(()->{
            //hBoxContestsData.getChildren().clear();
            choiceBoxContest.getItems().clear();
            contestsDataTableView.getItems().clear();
            if(contestsData.isEmpty()){
                return;
            }

            for(int i=0;i<contestsData.getBattleFieldsName().size();i++) {
            StringBuilder status=new StringBuilder();
            if(contestsData.getStatuses().get(i)) {
            status.append("Active");
            }else{
              status.append("Not active");
            }
            choiceBoxContest.getItems().add(contestsData.getBattleFieldsName().get(i));

            ObservableList<AllContestDataForTableView> data =
                    FXCollections.observableArrayList(
                            new AllContestDataForTableView(contestsData.getBattleFieldsName().get(i), contestsData.getUBoatsName().get(i), status.toString(),contestsData.getLevels().get(i),String.valueOf(contestsData.getAlliesMax().get(i)),String.valueOf(contestsData.getAlliesLogged().get(i)))
                    );

            contestsDataTableView.getItems().addAll(data);

            VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.getChildren().addAll(contestsDataTableView);
            hBoxTableView.getChildren().addAll(vbox);
        }
        });

    }

    private void setContestChoiceBox(ActionEvent actionEvent) {

        if(!choiceBoxContest.isShowing()){
            return;
        }
        chosenContestLabel.setText(choiceBoxContest.getValue());
        readyToRegisterButtonProperty.set(true);
    }




    @Override
    public void close() throws IOException {

    }

    public void readyToRegisterButtonListener(ActionEvent actionEvent) {

        this.alliesMainController.readyForRegistrationClicked(chosenContestLabel.getText());
        readyToRegisterButtonProperty.set(false);
        choiceBoxContest.disableProperty().set(true);
    }

    public void setAnswerAboutReadyForReg(boolean result,boolean extra) {

        Platform.runLater(()->{
            if(result){
                isReadyLabel.setText("Registration succeeded!");
                isReadyLabel.setTextFill(Color.GREEN);

            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if(extra==true) {
                    alert.setHeaderText("Reason:");
                    alert.setContentText("This contest cannot be registered because it has just been used and the user hasn't logged out. ");
                    alert.showAndWait();
                    isReadyLabel.setText("");
                }else{
                    isReadyLabel.setText("This contest is full!");
                    isReadyLabel.setTextFill(Color.RED);
                }


                //alert.setTitle("Error");
                //alert.setHeaderText("Reason:");
                //alert.setContentText("This competition is full!");
                //alert.showAndWait();
                readyToRegisterButtonProperty.set(true);
                choiceBoxContest.disableProperty().set(false);
            }
        });
    }

    public void showTeamsAgentsData(AllTeamsAgentsData allTeamsAgentsData) {
        Platform.runLater(()->{
            teamsAgentsTableView.getItems().clear();
            for(int i=0;i<allTeamsAgentsData.getNamesOfAgents().size();i++) {
                ObservableList<AllTeamsAgentsDataForTableView> dataTeam =
                        FXCollections.observableArrayList(
                                new AllTeamsAgentsDataForTableView(allTeamsAgentsData.getNamesOfAgents().get(i), String.valueOf(allTeamsAgentsData.getNumberOfThreads().get(i)), String.valueOf(allTeamsAgentsData.getSizeOfTasks().get(i)))
                        );

                teamsAgentsTableView.getItems().addAll(dataTeam);

                VBox vbox = new VBox();
                vbox.setSpacing(5);
                vbox.getChildren().addAll(teamsAgentsTableView);
                hBoxTeamsAgents.getChildren().addAll(vbox);
            }
        });

    }

    public void finishWasClickedUpdateData() {
        isReadyLabel.setText("");
        choiceBoxContest.getItems().clear();
        choiceBoxContest.disableProperty().set(false);
        chosenContestLabel.setText("");
        teamsAgentsTableView.getItems().clear();
        contestsDataTableView.getItems().clear();
    }
}
