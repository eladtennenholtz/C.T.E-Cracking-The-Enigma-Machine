package components.machineEncryptPage;

import components.main.MainController;
import data.DataFromEngineToUi;
import data.DataFromEngineToUiEncryptedString;
import data.DataFromEngineToUiShowingTheMachine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.CurrentMachineDetails;
import logic.MachineSettingStartManually;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MachinePageTwoController implements Initializable {

    private MainController mainController;
    private CurrentMachineDetails machineDetails;


    @FXML
    private TextArea currentMachineConfiguration;
    @FXML
    private TextArea inputStringTextArea;
    @FXML private TextArea encryptedStringTextArea;
    @FXML private TextArea staticsAndHistoryTextArea;
    @FXML
    private ComboBox<String> insertCodeStateComboBox;
    @FXML private Button processForContinuousButton;
    @FXML private Button clearTextAreaContinuousButton;
    @FXML private Button doneManualInputButton;
    @FXML private Button resetButton;
    @FXML private Button clearManualButton;

    private boolean done;
    private boolean isFirst;
    private int counterManual;
    private String ch;




    private SimpleStringProperty currentMachineConfigurationProperty;
    private SimpleStringProperty inputStringTextAreaProperty;
    private SimpleBooleanProperty processForContinuousButtonProperty;
    private SimpleBooleanProperty clearTextAreaContinuousButtonProperty;
    private SimpleBooleanProperty doneManualInputButtonProperty;
    private SimpleBooleanProperty resetButtonProperty;
    private SimpleBooleanProperty clearManualButtonProperty;



    public MachinePageTwoController() {
        this.currentMachineConfigurationProperty = new SimpleStringProperty();
        this.inputStringTextAreaProperty = new SimpleStringProperty();
        this.processForContinuousButtonProperty=new SimpleBooleanProperty();
        this.clearTextAreaContinuousButtonProperty=new SimpleBooleanProperty();
        this.doneManualInputButtonProperty=new SimpleBooleanProperty();
        this.resetButtonProperty=new SimpleBooleanProperty();
        this.clearManualButtonProperty=new SimpleBooleanProperty();
    }


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        //this.mainController.showStats();
        this.currentMachineConfiguration.textProperty().bind(currentMachineConfigurationProperty);
        //this.inputStringTextArea.textProperty().bind(inputStringTextAreaProperty);
        this.processForContinuousButton.disableProperty().bind(processForContinuousButtonProperty.not());
        this.insertCodeStateComboBox.disableProperty().set(true);
        this.clearTextAreaContinuousButton.disableProperty().bind(clearTextAreaContinuousButtonProperty.not());
        this.doneManualInputButton.disableProperty().bind(doneManualInputButtonProperty.not());
        this.resetButton.disableProperty().bind(resetButtonProperty.not());
        this.clearManualButton.disableProperty().bind(clearManualButtonProperty.not());
        done=false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      //rotorsChoiceBox.setOnAction(this::setRotorsChoiceBox);
        inputStringTextArea.setOnKeyTyped(this::keyPressedInput);

    }


    private void keyPressedInput(KeyEvent keyEvent) {

        if(insertCodeStateComboBox.getValue().equals("Manual")){
            if(inputStringTextArea.getText().length()==0){
                isFirst=true;

            }else{
                isFirst=false;
            }
            if(keyEvent.getCharacter().equals("\b")){
                if(inputStringTextArea.getText().length()==0){
                    encryptedStringTextArea.setText("");
                    return;
                }
                String sub=encryptedStringTextArea.getText().substring(0,encryptedStringTextArea.getText().length()-1);
                encryptedStringTextArea.setText(sub);
                return;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Reason:");


            this.ch=keyEvent.getCharacter();
            inputStringTextArea.editableProperty().set(true);
            DataFromEngineToUiEncryptedString data=this.mainController.popForEncryptionManual(keyEvent.getCharacter(),isFirst,done,"");

            if(data.getIsError()==true) {
                alert.setContentText(data.getEncryptedString());
                alert.showAndWait();
                inputStringTextArea.editableProperty().set(false);


            }else{
                inputStringTextArea.editableProperty().set(true);
                encryptedStringTextArea.appendText(data.getEncryptedString());

                //DataFromEngineToUi statsData=this.mainController.showHistoryAndStats();
                //staticsAndHistoryTextArea.setText(statsData.getResult());

                //this.mainController.showMachineDetails();
                //inputStringTextArea.editableProperty().set(false);

            }
        }else if(insertCodeStateComboBox.getValue().equals("Automatic")) {
            processForContinuousButtonProperty.set(true);
            clearTextAreaContinuousButtonProperty.set(true);
            doneManualInputButtonProperty.set(false);
            clearManualButtonProperty.set(false);
        }else if(insertCodeStateComboBox.getValue().equals("")){
            inputStringTextArea.editableProperty().set(false);
        }

    }

    public void setDetailsInChildMachinePageTwo() {

        currentMachineConfigurationProperty.set("");
        inputStringTextArea.disableProperty().set(true);
        processForContinuousButtonProperty.set(false);
        clearTextAreaContinuousButtonProperty.set(false);
        doneManualInputButtonProperty.set(false);
        clearManualButtonProperty.set(false);
        insertCodeStateComboBox.disableProperty().set(true);
        staticsAndHistoryTextArea.clear();
        inputStringTextArea.clear();
        encryptedStringTextArea.clear();
        done=false;

        this.machineDetails = this.mainController.getMachineDetails();


        ArrayList<Character> list = new ArrayList<>();
        String alphabet = machineDetails.getAlphabet();

        for (int i = 0; i < this.machineDetails.getAlphabet().length(); i++) {
            list.add(alphabet.charAt(i));
        }

            if(insertCodeStateComboBox.getItems().size()==0) {
                insertCodeStateComboBox.getItems().add(0, "");
                insertCodeStateComboBox.getItems().add(1, "Automatic");
                insertCodeStateComboBox.getItems().add(2, "Manual");
                insertCodeStateComboBox.getSelectionModel().select(0);

            }else{
                insertCodeStateComboBox.getItems().set(0, "");
                insertCodeStateComboBox.getItems().set(1, "Automatic");
                insertCodeStateComboBox.getItems().set(2, "Manual");
                insertCodeStateComboBox.getSelectionModel().select(0);
            }
    }



    public void setRandomCodeInTextField(DataFromEngineToUi resultedData) {
        this.currentMachineConfigurationProperty.set(resultedData.getResult());
        this.mainController.showMachineDetails();
        encryptedStringTextArea.clear();
        inputStringTextArea.clear();
        inputStringTextArea.disableProperty().set(true);
        processForContinuousButtonProperty.set(false);
        clearTextAreaContinuousButtonProperty.set(false);
        insertCodeStateComboBox.disableProperty().set(false);
        insertCodeStateComboBox.getSelectionModel().select(0);
        DataFromEngineToUi statsData=this.mainController.showHistoryAndStats();
        staticsAndHistoryTextArea.setText(statsData.getResult());
        counterManual=0;
        this.resetButtonProperty.set(true);
        done=false;
    }

    public void setManuallyCodeInTextField(DataFromEngineToUiShowingTheMachine resultData) {

        this.currentMachineConfigurationProperty.set(resultData.getCurrentStage());
        //inputStringTextArea.disableProperty().set(true);
        //encryptedStringTextArea.clear();
        //inputStringTextArea.clear();
        processForContinuousButtonProperty.set(false);
        //clearTextAreaContinuousButtonProperty.set(false);
        insertCodeStateComboBox.getSelectionModel().select(0);
        insertCodeStateComboBox.disableProperty().set(false);
        DataFromEngineToUi statsData=this.mainController.showHistoryAndStats();
        staticsAndHistoryTextArea.setText(statsData.getResult());
        counterManual=0;
        this.resetButtonProperty.set(true);
        done=false;
    }

    public void insertCodeStateComboBoxListener(ActionEvent actionEvent) {

        if (insertCodeStateComboBox.getValue().equals("Automatic")) {
            this.inputStringTextArea.disableProperty().set(false);
            this.inputStringTextArea.editableProperty().set(true);
            inputStringTextArea.clear();
            encryptedStringTextArea.clear();
            this.clearManualButtonProperty.set(false);
            this.doneManualInputButtonProperty.set(false);

            if(encryptedStringTextArea.getText().length()!=0){
                this.inputStringTextArea.editableProperty().set(false);
            }
                this.processForContinuousButtonProperty.set(true);

            this.clearTextAreaContinuousButtonProperty.set(true);

            //לפתוח כפתור  process

        } else if(insertCodeStateComboBox.getValue().equals("Manual")){
            this.inputStringTextArea.disableProperty().set(false);
            this.inputStringTextArea.editableProperty().set(true);
            inputStringTextArea.clear();
            encryptedStringTextArea.clear();
            this.doneManualInputButtonProperty.set(true);
            processForContinuousButtonProperty.set(false);
            clearTextAreaContinuousButtonProperty.set(false);
            this.clearManualButtonProperty.set(true);

            //ללחוץ עכשיו על "כאילו" proccess
            //לעשות fire לזה
        }else{
            //inputStringTextArea.editableProperty().set(false);
            doneManualInputButtonProperty.set(false);
            clearManualButtonProperty.set(false);
            processForContinuousButtonProperty.set(false);
            clearTextAreaContinuousButtonProperty.set(false);
            return;
        }
    }


    public void processForContinuousButtonListener(ActionEvent actionEvent) {
        if(inputStringTextArea.getText().equals("")){
            return;
        }
        DataFromEngineToUiEncryptedString res=this.mainController.popForEncryptionContinuous(inputStringTextArea.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");

        if(res.getIsError()==true) {
            alert.setContentText(res.getEncryptedString());
            alert.showAndWait();
        }else{
            encryptedStringTextArea.setText(res.getEncryptedString());
            DataFromEngineToUi statsData=this.mainController.showHistoryAndStats();
            staticsAndHistoryTextArea.setText(statsData.getResult());

            this.mainController.showMachineDetails();
            inputStringTextArea.editableProperty().set(false);

        }


    }

    public void makeStateComboBoxAccessible() {
        this.insertCodeStateComboBox.disableProperty().set(false);

    }



    public void clearTextAreaContinuousButtonListener(ActionEvent actionEvent) {

        inputStringTextArea.clear();
        encryptedStringTextArea.clear();
        processForContinuousButtonProperty.set(false);
        clearTextAreaContinuousButtonProperty.set(false);
        inputStringTextArea.editableProperty().set(false);
        insertCodeStateComboBox.getSelectionModel().select(0);
    }

    public void doneManualInputButtonListener(ActionEvent actionEvent) {
        if(!inputStringTextArea.getText().equals("")) {
            DataFromEngineToUiEncryptedString data = this.mainController.popForEncryptionManual(ch, isFirst, true, encryptedStringTextArea.getText());

            DataFromEngineToUi statsData = this.mainController.showHistoryAndStats();
            staticsAndHistoryTextArea.setText(statsData.getResult());
            this.mainController.showMachineDetails();
            inputStringTextArea.clear();
            encryptedStringTextArea.clear();
            processForContinuousButtonProperty.set(false);
            clearTextAreaContinuousButtonProperty.set(false);
            //inputStringTextArea.editableProperty().set(false);
            insertCodeStateComboBox.getSelectionModel().select(0);
        }



    }

    public void resetButtonListener(ActionEvent actionEvent) {

        this.mainController.resetTheMachinePop();
        inputStringTextArea.clear();
        encryptedStringTextArea.clear();


    }

    public void clearManualButtonListener(ActionEvent actionEvent) {

        inputStringTextArea.clear();
        encryptedStringTextArea.clear();
        processForContinuousButtonProperty.set(false);
        clearTextAreaContinuousButtonProperty.set(false);
        //inputStringTextArea.editableProperty().set(false);
        insertCodeStateComboBox.getSelectionModel().select(0);
    }
}