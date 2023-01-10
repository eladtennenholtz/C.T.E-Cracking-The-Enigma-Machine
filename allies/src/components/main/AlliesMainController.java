package components.main;

import components.contestPage.ContestAlliesMainController;
import components.dashBoardPage.DashBoardMainController;
import components.login.LoginControllerAllies;
import components.main.model.AllContestsData;
import components.main.model.AllEncryptedStringAndStartContestData;
import components.main.model.AllTeamsAgentsData;
import components.main.model.AllTeamsDataForContest;
import data.DataAgentsProgressToAllies;
import data.DataCandidatesToAllies;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtilAllies;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;

import static utils.ConstantsAllies.*;

public class AlliesMainController implements Initializable, Closeable {

    private Stage primaryStage;
    private String currentUser;
    private String currentBattle;
    @FXML
    private ScrollPane loginAlliesComponent;
    @FXML
    private LoginControllerAllies loginAlliesComponentController;
    @FXML
    private AnchorPane anchorPaneToShow;
    @FXML
    private ScrollPane dashBoardComponent;
    @FXML
    private DashBoardMainController dashBoardComponentController;
    @FXML
    private ScrollPane contestAlliesPageComponent;
    @FXML
    private ContestAlliesMainController contestAlliesPageComponentController;
    @FXML
    private Label nameOfLoggedUserLabel;
    @FXML
    private TabPane allTabs;
    @FXML
    private Tab tabContestAllies;
    @FXML
    private Tab dashBoardTab;
    @FXML
    private Label registrationMainPageLabel;
    private Timer timerForContestData;
    private Timer timerForTeamAgents;
    private Timer timerForCurrentContestData;
    private Timer timerForTeamsData;
    private Timer timerForEncryptedStringAndContestStart;
    private Timer timerForCandidates;
    private Timer timerAgentProgressData;
    private Timer timerOfProgressOfTasksDm;
    private Timer timerFinishedContest;
    private int sizeOfTask;

    private boolean timerReadyRegistration;
   public AlliesMainController(){

       timerReadyRegistration=false;
   }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (dashBoardComponentController != null) {
            dashBoardComponentController.setMainController(this);
        }
        if(contestAlliesPageComponentController!=null){
            contestAlliesPageComponentController.setMainController(this);
        }
        loadLoginPage();
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginAlliesComponent = fxmlLoader.load();
            loginAlliesComponentController = fxmlLoader.getController();
            loginAlliesComponentController.setAlliesMainController(this);
            anchorPaneToShow.getChildren().add(loginAlliesComponent);

            //loginComponentController(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void startRefresherForContestsData() {
        DataContestsRefresher dataContestsRefresher = new DataContestsRefresher(this::updatedDataContests);
        timerForContestData = new Timer();
        timerForContestData.schedule(dataContestsRefresher, REFRESH_RATE, REFRESH_RATE);

        //if(timerReadyRegistration){
          //  timer.cancel();
        //}
    }
    public void updatedDataContests(AllContestsData contestsData){

       this.dashBoardComponentController.showContestsData(contestsData);

    }
    public void startRefresherForTeamsAgents(){

       TeamsAgentsRefresher teamsAgentsRefresher=new TeamsAgentsRefresher(this::updatedTeamsAgents,currentUser);
       timerForTeamAgents=new Timer();
        timerForTeamAgents.schedule(teamsAgentsRefresher, REFRESH_RATE, REFRESH_RATE);


    }

    private void updatedTeamsAgents(AllTeamsAgentsData allTeamsAgentsData) {
       this.dashBoardComponentController.showTeamsAgentsData(allTeamsAgentsData);
    }

    public void startRefresherForCurrentContestData(){
       DataContestsRefresher dataContestsRefresher=new DataContestsRefresher(this::updateDataCurrentContest);
        timerForCurrentContestData = new Timer();
        timerForCurrentContestData.schedule(dataContestsRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateDataCurrentContest(AllContestsData allContestsData) {

        contestAlliesPageComponentController.startContestPage(currentBattle,allContestsData);
    }

    public void startRefresherForTeamsData(){
       TeamsDataRefresher teamsDataRefresher=new TeamsDataRefresher(this::updateTeamsData,currentBattle);
        timerForTeamsData = new Timer();
        timerForTeamsData.schedule(teamsDataRefresher, REFRESH_RATE, REFRESH_RATE);

    }

    private void updateTeamsData(AllTeamsDataForContest allTeamsDataForContest) {
       this.contestAlliesPageComponentController.showAllTeamsDataForContest(allTeamsDataForContest);
    }

    public void startRefresherForCandidatesData(){
       TeamsCandidatesRefresher teamsCandidatesRefresher=new TeamsCandidatesRefresher(this::updateCandidatesTeam,currentUser);
       timerForCandidates=new Timer();
       timerForCandidates.schedule(teamsCandidatesRefresher,REFRESH_RATE,REFRESH_RATE);
    }

    private void updateCandidatesTeam(DataCandidatesToAllies[] dataCandidatesToAllies) {
       this.contestAlliesPageComponentController.showAllCandidatesDataAllies(dataCandidatesToAllies);
    }

    public void startRefresherForAgentsProgressData(){
       TeamsProgressDataOfAgentsRefresher teamsProgressDataOfAgentsRefresher=new TeamsProgressDataOfAgentsRefresher(this::updateAgentsProgressData,currentUser);
       timerAgentProgressData=new Timer();
       timerAgentProgressData.schedule(teamsProgressDataOfAgentsRefresher,REFRESH_RATE,REFRESH_RATE);
    }

    private void updateAgentsProgressData(DataAgentsProgressToAllies[] dataAgentsProgressToAllies) {
       this.contestAlliesPageComponentController.showProgressOfAgentsData(dataAgentsProgressToAllies);
    }

    private void startRefresherForTheTotalProgressOfTaskDm(){
       ProgressOfTasksDmRefresher progressOfTasksDmRefresher=new ProgressOfTasksDmRefresher(this::updateProgressTasksDm,currentUser,currentBattle);
       timerOfProgressOfTasksDm=new Timer();
       timerOfProgressOfTasksDm.schedule(progressOfTasksDmRefresher,REFRESH_RATE,REFRESH_RATE);

    }

    private void updateProgressTasksDm(Integer[] dataOfTasksOfDM) {
       this.contestAlliesPageComponentController.showProgressOfTasksOfDm(dataOfTasksOfDM);
    }

    public void startRefresherForFinishedContest(){
       ContestFinishedRefresher contestFinishedRefresher=new ContestFinishedRefresher(this::updateContestFinished,currentUser,currentBattle);
       timerFinishedContest=new Timer();
       timerFinishedContest.schedule(contestFinishedRefresher,REFRESH_RATE,REFRESH_RATE);

    }

    private void updateContestFinished(String[] result) {

        this.contestAlliesPageComponentController.showTheWinnerOfContest(result[1],currentUser);
        timerFinishedContest.cancel();
        timerOfProgressOfTasksDm.cancel();
        timerAgentProgressData.cancel();
        timerForCandidates.cancel();
        timerForTeamAgents.cancel();
        timerForTeamsData.cancel();
        timerForContestData.cancel();
    }



    @Override
    public void close() throws IOException {

    }

    public void setPageAfterLoginSuccess(){
        anchorPaneToShow.getChildren().remove(loginAlliesComponent);
        nameOfLoggedUserLabel.setText("Logged User: "+currentUser);
        startRefresherForContestsData();
        startRefresherForTeamsAgents();
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage=primaryStage;
    }

    public void setCurrentLoginAccount(String userName) {
        currentUser=new String();
        currentUser=userName;
    }

    public void readyForRegistrationClicked(String battle) {

       currentBattle=battle;
        String finalUrl = HttpUrl
                .parse(READY_FOR_REG_PAGE)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("type","allies").addQueryParameter("battle",battle)
                .build()
                .toString();
        HttpClientUtilAllies.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                dashBoardComponentController.setAnswerAboutReadyForReg(false,false);

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {

                    if(response.code()==204){
                        dashBoardComponentController.setAnswerAboutReadyForReg(false,true);
                    }else {
                        dashBoardComponentController.setAnswerAboutReadyForReg(false,false);
                    }
                    response.close();

                    return;

                } else {
                    dashBoardComponentController.setAnswerAboutReadyForReg(true,false);
                    response.close();
                    Platform.runLater(()->{
                        startRefresherForCurrentContestData();
                        startRefresherForTeamsData();
                        currentBattle=battle;
                        allTabs.getSelectionModel().select(tabContestAllies);
                        registrationMainPageLabel.setText("Registration succeeded!");
                    });
                }
            }
        });

    }

    public void readyToStartCompetitionClicked(int sizeOfTask) {

       this.sizeOfTask=sizeOfTask;
        String finalUrl = HttpUrl
                .parse(READY_FOR_CONTEST_ALLY_PAGE)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("type","allies").addQueryParameter("battle",currentBattle)
                .addQueryParameter("sizeOfTask",String.valueOf(sizeOfTask))
                .build()
                .toString();
        HttpClientUtilAllies.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //dashBoardComponentController.setAnswerAboutReadyForReg(false);

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    contestAlliesPageComponentController.resultFromReadyToStartCompetition(false,response.code());
                    return;

                } else {
                    startRefresherForEncryptedStringAndCheckIfContestStarts();
                    startRefresherForCandidatesData();
                    startRefresherForAgentsProgressData();
                    startRefresherForTheTotalProgressOfTaskDm();
                    startRefresherForFinishedContest();
                    contestAlliesPageComponentController.resultFromReadyToStartCompetition(true,response.code());

                }
                response.close();
            }
        });
    }
    public void startRefresherForEncryptedStringAndCheckIfContestStarts() {
        EncryptedStringAndContestStartRefresher encryptedStringAndContestStartRefresher = new EncryptedStringAndContestStartRefresher(this::updateDataEncryptedStringAndIfContestStart,currentBattle);
        timerForEncryptedStringAndContestStart = new Timer();
        timerForEncryptedStringAndContestStart.schedule(encryptedStringAndContestStartRefresher, REFRESH_RATE, REFRESH_RATE);

        //if(timerReadyRegistration){
        //  timer.cancel();
        //}
    }
    public void updateDataEncryptedStringAndIfContestStart(AllEncryptedStringAndStartContestData allEncryptedStringAndStartContestData){

       Platform.runLater(()->{
           if(!allEncryptedStringAndStartContestData.getContestStart().equals("false")){
               timerForEncryptedStringAndContestStart.cancel();
               timerForCurrentContestData.cancel();
           }
           contestAlliesPageComponentController.updateEncryptedStringAndIfContestStarted(allEncryptedStringAndStartContestData);

       });
        //this.dashBoardComponentController.showContestsData(contestsData);

    }

    public void finishedButtonClicked() {
        String finalUrl = HttpUrl
                .parse(FINISHED_CONTEST_BUTTON_PRESSED)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("type","allies").addQueryParameter("battle",currentBattle)
                .addQueryParameter("sizeOfTask",String.valueOf(sizeOfTask))
                .build()
                .toString();
        HttpClientUtilAllies.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //dashBoardComponentController.setAnswerAboutReadyForReg(false);

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {

                    return;

                } else {


                }
                response.close();
            }
        });
        Platform.runLater(()->{
            registrationMainPageLabel.setText("");
            allTabs.getSelectionModel().select(dashBoardTab);
            this.dashBoardComponentController.finishWasClickedUpdateData();
            startRefresherForContestsData();
            startRefresherForTeamsAgents();

        });
    }
}
