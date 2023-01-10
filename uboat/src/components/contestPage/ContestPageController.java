package components.contestPage;

import components.main.AllCandidatesToUBoatTableView;
import components.main.UBoatMainController;
import data.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ContestPageController implements Initializable {
    private UBoatMainController uboatMainController;
    private CurrentDetailsMachine machineDetails;
    private String winningConfiguration;

    @FXML
    private TextArea currentMachineConfigurationTextArea;
    @FXML
    private TextArea inputTextArea;
    @FXML
    private TextArea outputTextArea;
    @FXML
    private Button encryptButton;
    @FXML
    private Button resetMachineButton;
    @FXML
    private Button readyButton;
    @FXML
    private Button logoutButton;
    @FXML
    private HBox HBoxDictionary;
    @FXML
    private ListView<String> listViewDictionary;
    @FXML
    private Label readyToStartLabel;
    @FXML
    private Label contestStartedLabel;
    @FXML
    private HBox hBoxCandidates;
    @FXML
    private TableView<AllCandidatesToUBoatTableView> tableViewCandidates;
    @FXML
    private TableColumn colDecrypt;
    @FXML
    private TableColumn colTeam;
    @FXML
    private TableColumn colCode;
    @FXML
    private Label winnerOfContestLabel;


    private HashSet<String> dictionaryPage;
    private String encryptedStringForContest;


    boolean random;
    private SimpleStringProperty currentMachineConfigurationTextAreaProperty;
    private SimpleBooleanProperty encryptButtonProperty;
    private final ObservableList<AllCandidatesToUBoatTableView> data=FXCollections.observableArrayList();

    public ContestPageController() {
        this.currentMachineConfigurationTextAreaProperty = new SimpleStringProperty();
        this.encryptButtonProperty = new SimpleBooleanProperty();
    }

    public void setMainController(UBoatMainController uboatMainController) {

        this.uboatMainController = uboatMainController;

        this.currentMachineConfigurationTextArea.textProperty().bind(currentMachineConfigurationTextAreaProperty);
        colDecrypt.setCellValueFactory(
                new PropertyValueFactory<AllCandidatesToUBoatTableView, String>("colDecrypt"));

        colTeam.setCellValueFactory(
                new PropertyValueFactory<AllCandidatesToUBoatTableView, String>("colTeam"));


        colCode.setCellValueFactory(
                new PropertyValueFactory<AllCandidatesToUBoatTableView, String>("colCode"));
    }


    public void setDetailsInChildMachinePageThree(CurrentDetailsMachine machineDetails) {

        currentMachineConfigurationTextAreaProperty.set("");
        encryptButton.disableProperty().set(true);
        readyButton.disableProperty().set(true);
        logoutButton.disableProperty().set(true);
        resetMachineButton.disableProperty().set(true);
        inputTextArea.clear();
        inputTextArea.editableProperty().set(false);
        outputTextArea.clear();
        this.dictionaryPage = machineDetails.getDictionary();
        listViewDictionary = new ListView<>();
        VBox dictionaryEdit = new VBox();
        //dictionaryEdit.setId(rotorsChoiceBox.getValue().toString());
        Label dictionaryIdLabel = new Label();
        //rotorIdLabel.setLabelFor(listViewRotors);
        //rotorIdLabel.setText(rotorsChoiceBox.getValue().toString());
        dictionaryEdit.getChildren().add(dictionaryIdLabel);
        dictionaryEdit.getChildren().add(listViewDictionary);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addListener(new ListChangeListener() {
            @Override
//onChanged method
            public void onChanged(ListChangeListener.Change c) {

            }
        });


        TreeSet myTreeSet = new TreeSet(machineDetails.getDictionary());
        observableList.addAll(myTreeSet);

        listViewDictionary.setItems(observableList);
        listViewDictionary.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = listViewDictionary.getSelectionModel().getSelectedIndex();
                inputTextArea.setText(listViewDictionary.getItems().get(index));
                outputTextArea.setText("");
                readyButton.disableProperty().set(true);
                //startBruteForceButton.disableProperty().set(true);
                //stopButton.disableProperty().set(true);
            }
        });
        HBoxDictionary.getChildren().remove(0);
        HBoxDictionary.getChildren().addAll(dictionaryEdit);

    }

    public void encryptButtonListener(ActionEvent actionEvent) throws IOException {
        if (inputTextArea.getText().equals("")) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");
        String[] allOfThemStr = inputTextArea.getText().toLowerCase().split(" ");
        for (int i = 0; i < allOfThemStr.length; i++) {
            if (!this.dictionaryPage.contains(allOfThemStr[i])) {
                alert.setContentText("The word " + allOfThemStr[i] + " is not part of the dictionary.");
                alert.showAndWait();
                return;
            }
        }

        DataFromEngineToUiEncryptedString res = this.uboatMainController.popForEncryption(inputTextArea.getText());

        if (res.getIsError()) {
            alert.setContentText(res.getEncryptedString());
            alert.showAndWait();
        } else {
            outputTextArea.setText(res.getEncryptedString());
            encryptedStringForContest = outputTextArea.getText();
            readyButton.disableProperty().set(false);
            this.uboatMainController.showMachineDetails();
        }
    }

    public void resetMachineButtonListener(ActionEvent actionEvent) {
        this.uboatMainController.resetTheMachinePop();
        if (outputTextArea.getText().equals("")) {
            readyButton.disableProperty().set(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setRandomCodeInTextField(String resultedData) {

        //this.currentMachineConfigurationProperty.set(resultData.getCurrentStage());
        this.currentMachineConfigurationTextAreaProperty.set(resultedData);
        this.encryptButton.disableProperty().set(false);
        inputTextArea.editableProperty().set(true);
        resetMachineButton.disableProperty().set(false);
        //encryptButton.disableProperty().set(true);
        random = true;
    }

    public void setManuallyCodeInTextField(DataFromEngineToUiShowingTheMachine resultData) {
        this.currentMachineConfigurationTextAreaProperty.set(resultData.getCurrentStage());
        this.encryptButton.disableProperty().set(false);
        inputTextArea.editableProperty().set(true);
        resetMachineButton.disableProperty().set(false);
        random = false;
    }

    public void readyButtonListener(ActionEvent actionEvent) {
        encryptButton.disableProperty().set(true);
        //this.uboatMainController.readyToStartContestUBoatClicked(encryptedStringForContest);
        this.uboatMainController.readyToStartContestUBoatClicked(outputTextArea.getText());
        readyButton.disableProperty().set(true);
        resetMachineButton.disableProperty().set(true);
    }

    public void logoutButtonListener(ActionEvent actionEvent) throws IOException {
        contestStartedLabel.setText("");
        winnerOfContestLabel.setText("");
        readyToStartLabel.setText("");
        tableViewCandidates.getItems().clear();
        logoutButton.disableProperty().set(true);

        this.uboatMainController.logOutButtonClicked();
    }

    public void uBoatIsReadyToStart() {
        readyToStartLabel.setText("Ready to start!");
    }

    public void contestStartedRightNow(boolean started) {
        Platform.runLater(() -> {
            if (started) {
                contestStartedLabel.setTextFill(Color.GREEN);
                contestStartedLabel.setText("Contest started!");
            } else {
                contestStartedLabel.setTextFill(Color.RED);
                contestStartedLabel.setText("Contest hasn't started yet.");
                return;
            }
        });

    }

    public void showAllCandidatesData(DataCandidatesToUBoat[] allCandidatesForUBoat) {
        Platform.runLater(() -> {
            //AllCandidatesToUBoatTableView[]allCandidatesToUBoatTableViewsArray=new AllCandidatesToUBoatTableView[allCandidatesForUBoat.length];
            for(int i=0;i<allCandidatesForUBoat.length;i++){
                if(allCandidatesForUBoat[i]!=null){
                    String config=convertToTheConfig(allCandidatesForUBoat[i].getConfiguration());
                    data.add(new AllCandidatesToUBoatTableView(allCandidatesForUBoat[i].getDecrypt(),allCandidatesForUBoat[i].getTeamName(),config));
                    if(config.equals(winningConfiguration)){
                        this.uboatMainController.foundTheWinner(allCandidatesForUBoat[i].getTeamName());
                        winnerOfContestLabel.setText(allCandidatesForUBoat[i].getTeamName());
                        contestStartedLabel.setText("Contest finished.");
                        logoutButton.disableProperty().set(false);
                        break;
                    }
                    //data.add(new AllCandidatesToUBoatTableView(allCandidatesForUBoat[i].getDecrypt(),allCandidatesForUBoat[i].getTeamName(),convertToTheConfig(allCandidatesForUBoat[i].getConfiguration())));
                }

            }
            tableViewCandidates.setItems(data);
            colDecrypt.setCellValueFactory(
                    new PropertyValueFactory<AllCandidatesToUBoatTableView, String>("colDecrypt"));

            colTeam.setCellValueFactory(
                    new PropertyValueFactory<AllCandidatesToUBoatTableView, String>("colTeam"));


            colCode.setCellValueFactory(
                    new PropertyValueFactory<AllCandidatesToUBoatTableView, String>("colCode"));

        });
    }


    public String convertToTheConfig(String configuration){

        String[]result=configuration.split("\t");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("<"+result[0]+">"+"<"+result[1]+">"+"<"+result[2]+">");
        return stringBuilder.toString();
    }


    public void setConfiguration(String saveCurrentConfiguration) {
        this.winningConfiguration=saveCurrentConfiguration;




    }
    public boolean checkIfWinningConfiguration(String configuration){



        return false;
    }
    public String getTextAreaMachineDetailsForRandom(){
        return currentMachineConfigurationTextArea.getText();
    }
}