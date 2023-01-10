package components.main;

import atomic.TheAtomicClass;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import components.login.LoginControllerAgents;
import components.login.modal.AllAgentsContestAndTeamData;
import components.login.modal.AllCandidatesOfAgent;
import data.DataContestWebDto;
import data.DataFromAgentToDm;
import data.DataToAgentXmlDto;
import data.TaskDto;
import generatedAgent.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logicTasks.AgentTasksNew;
import logicTasks.ThreadFactoryBuilderNew;
import machine.EnigmaMachine;
import okhttp3.*;
import okhttp3.internal.http2.Hpack;
import org.jetbrains.annotations.NotNull;
import utils.ConstantsAgents;
import utils.HttpClientUtilAgents;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import static utils.ConstantsAgents.*;
import static utils.HttpClientUtilAgents.HTTP_CLIENT;

public class AgentsMainController implements Initializable, Closeable {

    private EnigmaMachine enigmaMachine;
    private HashSet<String>dictionary;
    private Stage primaryStage;
    private String currentUser;
    private String alliesTeam;
    private int threadsToUseNum;
    private double threadsToUse;
    private String tasksToPull;
    private int taskToPullNum;
    private String currentBattle;
    private int counterForStartOfContest;
    private String encryptedString;
    private int sizeOfTasKOfAlly;
    private int totalTasksAlly;
    private int usedRotorsSize;
    private String machineAlphabet;
    private BlockingQueue<Runnable> blockingQueue;
    private BlockingQueue<DataFromAgentToDm>dataFromAgentToDmBlockingQueue;
    private ThreadPoolExecutor executor;
    private ThreadFactory customThreadFactory;
    double index=0;
    private int indexForAllTasks;
    private Consumer<Double> toSendCounter;
    private Consumer<Long>toSendTimeOfAll;
    private Object objectToWaitOn;
    //private Consumer<Integer>counterOfTasksInQueue;
    boolean isEmptyQueue;
    private IntConsumer isLast;
    IntConsumer counterOfTasksInQueue;
    private int resultAll;
    private int taskForRunning;
    private int sumOfAllTaskedPulled;
    private HashSet<String>allDecryption;
    private HashSet<String>allConfigurationsOfDecryption;
    private AllCandidatesOfAgent allCandidatesOfAgent;
    private int allCandidates;
    boolean candidatesBooleanLoop;
    private ArrayList<DataFromAgentToDm>allTheDecryptionArrayToSendToServer;
    private int remainingTasks;
    private Object lock;
    private boolean finished;


    private final TheAtomicClass.Counter counterAtomic = new TheAtomicClass.Counter();
    private final TheAtomicClass.Counter counterAtomicCandidates=new TheAtomicClass.Counter();
    private final TheAtomicClass.Counter counterAllTasksPreformed=new TheAtomicClass.Counter();
    @FXML
    private ScrollPane loginAgentsComponent;
    @FXML
    private LoginControllerAgents loginAgentsComponentController;
    @FXML
    private AnchorPane anchorPaneToShow;
    @FXML
    private Label nameOfLoggedUserLabel;
    @FXML
    private HBox hBoxContestAndTeamData;
    @FXML
    private TableView<AllContestAndTeamTableViewAgents> tableViewContestAndTeamData;
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
    private TableColumn colTeam;
    @FXML
    private TableColumn colAgents;
    @FXML
    private TableColumn colTaskSize;
    //@FXML
    //private HBox hBoxAllAgentsCandidates;
    //@FXML
    //private TableView<AllCandidatesDataTableView>allAgentsCandidatesTableView;
    //@FXML
    //private TableColumn colDecrypt;
    //@FXML
    //private TableColumn colTaskProduced;
    //@FXML
    //private TableColumn colConfiguration;
    @FXML
    private Label statusOfContestAgentLabel;
    @FXML
    private Label tasksInQueueLabel;
    @FXML
    private Label totalTasksPulledLabel;
    @FXML
    private Label totalTasksPreformedLabel;
    @FXML
    private Label candidatesFoundLabel;
    @FXML
    private TextArea textAreaCandidates;
    @FXML
    private Label winnerOfContestLabel;
    private Timer timerContestAndTeamData;
    private Timer timerFinishedContestAgent;
    private Timer timerCheckAllyPressedFinished;
    private Timer timerCheckIfContestStarted;
    private Thread taskCreator;
    private Thread taskReceiverCandidates;
    private boolean taskReceiverRes;
    private boolean contestStopped;
    DataFromAgentToDm[]dataFromAgentToDms;


    private int tmpInt;

    public AgentsMainController(){

        counterForStartOfContest=0;
        contestStopped=false;
        tmpInt=0;
        finished=false;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colBattle.setCellValueFactory(
                new PropertyValueFactory<AllContestAndTeamTableViewAgents, String>("colBattle"));

        colUBoat.setCellValueFactory(
                new PropertyValueFactory<AllContestAndTeamTableViewAgents, String>("colUBoat"));


        colStatus.setCellValueFactory(
                new PropertyValueFactory<AllContestAndTeamTableViewAgents, String>("colStatus"));


        colLevel.setCellValueFactory(new PropertyValueFactory<AllContestAndTeamTableViewAgents,String>("colLevel"));

        colAlliesMax.setCellValueFactory(new PropertyValueFactory<AllContestAndTeamTableViewAgents,String>("colAlliesMax"));

        colAlliesLogged.setCellValueFactory(new PropertyValueFactory<AllContestAndTeamTableViewAgents,String>("colAlliesLogged"));

        colTeam.setCellValueFactory(
                new PropertyValueFactory<AllContestAndTeamTableViewAgents, String>("colTeam"));
        colAgents.setCellValueFactory(
                new PropertyValueFactory<AllContestAndTeamTableViewAgents, String>("colAgents"));
        colTaskSize.setCellValueFactory(
                new PropertyValueFactory<AllContestAndTeamTableViewAgents, String>("colTaskSize"));
      //  colDecrypt.setCellValueFactory(new PropertyValueFactory<AllCandidatesDataTableView,String>("colDecrypt"));

        //colConfiguration.setCellValueFactory(new PropertyValueFactory<AllCandidatesDataTableView,String>("colConfiguration"));
        //colTaskProduced.setCellValueFactory(new PropertyValueFactory<AllCandidatesDataTableView,String>("colTaskProduced"));
        loadLoginPage();

    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginAgentsComponent = fxmlLoader.load();
            loginAgentsComponentController = fxmlLoader.getController();
            loginAgentsComponentController.setAlliesMainController(this);
            anchorPaneToShow.getChildren().add(loginAgentsComponent);
            loginAgentsComponentController.getAllDataFromUser();

            //loginComponentController(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setPageAfterLoginSuccess(){
        anchorPaneToShow.getChildren().remove(loginAgentsComponent);
        nameOfLoggedUserLabel.setText("Logged User: "+currentUser);

        startRefresherForAgentsContestAndTeamData();
        //startRefresherCheckIfContestStartedAgent();

        //startRefresher();
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage=primaryStage;
    }



    @Override
    public void close() throws IOException {

    }

    public void setCurrentLoginAccount(String userName, String alliesTeam, double threadsToUse, String tasksToPull) {
        currentUser=userName;
        this.alliesTeam=alliesTeam;
        this.threadsToUseNum=(int)threadsToUse;
        this.threadsToUse=threadsToUse;
        this.tasksToPull=tasksToPull;
        this.taskToPullNum=Integer.parseInt(tasksToPull);


    }
    public void startRefresherCheckIfContestStartedAgent(){
        AgentIsContestStartedRefresher agentIsContestStartedRefresher=new AgentIsContestStartedRefresher(this::updateIfContestStarted,currentUser,alliesTeam);
        timerCheckIfContestStarted=new Timer();
        timerCheckIfContestStarted.schedule(agentIsContestStartedRefresher,REFRESH_RATE,REFRESH_RATE);

    }

    private void updateIfContestStarted(Boolean result) {
            if(result){
                try {
                    timerCheckIfContestStarted.cancel();
                    initAllTheThreadsForContest();
                }catch (InterruptedException e){
                    throw new RuntimeException();
                }
            }
    }

    private void startRefresherForAgentsContestAndTeamData() {
        AgentsContestAndTeamDataRefresher agentsContestAndTeamDataRefresher = new AgentsContestAndTeamDataRefresher(this::updateAgentsContestAndTeamData,currentUser);
        timerContestAndTeamData = new Timer();
        timerContestAndTeamData.schedule(agentsContestAndTeamDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateAgentsContestAndTeamData(AllAgentsContestAndTeamData allAgentsContestAndTeamData) {

        Platform.runLater(()->{

            tableViewContestAndTeamData.getItems().clear();

            StringBuilder status=new StringBuilder();

            if(allAgentsContestAndTeamData.getStatus()) {
                status.append("Active");
                counterForStartOfContest++;
            }else{
                status.append("Not active");
            }


            sizeOfTasKOfAlly=allAgentsContestAndTeamData.getSizeOfTask();
                ObservableList<AllContestAndTeamTableViewAgents> data =
                        FXCollections.observableArrayList(
                                new AllContestAndTeamTableViewAgents(allAgentsContestAndTeamData.getBattleFieldName(),allAgentsContestAndTeamData.getuBoatName(),status.toString(),allAgentsContestAndTeamData.getLevel(),String.valueOf(allAgentsContestAndTeamData.getAlliesMax()),String.valueOf(allAgentsContestAndTeamData.getAlliesLogged()),allAgentsContestAndTeamData.getTeamName(),String.valueOf(allAgentsContestAndTeamData.getNumberOfAgents()),String.valueOf(allAgentsContestAndTeamData.getSizeOfTask()))
                        );

            tableViewContestAndTeamData.getItems().addAll(data);

                VBox vbox = new VBox();
                vbox.setSpacing(5);
                vbox.getChildren().addAll(tableViewContestAndTeamData);
                hBoxContestAndTeamData.getChildren().addAll(vbox);
                if(counterForStartOfContest==1){
                    timerContestAndTeamData.cancel();
                    //counterForStartOfContest=0;
                    //לזכור לבדוק פה האם יש חשיבות למשתנה של הקאונטר והאם צריך לאפס אותו שמתחילה תחרות חדשה
                    currentBattle=allAgentsContestAndTeamData.getBattleFieldName();
                    try {
                        initAllTheThreadsForContest();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
    }
    private void initAllTheThreadsForContest() throws InterruptedException {

        candidatesBooleanLoop=true;
        allCandidatesOfAgent=new AllCandidatesOfAgent();
        allDecryption=new HashSet<>();
        allConfigurationsOfDecryption=new HashSet<>();
        allCandidates=0;
        sumOfAllTaskedPulled=0;
        counterAllTasksPreformed.makeEmpty();
        counterAtomic.makeEmpty();
        counterAtomicCandidates.makeEmpty();
        isEmptyQueue=false;
        resultAll=0;
        contestStopped=false;

        dataFromAgentToDmBlockingQueue=new LinkedBlockingQueue<>();
        this.blockingQueue=new LinkedBlockingQueue<>(taskToPullNum+5);
        customThreadFactory = new ThreadFactoryBuilderNew()
                .setNamePrefix("number:").setDaemon(false)
                .setPriority(Thread.MAX_PRIORITY)
                .setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        System.err.println(String.format(
                                "Thread %s threw exception - %s", t.getName(),
                                e.getMessage()));

                    }
                }).build();
        executor = new ThreadPoolExecutor(this.threadsToUseNum, this.threadsToUseNum, 5, TimeUnit.SECONDS,
                blockingQueue, new ThreadPoolExecutor.AbortPolicy());

        // Let start all core threads initially
        executor.prestartAllCoreThreads();
        //this.taskReceiverCandidates=new Thread(()->{
          //  while(candidatesBooleanLoop){
            //    DataFromAgentToDm data= null;
              //  try {
                //    data = dataFromAgentToDmBlockingQueue.take();
                    //allAgentsCandidatesTableView.getItems().clear();
                  //  insertToTableViewForCandidates(data);

                //} catch (InterruptedException e) {
                  //  throw new RuntimeException(e);
                //}
            //}

        //});
       // taskReceiverCandidates.start();

        this.taskCreator=new Thread(()->{
            try {
                Platform.runLater(()->{
                    statusOfContestAgentLabel.setTextFill(Color.GREEN);
                    statusOfContestAgentLabel.setText("Contest started!");
                });
                startContest();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });taskCreator.start();
     //   while(contestStopped==false){
       //     synchronized (lock){
         //       lock.wait();
           // }
        //}



    }
    public void insertToTableViewForCandidates(DataFromAgentToDm data){

        //allAgentsCandidatesTableView.getItems().clear();
        allDecryption.add(data.getDecrypted());
        allCandidates++;
        allCandidatesOfAgent.addDecrypted(data.getDecrypted());
        allCandidatesOfAgent.addNumberOfTasks((int)data.getNumberOfTask());
        allCandidatesOfAgent.addConfiguration(data.getConfiguration());
        Platform.runLater(()->{
            for(int i=0;i<allCandidatesOfAgent.getDecrypted().size();i++) {
                ObservableList<AllCandidatesDataTableView> dataArray = FXCollections.observableArrayList(new AllCandidatesDataTableView(allCandidatesOfAgent.getDecrypted().get(i), String.valueOf(allCandidatesOfAgent.getNumberOfTasks().get(i)), allCandidatesOfAgent.getConfigurations().get(i)));

          //      allAgentsCandidatesTableView.getItems().addAll(dataArray);
                VBox vbox = new VBox();
                vbox.setSpacing(5);
            //    vbox.getChildren().addAll(allAgentsCandidatesTableView);
              //  hBoxAllAgentsCandidates.getChildren().addAll(vbox);
                candidatesFoundLabel.setText(String.valueOf(allCandidates));
            }
        });
    }
    private void startContest() throws IOException, JAXBException, InterruptedException {


        Platform.runLater(()->{
            tasksInQueueLabel.setText("0");
            totalTasksPulledLabel.setText("0");
            totalTasksPulledLabel.setText("0");
            candidatesFoundLabel.setText("0");

        });
        String url = HttpUrl
                .parse(XML_DATA_TO_AGENT)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("team",alliesTeam).addQueryParameter("battle",currentBattle)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();
        if(response.code()!=200){
            response.close();
            return;
        }
        startRefresherForFinishedContest();
        Gson gson=new Gson();
        DataToAgentXmlDto dataToAgentXmlDtos=gson.fromJson(response.body().string(),DataToAgentXmlDto.class);

       // JsonParser jsonParser=new JsonParser();
        //JsonElement jsonElement=jsonParser.parse(response.body().string());
        //JsonObject jsonObject =jsonElement.getAsJsonObject();
        //StringReader stringReader=new StringReader(jsonObject.get("xml").getAsString());
        //encryptedString=jsonObject.get("encryptedString").toString().substring(1,jsonObject.get("encryptedString").toString().length()-1);
        //totalTasksAlly=jsonObject.get("totalTasks").getAsInt();
       // StringReader stringReader=new StringReader(response.body().string());
        StringReader stringReader=new StringReader(dataToAgentXmlDtos.getXml());
        totalTasksAlly=dataToAgentXmlDtos.getTotalTasks();
        encryptedString=dataToAgentXmlDtos.getEncryptedString();
        CTEEnigma cteEnigma = deserializeFrom(stringReader);
        String alphabet=cteEnigma.getCTEMachine().getABC();
        String resultAlphabetWithTrim=null;
        resultAlphabetWithTrim=alphabet.trim();
        enigmaMachine=new EnigmaMachine(cteEnigma.getCTEMachine().getRotorsCount(),resultAlphabetWithTrim);
        addReflectors(cteEnigma.getCTEMachine().getCTEReflectors());
        addRotors(cteEnigma.getCTEMachine().getCTERotors());
        usedRotorsSize=cteEnigma.getCTEMachine().getRotorsCount();
        machineAlphabet=resultAlphabetWithTrim;
        dictionary=new HashSet<>();
        String words=cteEnigma.getCTEDecipher().getCTEDictionary().getWords().trim();
        String excluded=cteEnigma.getCTEDecipher().getCTEDictionary().getExcludeChars();
        String regexComma = " ";
        String[] strWords = words.split(regexComma);
        StringBuilder stringBuilder=new StringBuilder();

        for(String str:strWords){
            stringBuilder=new StringBuilder();
            for(int i=0;i<str.length();i++){
                for(int j=0;j<excluded.length();j++){
                    if(str.charAt(i)==excluded.charAt(j)){
                        stringBuilder.append(str);
                        stringBuilder.deleteCharAt(i);
                        str=stringBuilder.toString();
                        i--;
                    }

                }
            }

            if(!dictionary.contains(str)){
                dictionary.add(str);
            }
        }

        indexForAllTasks=0;
        int theTasksForLoop=totalTasksAlly;


        Double howMuchConfig=Math.pow(machineAlphabet.length(),enigmaMachine.getUsedRotorsCount());
        objectToWaitOn=new Object();

        //howMuchConfig/(sizeOfTasKOfAlly*taskToPullNum)
        taskForRunning=taskToPullNum;
        while(!contestStopped) {
            String finalUrl = HttpUrl
                    .parse(ConstantsAgents.TASKS_FROM_DM_TO_AGENT_PAGE)
                    .newBuilder()
                    .addQueryParameter("username", currentUser).addQueryParameter("type", "agents").addQueryParameter("team", alliesTeam)
                    .addQueryParameter("tasksToPull", tasksToPull).addQueryParameter("battle", currentBattle)
                    .build()
                    .toString();

            //updateHttpStatusLine("New request is launched for: " + finalUrl);
            HttpClientUtilAgents.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        response.close();
                        contestStopped=true;
                        freeThreadPool();
                        return;

                        //Platform.runLater(() ->
                        //           loginErrorLabel.setText("Something went wrong: " + responseBody)
                        //);
                    } else {

                        Gson gson=new Gson();
                        TaskDto[]taskDtos=gson.fromJson(response.body().string(),TaskDto[].class);

                        ArrayList<ArrayList<Integer>> myArray = new ArrayList<>();
                        int[] array = new int[usedRotorsSize];

                        if(contestStopped){
                            return;
                        }
                        // כאן
                        //try {

                            //taskForRunning = jsonElement.getAsJsonArray().size();
                        //}catch (IllegalStateException e){
                          //  System.out.println("here is the problem..");
                            //Platform.runLater(()->{
                              //  tasksInQueueLabel.setText("0");
                            //});

                            //return;
                        //}
                        if(taskDtos==null){
                            return;
                        }
                        taskForRunning=taskDtos.length;

                            //taskForRunning=tasks.size();

                            //taskForRunning=0;
                        sumOfAllTaskedPulled+=taskForRunning;

                            Platform.runLater(() -> {
                                totalTasksPulledLabel.setText(String.valueOf(sumOfAllTaskedPulled));

                            });

                        if(contestStopped==true){
                            freeThreadPool();
                            Platform.runLater(()->{
                                tasksInQueueLabel.setText("0");
                            });
                            return;
                        }
                         for (int i = 0; i < taskForRunning; i++) {

                            ArrayList<Integer> startingPosition = new ArrayList<>();
                            for(int j=0;j<taskDtos[i].getStartingPosition().size();j++){
                                startingPosition.add(taskDtos[i].getStartingPosition().get(j));
                            }


                             int reflectorId=taskDtos[0].getReflectorId();
                            EnigmaMachine machine = new EnigmaMachine(usedRotorsSize, machineAlphabet);

                             machine.addReflector(reflectorId,enigmaMachine.allReflectors.get(reflectorId).getReflectorMapping());

                            for (int j = 0; j < enigmaMachine.allRotors.size(); j++) {
                                machine.addRotor(enigmaMachine.allRotors.get(j + 1).getRotorId(), enigmaMachine.allRotors.get(j + 1).getNotchPosition(), enigmaMachine.allRotors.get(j + 1).forwardEntry, enigmaMachine.allRotors.get(j + 1).backwardEntry);
                            }

                            int[] usedRotors = new int[usedRotorsSize];

                             for(int w=0;w<taskDtos[0].getRotorsId().length;w++){
                                 usedRotors[w]=taskDtos[0].getRotorsId()[w];
                             }
                             //usedRotors=tasks.get(0).getRotorsId();

                            machine.chooseRotorsToBeUsed(usedRotors);
                             //כאן
                           // machine.chooseReflectorToBeUsed(Integer.parseInt(reflectorId));
                             machine.chooseReflectorToBeUsed(reflectorId);

                            // machine.chooseReflectorToBeUsed(tasks.get(0).getReflectorId());
                            array = new int[usedRotorsSize];
                            myArray = new ArrayList<>();
                            for (int k = 0; k < usedRotorsSize; k++) {
                                ArrayList<Integer> tmp = new ArrayList<>();
                                for (int m = 0; m < machineAlphabet.length(); m++) {
                                    tmp.add(m);
                                }
                                myArray.add(tmp);
                            }

                            for (int w = 0; w < array.length; w++) {
                                while (!myArray.get(w).get(0).equals(startingPosition.get(w))) {
                                    Collections.rotate(myArray.get(w), -1);
                                }
                                array[w] = 0;
                            }

                            ArrayList<ArrayList<Integer>> thePositions = new ArrayList<>();


                            int tmpSizeOfTask = sizeOfTasKOfAlly;
                            while (tmpSizeOfTask > 0) {

                                ArrayList<Integer> posForConfig = new ArrayList<>();

                                for (int r = 0; r < myArray.size(); r++) {

                                    posForConfig.add(myArray.get(r).get(0));
                                }
                                thePositions.add(posForConfig);

                                for (int f = 0; f < myArray.size(); f++) {
                                    if (f == 0) {
                                        if (myArray.get(f).get(0) == machineAlphabet.length() - 1) {
                                            Collections.rotate(myArray.get(f), -1);
                                            array[0] = 1;

                                        } else {
                                            Collections.rotate(myArray.get(f), -1);

                                        }
                                    } else {

                                        if (array[f - 1] == 1) {
                                            if (myArray.get(f).get(0) == machineAlphabet.length() - 1) {
                                                array[f] = 1;
                                            }
                                            Collections.rotate(myArray.get(f), -1);
                                            array[f - 1] = 0;
                                        }
                                    }
                                }
                                tmpSizeOfTask--;
                            }
                            index++;
                           // System.out.println(thePositions);
                             //System.out.println(thePositions.size());
                            // indexForAllTasks++;
                           // counterOfTasksInQueue = res -> {
                             //   resultAll += res;
                               // Platform.runLater(() -> {
                                 //   totalTasksPreformedLabel.setText(String.valueOf(resultAll));
                                   // tasksInQueueLabel.setText(String.valueOf(blockingQueue.size()));
                                //});

                            //};

                                try {
                                    blockingQueue.put(new AgentTasksNew(machine, encryptedString, thePositions, dictionary, dataFromAgentToDmBlockingQueue, index, toSendCounter, sizeOfTasKOfAlly * taskToPullNum, toSendTimeOfAll, counterOfTasksInQueue, taskToPullNum, isLast,counterAtomic,counterAtomicCandidates,counterAllTasksPreformed));

                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                             Platform.runLater(()->{
                                tasksInQueueLabel.setText(String.valueOf(blockingQueue.size()));
                              });
                    }
                        }

                }
            });
            Platform.runLater(()->{
                totalTasksPreformedLabel.setText(String.valueOf(counterAllTasksPreformed.value()));
                //tasksInQueueLabel.setText(String.valueOf(blockingQueue.size()));
            });
            if(contestStopped==true){
                freeThreadPool();
                Platform.runLater(()->{
                    tasksInQueueLabel.setText("0");
                });
                return;
            }

            while(counterAtomic.value()!=taskForRunning){
                //Platform.runLater(()->{
                    //tasksInQueueLabel.setText(String.valueOf(taskForRunning-counterAtomic.value()));
                 //   tasksInQueueLabel.setText(String.valueOf(blockingQueue.size()));
               // });
                sendHttpForAgentsProgress(blockingQueue.size());
                Platform.runLater(()->{
                    tasksInQueueLabel.setText(String.valueOf(blockingQueue.size()));
                });
                Thread.sleep(1000);
            }
            if(contestStopped==true){
                freeThreadPool();
                Platform.runLater(()->{
                    tasksInQueueLabel.setText("0");
                });
                return;
            }

             DataFromAgentToDm data;
            int i=0;
            allTheDecryptionArrayToSendToServer=new ArrayList<>();
            while(i<counterAtomicCandidates.value()){
                StringBuilder stringBuilder1=new StringBuilder();
                data=dataFromAgentToDmBlockingQueue.take();

                stringBuilder1.append("Decrypted message: ("+ data.getDecrypted()+") ,"+ "Task produced: ("+(int)data.getNumberOfTask()+ "), Configuration: ("+ data.getConfiguration()+").\n");
                allCandidates++;
                Platform.runLater(()->{
                    textAreaCandidates.appendText(stringBuilder1.toString());
                    candidatesFoundLabel.setText(String.valueOf(allCandidates));
                });
                allTheDecryptionArrayToSendToServer.add(data);
                if(allTheDecryptionArrayToSendToServer.size()>20){
                    dataFromAgentToDms=new DataFromAgentToDm[allTheDecryptionArrayToSendToServer.size()];
                    for(int w=0;w<dataFromAgentToDms.length;w++){
                        dataFromAgentToDms[w]=allTheDecryptionArrayToSendToServer.get(w);
                    }
                    Gson json=new Gson();
                    String theUrl=HttpUrl.parse(CANDIDATES_FROM_AGENTS_TO_UBOAT).newBuilder().build().toString();
                    String jsonString="code="+json.toJson(dataFromAgentToDms);
                    Request requestToServer =new Request.Builder().url(theUrl).addHeader("username",currentUser).addHeader("team",alliesTeam).addHeader("battle",currentBattle)
                            //.addHeader("candidates",String.valueOf(allCandidates)).addHeader("taskedPreformed",String.valueOf(counterAllTasksPreformed.value())).addHeader("taskRemain",String.valueOf(taskForRunning-counterAtomic.value()))
                            .post(RequestBody.create(jsonString.getBytes())).build();
                    Call callToServer = HTTP_CLIENT.newCall(requestToServer);
                    Response responseFromServer = callToServer.execute();
                    if(responseFromServer.code()!=200){

                    }
                    allTheDecryptionArrayToSendToServer.clear();
                }
                i++;
            }
            if(!allTheDecryptionArrayToSendToServer.isEmpty()){
                dataFromAgentToDms=new DataFromAgentToDm[allTheDecryptionArrayToSendToServer.size()];
                for(int w=0;w<allTheDecryptionArrayToSendToServer.size();w++){
                    dataFromAgentToDms[w]=allTheDecryptionArrayToSendToServer.get(w);
                }
                Gson json=new Gson();
                String theUrl=HttpUrl.parse(CANDIDATES_FROM_AGENTS_TO_UBOAT).newBuilder().build().toString();
                String jsonString="code="+json.toJson(dataFromAgentToDms);
                Request requestToServer =new Request.Builder().url(theUrl).addHeader("username",currentUser).addHeader("team",alliesTeam).addHeader("battle",currentBattle)
                        //.addHeader("candidates",String.valueOf(allCandidates)).addHeader("taskedPreformed",totalTasksPreformedLabel.getText()).addHeader("taskRemain",String.valueOf(taskForRunning-counterAtomic.value()))
                        .post(RequestBody.create(jsonString.getBytes())).build();
                Call callToServer = HTTP_CLIENT.newCall(requestToServer);
                Response responseFromServer = callToServer.execute();
                if(responseFromServer.code()!=200){

                }
            }

            allTheDecryptionArrayToSendToServer.clear();
            if(contestStopped==true){
                freeThreadPool();
                Platform.runLater(()->{
                    tasksInQueueLabel.setText("0");
                });
                return;
            }


            counterAtomic.makeEmpty();
            counterAtomicCandidates.makeEmpty();
            Platform.runLater(()->{
                tasksInQueueLabel.setText(String.valueOf(blockingQueue.size()));
            });

        }
        candidatesBooleanLoop=false;
    }

    private void freeThreadPool() {
        executor.shutdown();

    }

    private void sendHttpForAgentsProgress(int taskRemain) {

        String finalUrl = HttpUrl
                .parse(ConstantsAgents.SEND_AGENTS_PROGRESS_TO_SERVER)
                .newBuilder().addQueryParameter("username",currentUser).addQueryParameter("team",alliesTeam)
                .addQueryParameter("battle",currentBattle).addQueryParameter("candidates", String.valueOf(allCandidates))
                .addQueryParameter("taskedPreformed",totalTasksPreformedLabel.getText()).addQueryParameter("taskRemain", String.valueOf(taskRemain))
                .build()
                .toString();
        HttpClientUtilAgents.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    response.close();

                } else {
                    response.close();
                    //System.out.println("There is NO competitions yet!");

                }
            }
        });
    }


    private final static String JAXB_XML_GAME_PACKAGE_NAME = "generatedAgent";
    //function that creates the instance of the xml file to a java object
        private static CTEEnigma deserializeFrom(StringReader in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }


    public void addReflectors(CTEReflectors reflectors) {

        int input;
        int output;
        for (CTEReflector reflector : reflectors.getCTEReflector()) {
            HashMap<Integer, Integer> reflectorMap = new HashMap<>();
            for(CTEReflect reflect:reflector.getCTEReflect()) {
                input = reflect.getInput();
                output = reflect.getOutput();
                reflectorMap.put(input-1, output-1);
                reflectorMap.put(output-1,input-1);
            }
            this.enigmaMachine.addReflector(changeFromRomeToInt(reflector.getId()), reflectorMap);
        }
    }
    public int changeFromRomeToInt(String input) {

        switch (input) {

            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;
        }
        return 0;
    }
    public String changeFromIntToRome(int input) {
        switch (input) {

            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
        }
        return "";
    }

    public void addRotors(CTERotors cteRotors){

        String tempLeft;
        String tempRight;

        for(CTERotor rotor:cteRotors.getCTERotor()) {
            ArrayList<Character> rotorForward = new ArrayList<>();
            ArrayList<Character> rotorBackward = new ArrayList<>();
            for (CTEPositioning positioning : rotor.getCTEPositioning()) {
                tempRight = positioning.getRight();
                tempLeft = positioning.getLeft();
                rotorForward.add(tempRight.charAt(0));
                rotorBackward.add(tempLeft.charAt(0));
            }

            this.enigmaMachine.addRotor(rotor.getId(), (rotor.getNotch())-1, rotorForward, rotorBackward);

        }

    }
    public void startRefresherForFinishedContest(){
        ContestFinishedAgentsRefresher contestFinishedRefresher=new ContestFinishedAgentsRefresher(this::updateContestFinishedAgent,currentUser,currentBattle);
        timerFinishedContestAgent=new Timer();
        timerFinishedContestAgent.schedule(contestFinishedRefresher,REFRESH_RATE,REFRESH_RATE);
    }

    private void updateContestFinishedAgent(String[] results) {
            Platform.runLater(()->{
                if(results[1].equals(alliesTeam)){
                    winnerOfContestLabel.setText("Your team won! "+"Team "+results[1]);
                }else{
                    winnerOfContestLabel.setText(results[1]);
                }
                statusOfContestAgentLabel.setTextFill(Color.RED);
                statusOfContestAgentLabel.setText("Contest finished.");
            });
            contestStopped=true;
            timerFinishedContestAgent.cancel();
            timerContestAndTeamData.cancel();
            startRefresherToCheckIfAllyPressedFinishedButton();
    }
    public void startRefresherToCheckIfAllyPressedFinishedButton(){
            PressedAllyFinishedRefresher pressedAllyFinishedRefresher=new PressedAllyFinishedRefresher(this::updateAllyPressedFinish,alliesTeam);
            timerCheckAllyPressedFinished=new Timer();
            timerCheckAllyPressedFinished.schedule(pressedAllyFinishedRefresher,REFRESH_RATE,REFRESH_RATE);

    }

    private void updateAllyPressedFinish(Boolean result) {
            if(result){
                Platform.runLater(()->{
                    tableViewContestAndTeamData.getItems().clear();
                    textAreaCandidates.setText("");
                    tasksInQueueLabel.setText("");
                    totalTasksPulledLabel.setText("");
                    totalTasksPreformedLabel.setText("");
                    totalTasksPreformedLabel.setText("");
                    winnerOfContestLabel.setText("");
                    candidatesFoundLabel.setText("");
                    statusOfContestAgentLabel.setText("");
                    counterForStartOfContest=0;
                    startRefresherForAgentsContestAndTeamData();
                   // startRefresherCheckIfContestStartedAgent();
                    timerCheckAllyPressedFinished.cancel();
                    //timerFinishedContestAgent.cancel();
                    //timerContestAndTeamData.cancel();

                });
            }
    }


}
