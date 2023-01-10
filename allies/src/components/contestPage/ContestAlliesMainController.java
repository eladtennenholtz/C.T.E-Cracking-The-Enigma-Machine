package components.contestPage;

import components.dashBoardPage.AllContestDataForTableView;
import components.main.AlliesMainController;
import components.main.model.AllContestsData;
import components.main.model.AllEncryptedStringAndStartContestData;
import components.main.model.AllTeamsDataForContest;
import data.DataAgentsProgressToAllies;
import data.DataCandidatesToAllies;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ContestAlliesMainController implements Initializable, Closeable {

    private AlliesMainController alliesMainController;
    private String currentBattle;
    private String levelOfCurrentContest;
    private int sizeOfTask;
    private int defaultSizeOfTask;
    @FXML
    private HBox hBoxContestDataPageContest;
    @FXML
    private TableView<AllContestDataForTableView> contestDataTableViewContestPage;
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
    private Button readyToStartCompetitionButton;
    @FXML
    private TextField taskSizeTextField;
    @FXML
    private Label readyToStartContestLabel;
    @FXML
    private HBox hBoxContestsTeamsPageContest;
    @FXML
    private TableView<AllTeamsDataForContestTableView> contestsTeamsTableViewPageContest;

    @FXML
    private TableColumn colTeam;
    @FXML
    private TableColumn colAgents;
    @FXML
    private TableColumn colTaskSize;
    @FXML
    private Label statusOfContestAllies;
    @FXML
    private Label encryptedStringForContestLabel;
    @FXML
    private TableView<AllCandidatesForAlliesTableView>tableViewTeamCandidates;
    @FXML
    private TableColumn colDecrypt;
    @FXML
    private TableColumn colAgentCandidate;
    @FXML
    private TableColumn colCode;
    @FXML
    private TableView<AllTeamsAgentsProgressTableView>tableViewAgentsDataProgress;
    @FXML
    private TableColumn colAgentProgress;
    @FXML
    private TableColumn colTaskDone;
    @FXML
    private TableColumn colTaskRemain;
    @FXML
    private TableColumn colCandidates;
    @FXML
    private Label totalTasksExistLabel;
    @FXML
    private Label totalTasksProducedLabel;
    @FXML
    private Label totalAmountOfTasksFinishedAllAgentsLabel;
    @FXML
    private Label winnerOfContest;
    @FXML
    private Button finishedTheContestButton;
    private int totalAmountOfTaskDoneInDmAllAgents;
    private final ObservableList<AllCandidatesForAlliesTableView> data=FXCollections.observableArrayList();
   // private final ObservableList<AllTeamsAgentsProgressTableView> dataForAgentsProgress=FXCollections.observableArrayList();


    public ContestAlliesMainController(){

        totalAmountOfTaskDoneInDmAllAgents=0;
    }
    public void setMainController(AlliesMainController alliesMainController) {
        this.alliesMainController=alliesMainController;
        finishedTheContestButton.disableProperty().set(true);
        //contestDataTableViewContestPage.disableProperty().set(true);
        colBattle.setCellValueFactory(
                new PropertyValueFactory<AllContestDataForTableView, String>("colBattle"));

        colUBoat.setCellValueFactory(
                new PropertyValueFactory<AllContestDataForTableView, String>("colUBoat"));


        colStatus.setCellValueFactory(
                new PropertyValueFactory<AllContestDataForTableView, String>("colStatus"));


        colLevel.setCellValueFactory(new PropertyValueFactory<AllContestDataForTableView,String>("colLevel"));

        colAlliesMax.setCellValueFactory(new PropertyValueFactory<AllContestDataForTableView,String>("colAlliesMax"));

        colAlliesLogged.setCellValueFactory(new PropertyValueFactory<AllContestDataForTableView,String>("colAlliesLogged"));
        readyToStartCompetitionButton.disableProperty().set(true);
        taskSizeTextField.editableProperty().set(false);
        taskSizeTextField.disableProperty().set(true);
        readyToStartContestLabel.setText("");
        colTeam.setCellValueFactory(
                new PropertyValueFactory<AllTeamsDataForContestTableView, String>("colTeam"));
        colAgents.setCellValueFactory(
                new PropertyValueFactory<AllTeamsDataForContestTableView, String>("colAgents"));
        colTaskSize.setCellValueFactory(
                new PropertyValueFactory<AllTeamsDataForContestTableView, String>("colTaskSize"));
        colDecrypt.setCellValueFactory(
                new PropertyValueFactory<AllCandidatesForAlliesTableView, String>("colDecrypt"));

        colAgentCandidate.setCellValueFactory(
                new PropertyValueFactory<AllCandidatesForAlliesTableView, String>("colAgentCandidate"));


        colCode.setCellValueFactory(
                new PropertyValueFactory<AllCandidatesForAlliesTableView, String>("colCode"));



        colAgentProgress.setCellValueFactory(
                new PropertyValueFactory<AllTeamsAgentsProgressTableView, String>("colAgentProgress"));
        colTaskDone.setCellValueFactory(
                new PropertyValueFactory<AllTeamsAgentsProgressTableView, String>("colTaskDone"));

        colTaskRemain.setCellValueFactory(
                new PropertyValueFactory<AllTeamsAgentsProgressTableView, String>("colTaskRemain"));


        colCandidates.setCellValueFactory(
                new PropertyValueFactory<AllTeamsAgentsProgressTableView, String>("colCandidates"));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        taskSizeTextField.setOnKeyTyped(this::keyPressedForTasks);
    }

    private void keyPressedForTasks(KeyEvent keyEvent) {

        try {
            int x = Integer.parseInt(keyEvent.getCharacter());
            taskSizeTextField.editableProperty().set(true);
        } catch (NumberFormatException e) {

            if(keyEvent.getCharacter().equals("\b")){
                taskSizeTextField.editableProperty().set(true);
                if(taskSizeTextField.getText().equals("")){
                    readyToStartCompetitionButton.disableProperty().set(true);
                }
                return;
            }
        }
        taskSizeTextField.editableProperty().set(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");

        try {
            int x = Integer.parseInt(keyEvent.getCharacter());
            taskSizeTextField.editableProperty().set(true);
            taskSizeTextField.appendText(keyEvent.getText());
            readyToStartCompetitionButton.disableProperty().set(false);

        } catch (NumberFormatException e) {
            if(keyEvent.getCharacter().equals("\b")){
                taskSizeTextField.editableProperty().set(true);
                return;
            }

            alert.setContentText("Input is not an int value");
            alert.showAndWait();
        }

        if(!taskSizeTextField.getText().equals("")){
            readyToStartCompetitionButton.disableProperty().set(false);
        }
        //if(!chosenTeamLabel.getText().equals("")){
          //  loginButton.disableProperty().set(false);

        //}
    }

    @Override
    public void close() throws IOException {

    }

    public void startContestPage(String battle,AllContestsData allContestsData) {
        //contestDataTableViewContestPage.disableProperty().set(false);

        Platform.runLater(()->{
            taskSizeTextField.disableProperty().set(false);
            contestDataTableViewContestPage.getItems().clear();
            int index=0;
            for(int i=0;i<allContestsData.getBattleFieldsName().size();i++){
                if(allContestsData.getBattleFieldsName().get(i).equals(battle)){
                    index=i;
                }
            }
            StringBuilder status=new StringBuilder();
            if(allContestsData.getStatuses().get(index)) {
                status.append("Active");
            }else{
                status.append("Not active");
            }
            ObservableList<AllContestDataForTableView> data =
                    FXCollections.observableArrayList(
                            new AllContestDataForTableView(allContestsData.getBattleFieldsName().get(index), allContestsData.getUBoatsName().get(index), status.toString(),allContestsData.getLevels().get(index),String.valueOf(allContestsData.getAlliesMax().get(index)),String.valueOf(allContestsData.getAlliesLogged().get(index)))
                    );

            levelOfCurrentContest=allContestsData.getLevels().get(index);
            contestDataTableViewContestPage.getItems().addAll(data);
            currentBattle=battle;

            VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.getChildren().addAll(contestDataTableViewContestPage);
            hBoxContestDataPageContest.getChildren().addAll(vbox);


        });
    }

    public void taskSizeTextFieldListener(ActionEvent actionEvent) {

       // if(!taskSizeTextField.textProperty().isEmpty().getValue()){
            //startBruteForceButtonProperty.set(true);
           // readyToStartCompetitionButton.disableProperty().set(false);
        //}
    }

    public void showTotalTasksListener(ActionEvent actionEvent) {
    }

    public void readyToStartCompetitionButtonListener(ActionEvent actionEvent) {

        sizeOfTask=Integer.parseInt(taskSizeTextField.getText());
        if(Integer.parseInt(taskSizeTextField.getText())>100){
            defaultSizeOfTask=100;
        }else{
            defaultSizeOfTask=sizeOfTask;
        }
        this.alliesMainController.readyToStartCompetitionClicked(sizeOfTask);
        readyToStartCompetitionButton.disableProperty().set(true);
        taskSizeTextField.editableProperty().set(false);
    }

    public void resultFromReadyToStartCompetition(boolean res,int code) {
        Platform.runLater(()->{
            if(res){
                readyToStartContestLabel.setText("Ready to start!");

            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Reason:");
                if(code==409) {
                    alert.setContentText("There has to be at least one agent to participate in the contest. ");
                    alert.showAndWait();
                }else{
                    alert.setContentText("The size of the task is bigger than the total configurations possible." +
                            "please try again.");
                    alert.showAndWait();
                }
                readyToStartCompetitionButton.disableProperty().set(false);
            }
        });

    }

    public void showAllTeamsDataForContest(AllTeamsDataForContest allTeamsDataForContest) {
        Platform.runLater(()->{
            //hBoxContestsData.getChildren().clear();

            contestsTeamsTableViewPageContest.getItems().clear();

            for(int i=0;i<allTeamsDataForContest.getTeamsName().size();i++) {

                ObservableList<AllTeamsDataForContestTableView> data =
                        FXCollections.observableArrayList(
                                new AllTeamsDataForContestTableView(allTeamsDataForContest.getTeamsName().get(i),String.valueOf(allTeamsDataForContest.getNumberOfAgents().get(i)),String.valueOf(allTeamsDataForContest.getSizeOfTasks().get(i)))
                        );

                contestsTeamsTableViewPageContest.getItems().addAll(data);

                VBox vbox = new VBox();
                vbox.setSpacing(5);
                vbox.getChildren().addAll(contestsTeamsTableViewPageContest);
                hBoxContestsTeamsPageContest.getChildren().addAll(vbox);
            }
        });
    }

    public void updateEncryptedStringAndIfContestStarted(AllEncryptedStringAndStartContestData allEncryptedStringAndStartContestData) {

        encryptedStringForContestLabel.setText(allEncryptedStringAndStartContestData.getEncryptedString());
        if(allEncryptedStringAndStartContestData.getContestStart().equals("false")){
            statusOfContestAllies.setTextFill(Color.RED);
            statusOfContestAllies.setText("Contest hasn't started yet.");
        }else{
            statusOfContestAllies.setTextFill(Color.GREEN);
            statusOfContestAllies.setText("Contest started!");
        }

    }

    public void showAllCandidatesDataAllies(DataCandidatesToAllies[] dataCandidatesToAllies) {

        Platform.runLater(() -> {
            //AllCandidatesToUBoatTableView[]allCandidatesToUBoatTableViewsArray=new AllCandidatesToUBoatTableView[allCandidatesForUBoat.length];
            for(int i=0;i<dataCandidatesToAllies.length;i++){
                if(dataCandidatesToAllies[i]!=null){
                    String config=convertToTheConfig(dataCandidatesToAllies[i].getConfiguration());
                    data.add(new AllCandidatesForAlliesTableView(dataCandidatesToAllies[i].getDecrypt(),dataCandidatesToAllies[i].getAgentName(),config));
                   // if(config.equals(winningConfiguration)){
                     //   this.uboatMainController.foundTheWinner(allCandidatesForUBoat[i].getTeamName());
                       // break;
                    //}
                    //data.add(new AllCandidatesToUBoatTableView(allCandidatesForUBoat[i].getDecrypt(),allCandidatesForUBoat[i].getTeamName(),convertToTheConfig(allCandidatesForUBoat[i].getConfiguration())));
                }

            }
            tableViewTeamCandidates.setItems(data);
            colDecrypt.setCellValueFactory(
                    new PropertyValueFactory<AllCandidatesForAlliesTableView, String>("colDecrypt"));

            colTeam.setCellValueFactory(
                    new PropertyValueFactory<AllCandidatesForAlliesTableView, String>("colAgentCandidate"));


            colCode.setCellValueFactory(
                    new PropertyValueFactory<AllCandidatesForAlliesTableView, String>("colCode"));

        });


    }
    public String convertToTheConfig(String configuration){

        String[]result=configuration.split("\t");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("<"+result[0]+">"+"<"+result[1]+">"+"<"+result[2]+">");
        return stringBuilder.toString();
    }

    public void showProgressOfAgentsData(DataAgentsProgressToAllies[] dataAgentsProgressToAllies) {

        Platform.runLater(() -> {
            ObservableList<AllTeamsAgentsProgressTableView> dataForAgentsProgress=FXCollections.observableArrayList();
            int tasksToSumUp=0;
            //AllCandidatesToUBoatTableView[]allCandidatesToUBoatTableViewsArray=new AllCandidatesToUBoatTableView[allCandidatesForUBoat.length];
            for(int i=0;i<dataAgentsProgressToAllies.length;i++){
                if(dataAgentsProgressToAllies[i]!=null){

                    dataForAgentsProgress.add(new AllTeamsAgentsProgressTableView(dataAgentsProgressToAllies[i].getAgentName(),String.valueOf(dataAgentsProgressToAllies[i].getTasksDone()),String.valueOf(dataAgentsProgressToAllies[i].getTasksRemain()),String.valueOf(dataAgentsProgressToAllies[i].getCandidates())));
                    // if(config.equals(winningConfiguration)){
                    //   this.uboatMainController.foundTheWinner(allCandidatesForUBoat[i].getTeamName());
                    // break;
                    //}
                    //data.add(new AllCandidatesToUBoatTableView(allCandidatesForUBoat[i].getDecrypt(),allCandidatesForUBoat[i].getTeamName(),convertToTheConfig(allCandidatesForUBoat[i].getConfiguration())));
                    tasksToSumUp+=dataAgentsProgressToAllies[i].getTasksDone();
                    //totalAmountOfTaskDoneInDmAllAgents+=dataAgentsProgressToAllies[i].getTasksDone();
                    //totalAmountOfTasksFinishedAllAgentsLabel.setText(String.valueOf(totalAmountOfTaskDoneInDmAllAgents));
                }

            }
            totalAmountOfTasksFinishedAllAgentsLabel.setText(String.valueOf(tasksToSumUp));
            tableViewAgentsDataProgress.setItems(dataForAgentsProgress);
            colAgentProgress.setCellValueFactory(
                    new PropertyValueFactory<AllTeamsAgentsProgressTableView, String>("colAgentProgress"));
            colTaskDone.setCellValueFactory(
                    new PropertyValueFactory<AllTeamsAgentsProgressTableView, String>("colTaskDone"));

            colTaskRemain.setCellValueFactory(
                    new PropertyValueFactory<AllTeamsAgentsProgressTableView, String>("colTaskRemain"));


            colCandidates.setCellValueFactory(
                    new PropertyValueFactory<AllTeamsAgentsProgressTableView, String>("colCandidates"));


        });



    }

    public void showProgressOfTasksOfDm(Integer[] dataOfTasksOfDM) {

        Platform.runLater(()->{
            totalTasksProducedLabel.setText(dataOfTasksOfDM[0].toString());
            totalTasksExistLabel.setText(dataOfTasksOfDM[1].toString());


        });
    }

    public void showTheWinnerOfContest(String winner, String currentUser) {

        Platform.runLater(()->{
            if(winner.equals(currentUser)){
                winnerOfContest.setText(currentUser+" you won!!");
            }else{
                winnerOfContest.setText(winner);
            }
            statusOfContestAllies.setText("Contest finished.");
            finishedTheContestButton.disableProperty().set(false);
        });

    }

    public void finishedTheContestButtonListener(ActionEvent actionEvent) {
        contestDataTableViewContestPage.getItems().clear();
        contestsTeamsTableViewPageContest.getItems().clear();
        statusOfContestAllies.setText("");
        encryptedStringForContestLabel.setText("");
        taskSizeTextField.setText("");
        winnerOfContest.setText("");
        tableViewAgentsDataProgress.getItems().clear();
        tableViewTeamCandidates.getItems().clear();
        totalAmountOfTasksFinishedAllAgentsLabel.setText("");
        totalTasksProducedLabel.setText("");
        totalTasksExistLabel.setText("");
        finishedTheContestButton.disableProperty().set(true);
        readyToStartContestLabel.setText("");
        this.alliesMainController.finishedButtonClicked();
    }
}
