package components.main;

import com.google.gson.*;
import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import components.contestPage.ContestPageController;
import components.login.LoginController;
import components.machinePage.MachinePageController;
import components.main.model.AllCandidatesForUBoat;
import data.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;

import static utils.Constants.*;
import static utils.HttpClientUtil.HTTP_CLIENT;

public class UBoatMainController implements Initializable, Closeable {

    private Stage primaryStage;
    private String currentUser;
    private int numRotors;
    private int numReflectors;
    CurrentDetailsMachine currentDetailsMachine;
    private String saveCurrentConfiguration;

    @FXML
    private ScrollPane machinePageComponent;
    @FXML
    private MachinePageController machinePageComponentController;
    @FXML
    private ScrollPane contestPageComponent;
    @FXML
    private ContestPageController contestPageComponentController;
    @FXML
    private ScrollPane loginComponent;
    @FXML
    private LoginController loginComponentController;
    @FXML
    private AnchorPane anchorPaneToShow;
    @FXML
    private TextArea textFilePath;
    @FXML
    private Label labelFileSuccess;
    @FXML
    private Label nameOfLoggedUserLabel;
    @FXML
    private TabPane allTabs;
    @FXML
    private Tab machineTab;
    @FXML
    private Tab contestTab;
    @FXML
    private Button loadFileButton;
    private SimpleStringProperty fileLoaderTextProperty;
    private SimpleBooleanProperty isFileLoadedProperty;
    private Timer timerContestStarted;
    private Timer timerCandidates;
    //private SimpleStringProperty labelFileSuccessTextAreaProperty;

    public UBoatMainController(){
        fileLoaderTextProperty = new SimpleStringProperty();
        isFileLoadedProperty = new SimpleBooleanProperty();
        //labelFileSuccessTextAreaProperty=new SimpleStringProperty();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        textFilePath.textProperty().bind(fileLoaderTextProperty);
        allTabs.disableProperty().bind(isFileLoadedProperty.not());
        //labelFileSuccessTextArea.textProperty().bind(labelFileSuccessTextAreaProperty);
        if (machinePageComponentController != null) {
            machinePageComponentController.setMainController(this);
        }
        if(contestPageComponentController!=null){
            contestPageComponentController.setMainController(this);
        }
        loadLoginPage();
    }
    
    public void loadFileButtonListener(ActionEvent actionEvent) throws IOException {

        Alert alertD = new Alert(Alert.AlertType.INFORMATION);
        alertD.setTitle("Error");
        alertD.setHeaderText("Reason:");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        String absolutePath = selectedFile.getAbsolutePath();

        File f = new File(absolutePath);

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file", f.getName(), RequestBody.create(f, MediaType.parse("text/plain")))
                        .addFormDataPart(currentUser, currentUser) // you can add multiple, different parts as needed
                        .build();

        Request request = new Request.Builder()
                .url(FILE_UPLOAD_PAGE)
                .post(body)
                .build();
        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        if(response.code()!=200){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Reason:");
            alert.setContentText(response.body().string());
            alert.showAndWait();

        }else{
            fileLoaderTextProperty.set(absolutePath);
            //labelFileSuccessTextAreaProperty.set("Loaded succeeded!");
            labelFileSuccess.setText("Loaded succeeded!");
            isFileLoadedProperty.set(true);
            loadFileButton.disableProperty().set(true);
            //הוספתי
            machineTab.disableProperty().set(false);
            contestTab.disableProperty().set(false);
            //
            showMachineDetails();
            setTheMachineDetails();
            //this.machinePageComponentController.setDetailsInChild();
        }
        response.close();

    }

    private void setTheMachineDetails() throws IOException {
        String finalUrl = HttpUrl
                .parse(MACHINE_SETTING_DETAILS_PAGE)
                .newBuilder()
                .addQueryParameter("username", currentUser)
                .build()
                .toString();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(currentUser,currentUser)
                .build();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();
        JsonParser jsonParser=new JsonParser();
        JsonElement jsonElement=jsonParser.parse(response.body().string());
        JsonObject jsonObject =jsonElement.getAsJsonObject();
        CurrentDetailsMachine currentDetailsMachine=new CurrentDetailsMachine();
        currentDetailsMachine.setAllNumberOfRotors(jsonObject.get("allNumberOfRotors").getAsInt());
        currentDetailsMachine.setAllNumberOfReflectors(jsonObject.get("allNumberOfReflectors").getAsInt());
        currentDetailsMachine.setAllRotors(jsonObject.get("allRotors").getAsJsonObject());
        currentDetailsMachine.setAllReflectors(jsonObject.get("allReflectors").getAsJsonObject());
        currentDetailsMachine.setLevel(jsonObject.get("level").getAsString());
        currentDetailsMachine.setNumberOfAllies(jsonObject.get("numberOfAllies").getAsInt());
        currentDetailsMachine.setSizeOfAlphabet(jsonObject.get("sizeOfAlphabet").getAsInt());
        currentDetailsMachine.setAlphabet(jsonObject.get("alphabet").getAsString());
        currentDetailsMachine.setUsedRotorsSize(jsonObject.get("usedRotorsSize").getAsInt());
        currentDetailsMachine.setDictionary(jsonObject.get("dictionary").getAsJsonArray());
        this.currentDetailsMachine=currentDetailsMachine;
        this.machinePageComponentController.setDetailsInChild(currentDetailsMachine);
        this.contestPageComponentController.setDetailsInChildMachinePageThree(currentDetailsMachine);


        //System.out.println(jsonObject.get("allReflectors"));

        response.close();

    }

    public void showMachineDetails() throws IOException {

        String finalUrl = HttpUrl
                .parse(MACHINE_DETAILS_PAGE)
                .newBuilder()
                .addQueryParameter("username", currentUser)
                .build()
                .toString();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(currentUser,currentUser)
                .build();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();
        Gson gson=new Gson();
        JsonParser jsonParser=new JsonParser();
        JsonElement jsonElement=jsonParser.parse(response.body().string());
        JsonObject jsonObject =jsonElement.getAsJsonObject();
        DataFromEngineToUiShowingTheMachine resultData=new DataFromEngineToUiShowingTheMachine(jsonObject.get("howManyRotorsTotal").getAsInt(),jsonObject.get("howManyUsedRotors").getAsInt(),jsonObject.get("totalReflectors").getAsInt(),jsonObject.get("howManyEncrypt").getAsInt(),jsonObject.get("firstStage").getAsString(),jsonObject.get("currentStage").getAsString());
        this.machinePageComponentController.showMachineDetails(resultData);
        this.numRotors=jsonObject.get("howManyRotorsTotal").getAsInt();
        this.numRotors=jsonObject.get("totalReflectors").getAsInt();
        this.contestPageComponentController.setManuallyCodeInTextField(resultData);

        //updateHttpStatusLine("New request is launched for: " + finalUrl);


     //   DataFromEngineToUiShowingTheMachine resultData = this.engine.displayingTheMachineSetting();
       // this.machinePageOneComponentController.showMachineDetails(resultData);
        //this.machinePageTwoComponentController.setManuallyCodeInTextField(resultData);
        //this.machinePageThreeComponentController.setManuallyCodeInTextField(resultData);
        response.close();
    }


    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginComponentController = fxmlLoader.getController();
            loginComponentController.setUBoatMainController(this);
            anchorPaneToShow.getChildren().add(loginComponent);

            //loginComponentController(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void setPageAfterLoginSuccess(){
        anchorPaneToShow.getChildren().remove(loginComponent);
        nameOfLoggedUserLabel.setText("Logged User: "+currentUser);


    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage=primaryStage;
    }

    public void setCurrentLoginAccount(String userName) {
        currentUser=new String();
        currentUser=userName;
    }

    @Override
    public void close() throws IOException {


    }

    public void setIsSet(boolean b) {
    }

    public String setCodeManuallyPop(ArrayList<String> inputs) throws IOException {
        String finalUrl = HttpUrl
                .parse(SET_ROTORS_PAGE)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("rotors",inputs.get(0))
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();
                    return;

                } else {
                    response.close();
                }
            }
        });

        finalUrl=HttpUrl
                .parse(SET_POSITIONS_PAGE)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("positions",inputs.get(1).toUpperCase())
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();
                    return;

                } else {
                    response.close();
                }
            }
        });
        finalUrl=HttpUrl
                .parse(SET_REFLECTOR_PAGE)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("reflector",inputs.get(2))
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();
                    return;

                } else {
                    response.close();
                }
            }
        });
        finalUrl=HttpUrl
                .parse(SET_PLUGS_PAGE)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("plugs",inputs.get(3).toUpperCase())
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();
                    return;

                } else {
                    response.close();
                }
            }
        });
        finalUrl=HttpUrl
                .parse(SET_ALL_MANUALLY_PAGE)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("rotors",inputs.get(0)).addQueryParameter("positions",inputs.get(1).toUpperCase())
                .addQueryParameter("reflector",inputs.get(2)).addQueryParameter("plugs",inputs.get(3).toUpperCase())
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();
                    return;

                } else {
                    response.close();

                    Platform.runLater(()->{
                        try {
                            displayMachineSettings();
                            allTabs.getSelectionModel().select(contestTab);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            showMachineDetails();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                }
            }
        });


        return "";
    }
    public void displayMachineSettings() throws IOException {
        String finalUrl = HttpUrl
                .parse(DISPLAY_MACHINE_SETTINGS)
                .newBuilder()
                .addQueryParameter("username", currentUser)
                .build()
                .toString();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(currentUser,currentUser)
                .build();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();
        response.close();
       //saveCurrentConfiguration=response.body().string();
       // String tmp=response.body().string();
       StringBuilder stringBuilder=new StringBuilder();
       stringBuilder.append("<");
       stringBuilder.append(this.machinePageComponentController.getRotorsFromTextArea());
       stringBuilder.append(">");
       stringBuilder.append("<");
       stringBuilder.append(this.machinePageComponentController.getStartingPosition());
       stringBuilder.append(">");
       stringBuilder.append("<");
       stringBuilder.append(this.machinePageComponentController.getReflectorFromTextArea());
       stringBuilder.append(">");
       saveCurrentConfiguration=stringBuilder.toString();

       this.contestPageComponentController.setConfiguration(saveCurrentConfiguration);
    }

    public void setRandomCodeListenerInChildHappened() throws IOException {

        //DataFromEngineToUi resultedData = this.engine.selectingAnInitialCodeConfigurationAutomatically();
        //this.machinePageComponentController.setRandomCodeInTextField(resultedData);
        String finalUrl = HttpUrl
                .parse(SET_MACHINE_RANDOM_PAGE)
                .newBuilder()
                .addQueryParameter("username", currentUser)
                .build()
                .toString();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(currentUser,currentUser)
                .build();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();
        saveCurrentConfiguration=response.body().string();
        String[]positions=saveCurrentConfiguration.split("\t");
        StringBuilder stringBuilder=new StringBuilder();
        String allOfTheConfig=positions[0];
        String[]allOfTheConfigStr=allOfTheConfig.split(">");
        stringBuilder.append(allOfTheConfigStr[0]+">"+"<"+positions[1]+">"+allOfTheConfigStr[2]+">");
        saveCurrentConfiguration=stringBuilder.toString();
        this.machinePageComponentController.setRandomCodeInTextField(positions[0]);
        this.contestPageComponentController.setRandomCodeInTextField(positions[0]);
        this.contestPageComponentController.setConfiguration(saveCurrentConfiguration);
        allTabs.getSelectionModel().select(contestTab);

    }
    public DataFromEngineToUiEncryptedString popForEncryption(String text) throws IOException {
        String finalUrl = HttpUrl
                .parse(ENCRYPT_STRING)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("encrypt",text)
                .build()
                .toString();
        RequestBody body = new MultipartBody.Builder().addFormDataPart(currentUser,currentUser)
                .build();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();
        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();
        DataFromEngineToUiEncryptedString resultedString=new DataFromEngineToUiEncryptedString("");
        if(response.code()!=200){
            resultedString.setEncryptedString(response.body().string());
            resultedString.setError(true);
        }else{
            resultedString.setEncryptedString(response.body().string());
            resultedString.setError(false);
        }
        return resultedString;
    }

    public void resetTheMachinePop() {

        String finalUrl = HttpUrl
                .parse(RESET_SETTINGS)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("reset","reset")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();
                    return;

                } else {
                    response.close();
                    Platform.runLater(()->{
                        try {
                            showMachineDetails();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        });
    }

    public void readyToStartContestUBoatClicked(String encryptedString) {
        String finalUrl = HttpUrl
                .parse(READY_TO_START_CONTEST_UBOAT)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("encrypted",encryptedString)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();
                    return;

                } else {
                    response.close();
                    Platform.runLater(()->{
                        //loadFileButton.disableProperty().set(true);
                        machineTab.disableProperty().set(true);
                        startRefresherForStartOfContest();
                        contestPageComponentController.uBoatIsReadyToStart();
                    });
                }
            }
        });
    }


    public void startRefresherForStartOfContest(){

        ContestStartedRefresherUBoat contestStartedRefresherUBoat=new ContestStartedRefresherUBoat(this::updatedStartOfContest,currentUser);

             timerContestStarted=new Timer();
            timerContestStarted.schedule(contestStartedRefresherUBoat, REFRESH_RATE, REFRESH_RATE);


    }
    public void startRefresherForCandidatesData(){
       CandidatesRefresherUBoat candidatesRefresherUBoat=new CandidatesRefresherUBoat(this::updatedCandidates,currentUser);
       timerCandidates=new Timer();
       timerCandidates.schedule(candidatesRefresherUBoat,REFRESH_RATE,REFRESH_RATE);
    }

    private void updatedCandidates(DataCandidatesToUBoat[] allCandidatesForUBoat) {
        this.contestPageComponentController.showAllCandidatesData(allCandidatesForUBoat);
    }

    private void updatedStartOfContest(String contestStarted) {
       // this.dashBoardComponentController.showTeamsAgentsData(allTeamsAgentsData);
        if(contestStarted.equals("true")){
            timerContestStarted.cancel();
            this.contestPageComponentController.contestStartedRightNow(true);
            startRefresherForCandidatesData();
        }else if(contestStarted.equals("false")){
            this.contestPageComponentController.contestStartedRightNow(false);
        }
    }

    public void foundTheWinner(String teamName) {

        timerCandidates.cancel();
        String finalUrl = HttpUrl
                .parse(FOUND_THE_WINNER_UBOAT)
                .newBuilder()
                .addQueryParameter("username", currentUser).addQueryParameter("teamname",teamName)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();
                    return;

                } else {
                    response.close();
                    Platform.runLater(()->{

                        System.out.println("byebye");
                    });
                }
            }
        });
    }

    public void logOutButtonClicked() throws IOException {

        String finalUrl = HttpUrl
                .parse(LOGOUT_UBOAT_PAGE)
                .newBuilder()
                .addQueryParameter("username", currentUser)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                return;
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();
                    return;

                } else {

                    Platform.runLater(()->{
                        machineTab.disableProperty().set(false);
                        allTabs.getSelectionModel().select(machineTab);
                        machineTab.disableProperty().set(true);
                        contestTab.disableProperty().set(true);
                        //allTabs.disableProperty().set(true);
                        loadFileButton.disableProperty().set(false);
                        fileLoaderTextProperty.set("");
                        labelFileSuccess.setText("");
                        //textFilePath.setText("");
                        loadLoginPage();
                        //anchorPaneToShow.getChildren().add(loginComponent);
                        //startAllAgain();

                        response.close();
                    });
                }
            }
        });


    }

    private void startAllAgain() {



    }
}
