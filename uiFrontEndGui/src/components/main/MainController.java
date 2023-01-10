package components.main;


import components.machineBruteForcePage.MachinePageThreeController;
import components.machineEncryptPage.MachinePageTwoController;
import components.machineSettingPage.MachinePageOneController;
import data.*;
import dm.DmManager;
import exceptionFromUserInput.*;
import exceptionsFromXml.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.CurrentMachineDetails;
import logic.Engine;
import logic.EngineFirst;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MainController {

    private Engine engine;
    private Stage primaryStage;
    public CurrentMachineDetails machineDetails;
    private boolean isSet;
    private ArrayList<String> inputs;
    private ArrayList<String>rotorsInput;
    private ArrayList<String>positionsInput;
    private ArrayList<String>reflectorInput;
    private ArrayList<String>plugBoardInput;
    private DmManager dmManager;
    private Thread helper;
    boolean decryptionHappened;


    @FXML
    private ScrollPane machinePageOneComponent;
    @FXML
    private MachinePageOneController machinePageOneComponentController;
    @FXML
    private ScrollPane machinePageTwoComponent;
    @FXML
    private MachinePageTwoController machinePageTwoComponentController;

    @FXML
    private ScrollPane machinePageThreeComponent;
    @FXML
    private MachinePageThreeController machinePageThreeComponentController;
    @FXML
    private TextArea textFilePath;
    @FXML
    private TabPane allTabs;
    @FXML
    private Tab machineFirstPageTab;

    private SimpleStringProperty fileLoaderTextProperty;
    private SimpleBooleanProperty isFileLoadedProperty;


    public MainController() {

        fileLoaderTextProperty = new SimpleStringProperty();
        isFileLoadedProperty = new SimpleBooleanProperty();

    }

    @FXML
    public void initialize() {
        if (machinePageOneComponentController != null) {
            machinePageOneComponentController.setMainController(this);
        }
        if(machinePageTwoComponentController!=null){
            machinePageTwoComponentController.setMainController(this);
        }
        if(machinePageThreeComponentController!=null){
            machinePageThreeComponentController.setMainController(this);
        }

        textFilePath.textProperty().bind(fileLoaderTextProperty);
        allTabs.disableProperty().bind(isFileLoadedProperty.not());


    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setEngineMachine() {
        this.engine = new EngineFirst();
        this.decryptionHappened=false;
       // this.dmManager=new
    }

    public void loadFileButtonListener(ActionEvent actionEvent) throws InterruptedException {
        long mainProgress=this.machinePageThreeComponentController.getProgress();
        if(mainProgress==100||mainProgress==99){
            this.dmManager.stopBruteForce();
            this.machinePageThreeComponentController.setProgress();
            decryptionHappened=false;
        }

        Alert alertD = new Alert(Alert.AlertType.INFORMATION);
        alertD.setTitle("Error");
        alertD.setHeaderText("Reason:");
        if(decryptionHappened==true){
            alertD.setContentText("Please wait until the decryption end's or press stop to stop the decryption.");
            alertD.showAndWait();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        String absolutePath = selectedFile.getAbsolutePath();
        fileLoaderTextProperty.set(absolutePath);
        this.inputs = new ArrayList<>();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");

        try {
            this.engine.readTheMachineXmlFile(new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            });
            isFileLoadedProperty.set(true);
            this.isSet=false;
            showMachineDetails();
            setTheMachineDetails();

        } catch (FileDoesNotExistException e) {
            alert.setContentText("File Does Not Exist");
            alert.showAndWait();
        } catch (RotorsCountException e) {
            alert.setContentText(e.getAnswer());
            alert.showAndWait();
        } catch (AlphabetSizeException e) {
            alert.setContentText(e.getAnswer());
            alert.showAndWait();
        } catch (IdRotorException e) {
            alert.setContentText("Id Rotor problem");
            alert.showAndWait();
        } catch (MappingRotorException e) {
            alert.setContentText("Rotor mapped wrong");
            alert.showAndWait();
        } catch (NotchException e) {
            alert.setContentText("Notch problem");
            alert.showAndWait();
        } catch (IdReflectorException e) {
            alert.setContentText(e.getAnswer());
            alert.showAndWait();
        } catch (MappingReflectorException e) {
            alert.setContentText("Reflector mapped to the same place");
            alert.showAndWait();
        }
        catch (ExceptionAll e){
            alert.setContentText(e.getResult());
            alert.showAndWait();
        }
        catch (Exception e){
            alert.setContentText("That is not a legal Xml schema for this specific exam");
            alert.showAndWait();
        }

    }

    public void showMachineDetails() {
        DataFromEngineToUiShowingTheMachine resultData = this.engine.displayingTheMachineSetting();
        this.machinePageOneComponentController.showMachineDetails(resultData);
        this.machinePageTwoComponentController.setManuallyCodeInTextField(resultData);
        this.machinePageThreeComponentController.setManuallyCodeInTextField(resultData);
    }

    public void setRandomCodeListenerInChildHappened() {
        DataFromEngineToUi resultedData = this.engine.selectingAnInitialCodeConfigurationAutomatically();
        this.machinePageOneComponentController.setRandomCodeInTextField(resultedData);
        this.machinePageTwoComponentController.setRandomCodeInTextField(resultedData);
        this.machinePageThreeComponentController.setRandomCodeInTextField(resultedData);

    }

    public void setTheMachineDetails() {

        CurrentMachineDetails machineDetails = this.engine.getDetails();
        this.machinePageOneComponentController.setDetailsInChildMachinePageOne(machineDetails);
        this.machineDetails=machineDetails;
        this.machinePageTwoComponentController.setDetailsInChildMachinePageTwo();
        this.machinePageThreeComponentController.setDetailsInChildMachinePageThree(machineDetails);

    }

    public String setCodeManuallyPop(ArrayList<String> inputs) {
        //ArrayList<String>inputs=new ArrayList<>();
        DataFromUiToEngineInputRotors data=new DataFromUiToEngineInputRotors(inputs.get(0));

        try {
            DataFromEngineToUiRotors resultData = this.engine.settingRotorsToMachine(data);;

        } catch (RotorExceptionsFromUi exceptions) {
            return exceptions.getAnswer();
        }
        DataFromUiToEngineInputRotorsPositioning dataPos = new DataFromUiToEngineInputRotorsPositioning(inputs.get(1).toUpperCase());
        try {
            DataFromEngineToUiRotorsPositioning resultData=engine.settingPositionsToMachine(dataPos);


        } catch (RotorPositionsExceptionFromUi e) {
            return e.getAnswer();
        }
        DataFromUiToEngineReflector dataReflector=new DataFromUiToEngineReflector(inputs.get(2));

        try {
            DataFromEngineToUiReflector resultData=engine.settingReflectors(dataReflector);


        }catch (ReflectorExceptionsFromUi e){
            return e.getAnswer();
        }
        DataFromUiToEngineInputPlugBoard dataPlugBoard = new DataFromUiToEngineInputPlugBoard(inputs.get(3).toUpperCase());
        try {
            DataFromEngineToUiPlugBoard resultData= engine.settingPlugBoard(dataPlugBoard);
            //System.out.println(resultData.getAnswer());
        }catch (PlugBoardExceptionsFromUi e){
            //System.out.println(e.getAnswer());
            return e.getAnswer();
        }
        engine.settingAllManually(inputs);
        DataFromEngineToUiShowingTheMachine resultData=this.engine.displayingTheMachineSetting();
        this.machinePageOneComponentController.setTheManuallyConfiguration(resultData);
        showMachineDetails();

        //String result="aabbccddeeff";

        //DataFromUiToEngineStringToEncrypt dataEncrypt = new DataFromUiToEngineStringToEncrypt(result.toUpperCase());
        //try {
            //DataFromEngineToUiEncryptedString resultedString = engine.inputProcessing(dataEncrypt);
            //System.out.println("The encrypted string: "+resultedString.getEncryptedString());

        //} catch (InputStringToEncryptException e) {
           // System.out.println("fuck!");
            //return e.getAnswer();
        //}

     return "";
    }


    public boolean checkIfRotorsAreLegal(String resultRotors) {

        DataFromUiToEngineInputRotors data = new DataFromUiToEngineInputRotors(resultRotors);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");
        try {
            DataFromEngineToUiRotors resultData = engine.settingRotorsToMachine(data);
            this.rotorsInput=new ArrayList<>();
            rotorsInput.add(data.getRotors());
            return true;
        } catch (RotorExceptionsFromUi exceptions) {
            alert.setContentText(exceptions.getAnswer());
            alert.showAndWait();
            return false;
        }

    }

    public boolean checkIfPositionsAreLegal(String positionsToCheck) {
        DataFromUiToEngineInputRotorsPositioning data = new DataFromUiToEngineInputRotorsPositioning(positionsToCheck.toUpperCase());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");
        try {
            DataFromEngineToUiRotorsPositioning resultData = engine.settingPositionsToMachine(data);
            this.positionsInput=new ArrayList<>();
            positionsInput.add(data.getPositions());
            return true;

        } catch (RotorPositionsExceptionFromUi e) {
            alert.setContentText(e.getAnswer());
            alert.showAndWait();
            return false;

        }
    }

    public boolean checkIfReflectorIsLegal(String reflectorToCheck) {

        DataFromUiToEngineReflector data = new DataFromUiToEngineReflector(reflectorToCheck);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");

        try {
            DataFromEngineToUiReflector resultData = engine.settingReflectors(data);
            this.reflectorInput=new ArrayList<>();
            reflectorInput.add(data.getReflector());
            return true;
        } catch (ReflectorExceptionsFromUi e) {
            alert.setContentText(e.getAnswer());
            alert.showAndWait();
            return false;


        }
    }
    public String convertReflectorsFromStringToInt(String input){

        switch (input) {

            case "I":
                return "1";
            case "II":
                return "2";
            case "III":
                return "3";
            case "IV":
                return "4";
            case "V":
                return "5";
        }
        return "";
    }



    public CurrentMachineDetails getMachineDetails() {
        return machineDetails;
    }

    public void setIsSet(boolean res) {
        this.isSet=true;
        this.machinePageTwoComponentController.makeStateComboBoxAccessible();
    }
    public boolean getIsSet(){
        return this.isSet;
    }

    public DataFromEngineToUiEncryptedString popForEncryptionContinuous(String text) {


        DataFromUiToEngineStringToEncrypt dataEncrypt = new DataFromUiToEngineStringToEncrypt(text.toUpperCase());
        DataFromEngineToUiEncryptedString resultedString=new DataFromEngineToUiEncryptedString("");

        try {
            resultedString = engine.inputProcessing(dataEncrypt);
            //System.out.println("The encrypted string: "+resultedString.getEncryptedString());
            //resultedString=new DataFromEngineToUiEncryptedString(resultedString.getEncryptedString());
            return resultedString;

        } catch (InputStringToEncryptException e) {
            resultedString.setEncryptedString(e.getAnswer());
            return resultedString;
        }
    }

    public DataFromEngineToUi showHistoryAndStats() {
        DataFromEngineToUi data=this.engine.historyAndStatistics();
        return data;
    }

    public DataFromEngineToUiEncryptedString popForEncryptionManual(String text,boolean isFirst,boolean done,String theString) {
        DataFromUiToEngineStringToEncrypt dataEncrypt = new DataFromUiToEngineStringToEncrypt(text.toUpperCase());
        DataFromEngineToUiEncryptedString resultedString=new DataFromEngineToUiEncryptedString("");

        try {
            resultedString=engine.inputProcessCharacter(dataEncrypt,isFirst,done,theString);
            //resultedString = engine.inputProcessing(dataEncrypt);
           // System.out.println("The encrypted string: "+resultedString.getEncryptedString());
            //resultedString=new DataFromEngineToUiEncryptedString(resultedString.getEncryptedString());
            return resultedString;

        } catch (InputStringToEncryptException e) {
            resultedString.setEncryptedString(e.getAnswer());
            return resultedString;
        }
    }

    public void resetTheMachinePop() {

        DataFromEngineToUi data=this.engine.resetSettings();
        showMachineDetails();

    }

    public void startBruteForceButtonPop(String encrypted,int numberOfAgentsFromChild,String levelOfTaskFromChild,int sizeOfTaskFromChild,int totalTasksFromChild) {

        decryptionHappened=true;
        this.helper=new Thread(()->{
            Consumer<DataFromAgentToDm>toSend=dataFromAgentToDm -> {
                //this.machinePageThreeComponentController.showInTextArea(DataFromAgentToDm);
                this.machinePageThreeComponentController.setDataFromDmInTextArea(dataFromAgentToDm);
                //System.out.println(dataFromAgentToDm.getConfiguration());
                //System.out.println(dataFromAgentToDm.getNumberOfThread());
            };
            Consumer<Double>toSendCounter=dataFromAgentToDmCounter->{
                this.machinePageThreeComponentController.setDataFromDmInTaskProgress(dataFromAgentToDmCounter);
            };
            Consumer<Long>toSendTimeOfAll=dataOfTime->{
                this.machinePageThreeComponentController.setDataFromDmInTotalTimeDm(dataOfTime);
            };

            dmManager=new DmManager(this.engine,numberOfAgentsFromChild,levelOfTaskFromChild,sizeOfTaskFromChild,totalTasksFromChild);
            dmManager.startBruteForce(encrypted,toSend,toSendCounter,toSendTimeOfAll,true);
        });
        this.helper.start();

    }


    public void pausePop() {
        this.dmManager.pauseBruteForce();
    }

    public void resumePop() {
        this.dmManager.resumeBruteForce();
    }

    public void stopPop() throws InterruptedException {
        this.dmManager.stopBruteForce();
        decryptionHappened=false;
    }
}