
package components.machineBruteForcePage;

import components.main.MainController;
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
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.CurrentMachineDetails;
import rotor.Rotor;

import java.net.URL;
import java.time.Duration;
import java.util.*;

public class MachinePageThreeController implements Initializable {


    private MainController mainController;
    private CurrentMachineDetails machineDetails;
    @FXML
    private TextArea currentMachineConfigurationTextArea;
    @FXML
    private TextArea inputTextArea;
    @FXML
    private TextArea outputTextArea;
    @FXML
    private TextField taskSizeTextField;
    @FXML
    private TextField numberOfAgentsTextField;
    @FXML
    private TextField levelTextField;
    @FXML
    private TextArea resultsFromDmTextArea;
    @FXML
    private TextArea percentInProgress;
    @FXML
    private TextArea totalTimeDm;
    @FXML
    private Slider agentsSlider;
    @FXML
    private Button startBruteForceButton;
    @FXML
    private Button encryptButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resumeButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button resetMachineButton;
    @FXML
    private Button showTotalTasksButton;

    @FXML
    private ChoiceBox<Integer> agentsChoiceBox;
    @FXML
    private ChoiceBox<String> levelChoiceBox;
    @FXML
    private HBox hBoxForSlider;
    @FXML
    private Slider sliderForAgents;
    @FXML
    private HBox HBoxDictionary;
    @FXML
    private HBox HBoxProgress;
    @FXML
    private ListView<String> listViewDictionary;
    @FXML
    private Label dmStringForDecode;
    @FXML
    private TextArea totalTasks;
    @FXML
    private ProgressBar progressBarDm;

    private HashSet<String>dictionaryPage;

    private SimpleStringProperty currentMachineConfigurationTextAreaProperty;
    private SimpleStringProperty resultsFromDmTextAreaProperty;
    private SimpleStringProperty taskSizeTextFieldProperty;
    private SimpleStringProperty numberOfAgentsTextFieldProperty;
    //private SimpleBooleanProperty startBruteForceButtonProperty;
    private SimpleBooleanProperty encryptButtonProperty;
    private SimpleBooleanProperty showTotalTasksButtonProperty;

    private ArrayList<Integer> allAgents;
    private ArrayList<String> levelsArray;
    private int numOfAgentsChosen;
    private long progress;
    boolean random;
    boolean res;



    public MachinePageThreeController() {

        this.currentMachineConfigurationTextAreaProperty = new SimpleStringProperty();
        //this.resultsFromDmTextAreaProperty=new SimpleStringProperty();
        this.taskSizeTextFieldProperty = new SimpleStringProperty();
        //this.numberOfAgentsTextFieldProperty=new SimpleStringProperty();
        //this.startBruteForceButtonProperty = new SimpleBooleanProperty();
        this.encryptButtonProperty = new SimpleBooleanProperty();
        this.showTotalTasksButtonProperty=new SimpleBooleanProperty();
    }


    public void setMainController(MainController mainController) {

        this.mainController = mainController;
        this.currentMachineConfigurationTextArea.textProperty().bind(currentMachineConfigurationTextAreaProperty);
        //this.resultsFromDmTextArea.textProperty().bind(resultsFromDmTextAreaProperty);
        // this.taskSizeTextField.textProperty().bind(taskSizeTextFieldProperty);
        //this.numberOfAgentsTextField.textProperty().bind(numberOfAgentsTextFieldProperty);
        //this.startBruteForceButton.disableProperty().bind(startBruteForceButtonProperty.not());
        //this.encryptButton.disableProperty().bind(encryptButtonProperty.not());
        this.showTotalTasksButton.disableProperty().bind(showTotalTasksButtonProperty.not());

    }

    public void setDetailsInChildMachinePageThree(CurrentMachineDetails machineDetails) {

        resultsFromDmTextArea.clear();
        levelChoiceBox.getItems().clear();
        currentMachineConfigurationTextAreaProperty.set("");
        taskSizeTextField.clear();
        numberOfAgentsTextField.setText("2");
        encryptButton.disableProperty().set(true);
        resetMachineButton.disableProperty().set(true);
        inputTextArea.clear();
        inputTextArea.editableProperty().set(false);
        outputTextArea.clear();
        dmStringForDecode.setText("");
        this.dictionaryPage=machineDetails.getDictionary();
        progress=0;
        percentInProgress.setText(String.valueOf(0)+"%");
        showTotalTasksButtonProperty.set(false);
        totalTasks.setText("");
        this.machineDetails=machineDetails;
        startBruteForceButton.disableProperty().set(true);
        pauseButton.disableProperty().set(true);
        resumeButton.disableProperty().set(true);
        stopButton.disableProperty().set(true);
        totalTimeDm.setText(String.valueOf(0));
        res=true;




        this.allAgents = new ArrayList<>();
        this.levelsArray = new ArrayList<>();


        for (int i = 2; i <= machineDetails.getNumberOfAgents(); i++) {
            allAgents.add(i);
        }

        levelsArray.add("easy");
        levelsArray.add("medium");
        levelsArray.add("hard");
        levelsArray.add("impossible");
        levelChoiceBox.getItems().addAll(levelsArray);
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
              int index=listViewDictionary.getSelectionModel().getSelectedIndex();
              inputTextArea.setText(listViewDictionary.getItems().get(index));
              outputTextArea.setText("");
              //startBruteForceButton.disableProperty().set(true);
              //stopButton.disableProperty().set(true);
          }
      });
        HBoxDictionary.getChildren().remove(0);
        HBoxDictionary.getChildren().addAll(dictionaryEdit);

        //listViewRotors.setItems(observableList);
        //HBoxRotors.getChildren().addAll(rotorEdit);
        sliderForAgents = new Slider(2, machineDetails.getNumberOfAgents(), 0);

        sliderForAgents.setMin(2);
        sliderForAgents.setMax(machineDetails.getNumberOfAgents());
        sliderForAgents.showTickLabelsProperty().set(true);
        //sliderForAgents.showTickMarksProperty().set(true);
        sliderForAgents.blockIncrementProperty().set(1);
        sliderForAgents.majorTickUnitProperty().set(1);
        sliderForAgents.setPrefWidth(550);
        hBoxForSlider.getChildren().remove(0);
        hBoxForSlider.getChildren().addAll(sliderForAgents);


        sliderForAgents.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                long number = Math.round(sliderForAgents.getValue());
                numberOfAgentsTextField.setText(String.valueOf(number));
                //numberOfAgentsTextField.setText(String.valueOf(Math.floor(sliderForAgents.getValue())));

            }
        });

        progressBarDm=new ProgressBar();
        progressBarDm.setProgress(0);


        HBoxProgress.getChildren().remove(0);
        HBoxProgress.getChildren().addAll(progressBarDm);


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        levelChoiceBox.setOnAction(this::setLevelChoiceBox);
        taskSizeTextField.setOnKeyTyped(this::keyPressedInput);
    }




    private void setLevelChoiceBox(ActionEvent actionEvent) {

        //if(!levelChoiceBox.getValue().isEmpty() && !numberOfAgentsTextField.getText().isEmpty()&& !taskSizeTextField.getText().isEmpty()){
            //totalTasks.setText(Integer.valueOf(numberOfAgentsTextField.getText())*);
          //  startBruteForceButtonProperty.set(true);


            //if(levelChoiceBox.getValue()=="easy"){
                //double res=Math.pow(machineDetails.getAlphabet().length(),machineDetails.getUsedRotorsSize());
                //System.out.println(res/Integer.valueOf(taskSizeTextField.getText()));
            //}
        //}

    }


    public void startBruteForceButtonListener(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");
        resultsFromDmTextArea.setText("");
        totalTimeDm.setText(String.valueOf(0));


        if(numberOfAgentsTextField.getText().isEmpty()){

            alert.setContentText("Make sure that you inserted all the data:\n" +
                    "1. Agents\n" +
                    "2. Level\n" +
                    "3. Task size");
            alert.showAndWait();
            return;
        }
        if(levelChoiceBox.getValue()!="easy" && levelChoiceBox.getValue()!="medium"&& levelChoiceBox.getValue()!="hard" && levelChoiceBox.getValue()!="impossible"){
            alert.setContentText("Make sure that you inserted all the data:\n" +
                    "1. Agents\n" +
                    "2. Level\n" +
                    "3. Task size");
            alert.showAndWait();
            return;
        }
        if(taskSizeTextField.getText().isEmpty()){
            alert.setContentText("Make sure that you inserted all the data:\n" +
                    "1. Agents\n" +
                    "2. Level\n" +
                    "3. Task size");
            alert.showAndWait();
            return;
        }
        if(progress!=100&&progress!=99&& !stopButton.isDisabled()&&pauseButton.isDisabled()){
            alert.setContentText("The Decryption is still running. press stop if you would like to run another decryption.");
            alert.showAndWait();
            stopButton.disableProperty().set(true);
            return;
        }


        if(!totalTasks.getText().equals("")) {
            this.mainController.startBruteForceButtonPop(outputTextArea.getText(), Integer.valueOf(numberOfAgentsTextField.getText()), levelChoiceBox.getValue(), Integer.valueOf(taskSizeTextField.getText()), Integer.valueOf(totalTasks.getText()));
        }else{
            int res = (int) Math.pow(machineDetails.getSizeOfAlphabet(), machineDetails.getUsedRotorsSize());
            int f,fact=1;
            int number=machineDetails.getUsedRotorsSize();//It is the number to calculate factorial
            int tasksToSend=0;
            for(f=1;f<=number;f++){
                fact=fact*f;
            }
            if(levelChoiceBox.getValue().equals("easy")) {

                    tasksToSend=res/ Integer.valueOf(taskSizeTextField.getText());

            } else if (levelChoiceBox.getValue().equals("medium")) {
                    tasksToSend=res * machineDetails.getAllReflectors().size()/Integer.valueOf(taskSizeTextField.getText());

            }else if(levelChoiceBox.getValue().equals("hard")){

                    tasksToSend=res*machineDetails.getAllReflectors().size()*fact/Integer.valueOf(taskSizeTextField.getText());
            }else if(levelChoiceBox.getValue().equals("impossible")){
                    tasksToSend=res * machineDetails.getAllReflectors().size() * fact * generate(machineDetails.getAllRotors().size(), machineDetails.getUsedRotorsSize()) / Integer.valueOf(taskSizeTextField.getText());
            }
            this.mainController.startBruteForceButtonPop(outputTextArea.getText(), Integer.valueOf(numberOfAgentsTextField.getText()), levelChoiceBox.getValue(), Integer.valueOf(taskSizeTextField.getText()), tasksToSend);
        }
        //startBruteForceButtonProperty.set(false);
        if(!stopButton.isDisabled()) {
           // startBruteForceButton.disableProperty().set(true);
        }
        pauseButton.disableProperty().set(false);
        resumeButton.disableProperty().set(false);
        if(stopButton.isDisabled()) {
            stopButton.disableProperty().set(false);
        }
    }


    public void taskSizeTextFieldListener(ActionEvent actionEvent) {

        if(!taskSizeTextField.textProperty().isEmpty().getValue()){
            //startBruteForceButtonProperty.set(true);
            startBruteForceButton.disableProperty().set(false);

        }


        //if(taskSizeTextField.getText().isEmpty()){
          //  startBruteForceButtonProperty.set(true);
        //}else{
          //  startBruteForceButtonProperty.set(false);
        //}


        //if(Integer.taskSizeTextField.textProperty().getValue())
        //System.out.println(Integer.decode(taskSizeTextField.getText()));
        //System.out.println("hey!");
    }

    private void keyPressedInput(KeyEvent keyEvent) {


        try {
            int x = Integer.parseInt(keyEvent.getCharacter());
            taskSizeTextField.editableProperty().set(true);
        } catch (NumberFormatException e) {

            if(keyEvent.getCharacter().equals("\b")){
                taskSizeTextField.editableProperty().set(true);
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


        } catch (NumberFormatException e) {
            if(keyEvent.getCharacter().equals("\b")){
                taskSizeTextField.editableProperty().set(true);
                return;
            }

            alert.setContentText("Input is not an int value");
            alert.showAndWait();
        }


    }

    public void setDataFromDmInTextArea(DataFromAgentToDm dataFromAgentToDm) {

        //resultsFromDmTextArea.setText(dataFromAgentToDm.getConfiguration());
        Platform.runLater(() -> {
            StringBuilder stringBuilder = new StringBuilder();
            //stringBuilder.append(dataFromAgentToDm.getConfiguration() + dataFromAgentToDm.getNumberOfThread() + "\n");
            stringBuilder.append("Agent: ( " +dataFromAgentToDm.getNumberOfThread()+" ) , Decrypted :  ( "+dataFromAgentToDm.getDecrypted().toLowerCase() + " ) , Config: "+dataFromAgentToDm.getConfiguration()+"\n" );
            //resultsFromDmTextArea.appendText(dataFromAgentToDm.getConfiguration());
            //resultsFromDmTextArea.appendText("\n");
            //resultsFromDmTextArea.appendText(String.valueOf(dataFromAgentToDm.getNumberOfThread()));
            resultsFromDmTextArea.appendText(stringBuilder.toString());
        });


    }

    public void setRandomCodeInTextField(DataFromEngineToUi resultedData) {

        //this.currentMachineConfigurationProperty.set(resultData.getCurrentStage());
        this.currentMachineConfigurationTextAreaProperty.set(resultedData.getResult());
        this.encryptButton.disableProperty().set(false);
        inputTextArea.editableProperty().set(true);
        resetMachineButton.disableProperty().set(false);
        showTotalTasksButtonProperty.set(true);
        //encryptButton.disableProperty().set(true);
        random=true;


    }

    public void setManuallyCodeInTextField(DataFromEngineToUiShowingTheMachine resultData) {
        this.currentMachineConfigurationTextAreaProperty.set(resultData.getCurrentStage());
        this.encryptButton.disableProperty().set(false);
        inputTextArea.editableProperty().set(true);
        resetMachineButton.disableProperty().set(false);
        showTotalTasksButtonProperty.set(true);

        random=false;
    }

    public void encryptButtonListener(ActionEvent actionEvent) {

        if (inputTextArea.getText().equals("")) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");


        String[]allOfThemStr=inputTextArea.getText().toLowerCase().split(" ");
        for(int i=0;i<allOfThemStr.length;i++){
            if(!this.dictionaryPage.contains(allOfThemStr[i])){
                alert.setContentText("The word "+allOfThemStr[i]+" is not part of the dictionary.");
                alert.showAndWait();
                return;
            }
        }

        //if(!this.dictionaryPage.contains(inputTextArea.getText().toLowerCase())){
          //  alert.setContentText("The string is not part of the dictionary.");
            //alert.showAndWait();
            //return;
        //}


        DataFromEngineToUiEncryptedString  res = this.mainController.popForEncryptionContinuous(inputTextArea.getText());




        if (res.getIsError() == true) {
            alert.setContentText(res.getEncryptedString());
            alert.showAndWait();
        } else {
            outputTextArea.setText(res.getEncryptedString());
            DataFromEngineToUi statsData = this.mainController.showHistoryAndStats();
            //staticsAndHistoryTextArea.setText(statsData.getResult());

            this.mainController.showMachineDetails();
            //inputTextArea.editableProperty().set(false);
            //startBruteForceButtonProperty.set(true);
            startBruteForceButton.disableProperty().set(false);
            dmStringForDecode.setText(outputTextArea.getText());


        }
    }

    public void resetMachineButtonListener(ActionEvent actionEvent) {

        this.mainController.resetTheMachinePop();
        //dmStringForDecode.setText("");
        //inputTextArea.clear();
        //outputTextArea.clear();
    }

    public void showTotalTasksListener(ActionEvent actionEvent) {
        if(!numberOfAgentsTextField.getText().equals("")&&!levelChoiceBox.getValue().equals("")&&!taskSizeTextField.getText().equals("")){

            int res = (int) Math.pow(machineDetails.getSizeOfAlphabet(), machineDetails.getUsedRotorsSize());
            int f,fact=1;
            int number=machineDetails.getUsedRotorsSize();//It is the number to calculate factorial
            for(f=1;f<=number;f++){
                fact=fact*f;
            }
            if(levelChoiceBox.getValue().equals("easy")) {
                if(res<Integer.valueOf(taskSizeTextField.getText())){
                    totalTasks.setText(String.valueOf(res));
                }else {
                    totalTasks.setText(String.valueOf(res / Integer.valueOf(taskSizeTextField.getText())));
                }
            } else if (levelChoiceBox.getValue().equals("medium")) {
                if(res*machineDetails.getAllReflectors().size()<Integer.valueOf(taskSizeTextField.getText())){
                    totalTasks.setText(String.valueOf(res*machineDetails.getAllReflectors().size()));
                }else {
                    totalTasks.setText(String.valueOf(res * machineDetails.getAllReflectors().size()/Integer.valueOf(taskSizeTextField.getText())));
                }

            }else if(levelChoiceBox.getValue().equals("hard")){
                //totalTasks.setText(String.valueOf(res*machineDetails.getAllReflectors().size()*fact));
                if(res*machineDetails.getAllReflectors().size()*fact<Integer.valueOf(taskSizeTextField.getText())){
                    totalTasks.setText(String.valueOf(res*machineDetails.getAllReflectors().size()*fact));
                }else{
                    totalTasks.setText(String.valueOf(res*machineDetails.getAllReflectors().size()*fact/Integer.valueOf(taskSizeTextField.getText())));
                }

            }else if(levelChoiceBox.getValue().equals("impossible")){

                if(res*machineDetails.getAllReflectors().size()*fact*generate(machineDetails.getAllRotors().size(),machineDetails.getUsedRotorsSize())<Integer.valueOf(taskSizeTextField.getText())){
                    totalTasks.setText(String.valueOf(res*machineDetails.getAllReflectors().size()*fact*generate(machineDetails.getAllRotors().size(),machineDetails.getUsedRotorsSize())));

                }else {
                    totalTasks.setText(String.valueOf(res * machineDetails.getAllReflectors().size() * fact * generate(machineDetails.getAllRotors().size(), machineDetails.getUsedRotorsSize()) / Integer.valueOf(taskSizeTextField.getText())));
                }
            }
        }

    }  public static int generate(int n, int r) {
        ArrayList<int[]> combinations = new ArrayList<>();
        int[] combination = new int[r];

        // initialize with lowest lexicographic combination
        for (int i = 0; i < r; i++) {
            combination[i] = i;
        }

        while (combination[r - 1] < n) {
            combinations.add(combination.clone());

            // generate next combination in lexicographic order
            int t = r - 1;
            while (t != 0 && combination[t] == n - r + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < r; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }

        return combinations.size();
    }

    public void setDataFromDmInTaskProgress(Double num) {

        Platform.runLater(()->{
            //progressBarDm.setProgress(progressBarDm.getProgress()+0.01);

            progressBarDm.setProgress(num);
            percentInProgress.setText(String.valueOf(Math.round(num*100))+"%");
            progress=Math.round(num*100);


        });

    }


    public void pauseButtonListener(ActionEvent actionEvent) {
        this.mainController.pausePop();
    }

    public void resumeButtonListener(ActionEvent actionEvent) {
        this.mainController.resumePop();
    }

    public void stopButtonListener(ActionEvent actionEvent) throws InterruptedException {
        this.mainController.stopPop();
        //resultsFromDmTextArea.setText("");
        progressBarDm=new ProgressBar();
        progressBarDm.setProgress(0);
        startBruteForceButton.disableProperty().set(false);
        stopButton.disableProperty().set(true);
        pauseButton.disableProperty().set(true);
        resumeButton.disableProperty().set(true);
        //totalTimeDm.setText(String.valueOf(0)+"%");
        HBoxProgress.getChildren().remove(0);
        HBoxProgress.getChildren().addAll(progressBarDm);

    }

    public long getProgress() {
        return progress;
    }

    public void setProgress() {
        progressBarDm.setProgress(0);
        progress=0;
    }

    public void setDataFromDmInTotalTimeDm(Long dataOfTime) {

        Platform.runLater(()->{
            if(totalTimeDm.getText().equals("")){
                totalTimeDm.setText(String.valueOf(dataOfTime));
            }else {
                long tmp = Long.valueOf(totalTimeDm.getText());
                long another = tmp + dataOfTime;
                totalTimeDm.setText(String.valueOf(another));
            }
        });

    }
}
