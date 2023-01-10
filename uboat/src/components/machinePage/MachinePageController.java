package components.machinePage;

import components.main.UBoatMainController;
import data.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import reflector.Reflector;
import rotor.Rotor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

public class MachinePageController implements Initializable {

    private UBoatMainController uboatMainController;
    @FXML
    private TextArea machineDetailsText;
    @FXML private TextArea rotorsShowingTextArea;
    @FXML private TextArea reflectorShowingArea;
    @FXML private TextArea plugBoardShowingArea;
    @FXML private ChoiceBox<Integer> rotorsChoiceBox;
    @FXML private ChoiceBox<String> reflectorChoiceBox;
    @FXML private ChoiceBox<String> plugsOneChoiceBox;
    @FXML private ChoiceBox<String> plugsTwoChoiceBox;
    @FXML private ChoiceBox<String>startingPositionChoiceBox;
    @FXML private HBox HBoxRotors;
    @FXML private HBox HBoxReflector;
    @FXML private HBox HBoxPlugBoard;
    @FXML private ListView<Character> listViewRotors;
    @FXML private ListView<String> listViewReflector;
    @FXML private ListView<String>listViewPlugBoard;

    @FXML public Button setCodeManuallyButton;
    @FXML private Button addRotorButton;
    @FXML private Button addReflectorButton;
    @FXML private Button addPlugButton;
    @FXML private Button clearRotorsButton;
    @FXML private Button clearReflectorButton;
    @FXML private Button clearPlugsButton;
    @FXML private Button removeRotorButton;
    @FXML private Button removePlugButton;
    @FXML private Button setRandomCode;

    public int countRotors;
    public int originalCountRotors;
    public int countReflectors;
    public int countPlugs;
    public int countPlugsReal;
    public int counter;
    public String rotorsToCheck;
    private ArrayList<Integer>allRotors;
    private ArrayList<FrontRotor>allRotorsReal;
    private ArrayList<FrontRotor>allRotorsLive;
    private ArrayList<String>allReflectors;
    private ArrayList<FrontReflector> allReflectorsReal;
    private ArrayList<String>plugBoardAlphabet;
    private ArrayList <String>rotorsCheckThatOK;
    private ArrayList<String>plugBoardToUse;
    private HashSet<String> plugOneChoiceBoxSet;
    private HashSet<String>plugTwoChoiceBoxSet;

    private SimpleStringProperty machineDetailsTextProperty;
    private SimpleBooleanProperty setCodeManuallyButtonProperty;
    private SimpleBooleanProperty addRotorButtonProperty;
    private SimpleBooleanProperty addReflectorButtonProperty;
    private SimpleBooleanProperty addPlugButtonProperty;
    private SimpleBooleanProperty clearRotorsButtonProperty;
    private SimpleBooleanProperty clearReflectorButtonProperty;
    private SimpleBooleanProperty clearPlugsButtonProperty;
    private SimpleBooleanProperty setRotorsInMachineButtonProperty;
    private SimpleBooleanProperty setPositionsInMachineButtonProperty;
    private SimpleBooleanProperty setReflectorInMachineButtonProperty;
    private SimpleBooleanProperty setPlugsInMachineButtonProperty;
    private SimpleBooleanProperty removeRotorButtonProperty;
    private SimpleBooleanProperty removePlugButtonProperty;


    public MachinePageController(){
        machineDetailsTextProperty=new SimpleStringProperty();
        setCodeManuallyButtonProperty=new SimpleBooleanProperty();
        addRotorButtonProperty=new SimpleBooleanProperty();
        addReflectorButtonProperty=new SimpleBooleanProperty();
        addPlugButtonProperty=new SimpleBooleanProperty();
        clearRotorsButtonProperty=new SimpleBooleanProperty();
        clearReflectorButtonProperty=new SimpleBooleanProperty();
        clearPlugsButtonProperty=new SimpleBooleanProperty();
        setRotorsInMachineButtonProperty=new SimpleBooleanProperty();
        setPositionsInMachineButtonProperty=new SimpleBooleanProperty();
        setReflectorInMachineButtonProperty=new SimpleBooleanProperty();
        setPlugsInMachineButtonProperty=new SimpleBooleanProperty();
        removeRotorButtonProperty=new SimpleBooleanProperty();
        removePlugButtonProperty=new SimpleBooleanProperty();
    }
    public void setMainController(UBoatMainController uBoatMainController) {
        this.uboatMainController=uBoatMainController;
        this.machineDetailsText.textProperty().bind(machineDetailsTextProperty);
        this.setCodeManuallyButton.disableProperty().bind(setCodeManuallyButtonProperty.not());
        this.addRotorButton.disableProperty().bind(addRotorButtonProperty.not());
        this.addReflectorButton.disableProperty().bind(addReflectorButtonProperty.not());
        this.addPlugButton.disableProperty().bind(addPlugButtonProperty.not());
        this.clearRotorsButton.disableProperty().bind(clearRotorsButtonProperty);
        this.clearReflectorButton.disableProperty().bind(clearReflectorButtonProperty);
        this.clearPlugsButton.disableProperty().bind(clearPlugsButtonProperty);
        this.removeRotorButton.disableProperty().bind(removeRotorButtonProperty.not());
        this.removePlugButton.disableProperty().bind(removePlugButtonProperty.not());
    }
    public void showMachineDetails(DataFromEngineToUiShowingTheMachine resultData) {

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("There are "+resultData.getHowManyRotorsTotal()+ " rotors total available.\n");
        stringBuilder.append("There are "+resultData.getHowManyUsedRotors()+" rotors used in the machine.\n");
        stringBuilder.append("There are "+resultData.getTotalReflectors()+" reflectors total available\n");
        stringBuilder.append("There were "+resultData.getHowManyEncrypt()+" encoded/decoded procedures.\n");

        if(resultData.getFirstStage().equals("")){
            stringBuilder.append("There were no setting of the machine.");
        }else {
            stringBuilder.append("The origin configuration of the machine is "+resultData.getFirstStage()+".\n");
            stringBuilder.append("The current configuration of the machine is"+resultData.getCurrentStage()+".");
        }
        this.machineDetailsTextProperty.set(stringBuilder.toString());

    }
    public void setDetailsInChild(CurrentDetailsMachine currentDetailsMachine) {

        rotorsChoiceBox.getItems().clear();
        reflectorChoiceBox.getItems().clear();
        plugsOneChoiceBox.getItems().clear();
        plugsTwoChoiceBox.getItems().clear();
        startingPositionChoiceBox.getItems().clear();
        rotorsShowingTextArea.clear();
        reflectorShowingArea.clear();
        plugBoardShowingArea.clear();
        addRotorButtonProperty.set(false);
        addReflectorButtonProperty.set(false);
        addPlugButtonProperty.set(false);
        setRotorsInMachineButtonProperty.set(false);
        setPositionsInMachineButtonProperty.set(false);
        setPlugsInMachineButtonProperty.set(false);
        //currentMachineConfigurationProperty.set("");
        removeRotorButtonProperty.set(false);
        HBoxRotors.getChildren().clear();
        HBoxReflector.getChildren().clear();
        HBoxPlugBoard.getChildren().clear();
        rotorsCheckThatOK=new ArrayList<>();
        plugBoardToUse=new ArrayList<>();
        counter=currentDetailsMachine.getUsedRotorsSize();
        setCodeManuallyButtonProperty.set(false);

        countRotors=currentDetailsMachine.getUsedRotorsSize();

        originalCountRotors=currentDetailsMachine.getUsedRotorsSize();
        countReflectors=1;
        countPlugs=currentDetailsMachine.getAlphabet().length()/2;
        countPlugsReal=currentDetailsMachine.getAlphabet().length()/2;

        //}
        countRotors=currentDetailsMachine.getUsedRotorsSize();
        this.allRotors=new ArrayList<>();
        this.allReflectors=new ArrayList<>();
        this.plugBoardAlphabet=new ArrayList<>();
        this.allRotorsReal=new ArrayList<>();
        this.allRotorsLive=new ArrayList<>(currentDetailsMachine.getAllRotors().size());
        this.allReflectorsReal=new ArrayList<>();
        this.plugOneChoiceBoxSet=new HashSet<>();
        this.plugTwoChoiceBoxSet=new HashSet<>();
        for(int i=0;i<currentDetailsMachine.getAllRotors().size();i++){
            allRotors.add(currentDetailsMachine.getAllRotors().get(i+1).getRotorId());
            allRotorsReal.add(currentDetailsMachine.getAllRotors().get(i+1));
        }
        rotorsChoiceBox.getItems().addAll(allRotors);
        for(int i=0;i<currentDetailsMachine.getAllReflectors().size();i++){
            allReflectors.add(convertReflectorsFromIntToString(currentDetailsMachine.getAllReflectors().get(i+1).getReflectorId()));
            allReflectorsReal.add(currentDetailsMachine.getAllReflectors().get(i+1));
        }
        reflectorChoiceBox.getItems().addAll(allReflectors);
        for(int i=0;i<currentDetailsMachine.getAlphabet().length();i++){
            plugBoardAlphabet.add(String.valueOf(currentDetailsMachine.getAlphabet().charAt(i)));
        }
        plugsOneChoiceBox.getItems().addAll(plugBoardAlphabet);
        plugsTwoChoiceBox.getItems().addAll(plugBoardAlphabet);

    }
    public void setRandomCodeListener(ActionEvent actionEvent) throws IOException {
        this.uboatMainController.setRandomCodeListenerInChildHappened();
        /*
        addRotorButtonProperty.set(false);
        addReflectorButtonProperty.set(false);
        addPlugButtonProperty.set(false);
        setCodeManuallyButtonProperty.set(false);
        clearRotorsButtonProperty.set(true);
        clearReflectorButtonProperty.set(true);
        clearPlugsButtonProperty.set(true);
        setRotorsInMachineButtonProperty.set(false);
        setPositionsInMachineButtonProperty.set(false);
        setReflectorInMachineButtonProperty.set(false);
        setPlugsInMachineButtonProperty.set(false);
        removeRotorButtonProperty.set(false);
        removePlugButtonProperty.set(false);
        setRandomCode.disableProperty().set(true);
        rotorsChoiceBox.disableProperty().set(true);
        startingPositionChoiceBox.disableProperty().set(true);
        reflectorChoiceBox.disableProperty().set(true);
        plugsOneChoiceBox.disableProperty().set(true);
        plugsTwoChoiceBox.disableProperty().set(true);

         */
    }
    public void setRandomCodeInTextField(String resultedData) throws IOException {

        //this.currentMachineConfigurationProperty.set(resultedData);
        this.uboatMainController.showMachineDetails();
        this.uboatMainController.setIsSet(true);
    }

    public String convertReflectorsFromIntToString(int input){

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
    private void setRotorsChoiceBox(ActionEvent actionEvent) {

        startingPositionChoiceBox.getItems().clear();
        ArrayList<Character>list=new ArrayList<>();
        ArrayList<String>anotherList=new ArrayList<>();


        if(rotorsChoiceBox.getValue()!=null) {
            list = this.allRotorsReal.get(rotorsChoiceBox.getValue().intValue() - 1).forwardEntry;

            for (int i = 0; i < list.size(); i++) {
                anotherList.add(list.get(i).toString());

            }
            startingPositionChoiceBox.getItems().addAll(anotherList);

        }
        if(rotorsChoiceBox.getValue()!=null&&startingPositionChoiceBox.getValue()!=null) {


//            System.out.println(startingPositionChoiceBox.getValue());
            addRotorButtonProperty.set(true);
            removeRotorButtonProperty.set(true);

        }
        else{
            addRotorButtonProperty.set(false);
        }

    }
    private void setPositionChoiceBox(ActionEvent actionEvent) {
        if(startingPositionChoiceBox.getValue()!=null &&rotorsChoiceBox.getValue()!=null){

            addRotorButtonProperty.set(true);
            removeRotorButtonProperty.set(true);
        }else{
            addRotorButtonProperty.set(false);
        }
    }
    private void setReflectorChoiceBox(ActionEvent actionEvent) {
        addReflectorButtonProperty.set(true);
    }
    private void setPlugTwoChoiceBox(ActionEvent actionEvent) {

        if(plugsOneChoiceBox.getValue()!=null && plugsTwoChoiceBox.getValue()!=null){
            addPlugButtonProperty.set(true);
        }

    }

    private void setPlugOneChoiceBox(ActionEvent actionEvent) {

        if(plugsOneChoiceBox.getValue()!=null && plugsTwoChoiceBox.getValue()!=null){
            addPlugButtonProperty.set(true);
        }
    }
    public void setCodeManuallyListener(ActionEvent actionEvent) throws IOException {

        if(countRotors!=0 ||countReflectors!=0){
            return;
        }

        StringBuilder strRotors=new StringBuilder();
        StringBuilder strPositions=new StringBuilder();
        StringBuilder strReflector=new StringBuilder();
        StringBuilder strPlugBoard=new StringBuilder();
        StringBuilder stringBuilder=new StringBuilder();
        this.rotorsToCheck=new String();

        for(int i=0;i<this.allRotorsLive.size();i++){
            strRotors.append(allRotorsLive.get(i).getRotorId());
            if(!(i==this.allRotorsLive.size()-1)) {
                strRotors.append(",");
            }
        }
        for(int i=0;i<this.allRotorsLive.size();i++){
            strPositions.append(allRotorsLive.get(i).forwardEntry.get(0));
        }

        strReflector.append(String.valueOf(convertReflectorsFromStringToInt(reflectorShowingArea.getText())));
        for(int i=0;i<plugBoardShowingArea.getText().length();i++){
            if(plugBoardShowingArea.getText().charAt(i)!=',' && plugBoardShowingArea.getText().charAt(i)!='|'){
                strPlugBoard.append(plugBoardShowingArea.getText().charAt(i));
            }
        }
        if(plugBoardShowingArea.getText()==null){
            strPlugBoard.append("");
        }

        ArrayList<String>inputs=new ArrayList<>();
        inputs.add(strRotors.toString());
        inputs.add(strPositions.toString());
        inputs.add(strReflector.toString());
        inputs.add(strPlugBoard.toString());
        String answer=this.uboatMainController.setCodeManuallyPop(inputs);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");
        if(!answer.equals("")){
            alert.setContentText(answer);
            alert.showAndWait();

        }else{
            this.uboatMainController.setIsSet(true);
        }
    }
    public void addRotorListener(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");

        if(countRotors==0){
            alert.setContentText("You cant add no more rotors.");
            alert.showAndWait();
            return;
        }

        if(rotorsCheckThatOK.contains(rotorsChoiceBox.getValue().toString())){
            alert.setContentText("You already added that rotor");
            alert.showAndWait();

            return;
        }
        countRotors--;
        rotorsCheckThatOK.add(rotorsChoiceBox.getValue().toString());

        if(!rotorsShowingTextArea.textProperty().isEmpty().getValue()){
            rotorsShowingTextArea.appendText(",");
        }
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(rotorsChoiceBox.getValue());
        //ListView<Character>rotorsList=new ListView<>();
        listViewRotors=new ListView<>();
        VBox rotorEdit=new VBox();
        rotorEdit.setId(rotorsChoiceBox.getValue().toString());
        Label rotorIdLabel=new Label();
        rotorIdLabel.setLabelFor(listViewRotors);
        rotorIdLabel.setText(rotorsChoiceBox.getValue().toString());
        rotorEdit.getChildren().add(rotorIdLabel);
        rotorEdit.getChildren().add(listViewRotors);
        ObservableList<Character> observableList= FXCollections.observableArrayList();
        observableList.addListener(new ListChangeListener() {
            @Override
//onChanged method
            public void onChanged(ListChangeListener.Change c) {

            }
        });


        int j=0;
        ArrayList<FrontRotor>tmp=new ArrayList<>();
        for(int k=0;k<allRotorsReal.size();k++){
            tmp.add(duplicate(allRotorsReal.get(k)));
        }

        while(tmp.get(rotorsChoiceBox.getValue().intValue()-1).forwardEntry.get(j)!=startingPositionChoiceBox.getValue().charAt(0)){
            tmp.get(rotorsChoiceBox.getValue().intValue()-1).turnAround();

        }
        allRotorsLive.add(tmp.get(rotorsChoiceBox.getValue().intValue()-1));

        for(int i=0;i<tmp.get(rotorsChoiceBox.getValue().intValue()-1).forwardEntry.size();i++){

            observableList.add(i,tmp.get(rotorsChoiceBox.getValue().intValue()-1).forwardEntry.get(i));
        }
        listViewRotors.setItems(observableList);
        HBoxRotors.getChildren().addAll(rotorEdit);
        //this.mainController.setListViewRotorsAndHBox(rotorEdit);
        rotorsShowingTextArea.appendText(stringBuilder.toString());
        setRotorsInMachineButtonProperty.set(true);
        if(countRotors!=0||countReflectors!=0){
            setCodeManuallyButtonProperty.set(false);
        }else{
            setCodeManuallyButtonProperty.set(true);
        }

    }
    public void addReflectorButtonListener(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");
        if(countReflectors==1){
            reflectorShowingArea.setText(reflectorChoiceBox.getValue());
            //addReflectorButtonProperty.set(false);
            setReflectorInMachineButtonProperty.set(true);
        }
        if(listViewReflector.getItems()!=null&& countReflectors==0){
            alert.setContentText("You can only have one reflector in the machine.");
            alert.showAndWait();
            return;
        }

        listViewReflector=new ListView<>();
        VBox reflectorEdit=new VBox();
        Label reflectorIdLabel=new Label();
        reflectorIdLabel.setLabelFor(listViewReflector);
        reflectorIdLabel.setText(reflectorChoiceBox.getValue().toString());
        reflectorEdit.getChildren().add(reflectorIdLabel);
        reflectorEdit.getChildren().add(listViewReflector);
        StringBuilder forOut=new StringBuilder();
        ObservableList<String>observableList=FXCollections.observableArrayList();
        observableList.addListener(new ListChangeListener() {
            @Override
//onChanged method
            public void onChanged(ListChangeListener.Change c) {

            }
        });
        int j=0;
        ArrayList<FrontReflector>tmp=new ArrayList<>();
        for(int k=0;k<allReflectorsReal.size();k++){
            tmp.add(duplicate(allReflectorsReal.get(k)));
        }
        FrontReflector r=tmp.get(convertReflectorsFromStringToInt(reflectorChoiceBox.getValue())-1);
        StringBuilder str=new StringBuilder();
        ArrayList<Integer>keys=new ArrayList<>();
        ArrayList<Integer>values=new ArrayList<>();

        keys.addAll(r.getReflectorMapping().keySet());
        values.addAll(r.getReflectorMapping().values());
        for(int i=0;i<plugBoardAlphabet.size();i++){

            forOut.append(("In :"+(keys.get(i)+1)+"   "+"Out :"+ (values.get(i)+1)));
            observableList.add(i,forOut.toString());
            forOut=new StringBuilder();
        }
        listViewReflector.setItems(observableList);
        HBoxReflector.getChildren().addAll(reflectorEdit);
        setRotorsInMachineButtonProperty.set(true);
        countReflectors--;
        if(countRotors!=0||countReflectors!=0){
            setCodeManuallyButtonProperty.set(false);
        }else{
            setCodeManuallyButtonProperty.set(true);
        }

    }
    public void addPlugButtonListener(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Reason:");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(plugsOneChoiceBox.getValue());
        stringBuilder.append("|");
        stringBuilder.append(plugsTwoChoiceBox.getValue());
        if(countPlugs==0){
            alert.setContentText("There cant be more plugs in the machine.");
            alert.showAndWait();
            return;
        }
        if(plugsOneChoiceBox.getValue().equals(plugsTwoChoiceBox.getValue())){
            alert.setContentText("A plug cant be between the same letters.");
            alert.showAndWait();
            return;
        }
        if(!plugOneChoiceBoxSet.isEmpty()&& !plugTwoChoiceBoxSet.isEmpty()) {
            if (plugOneChoiceBoxSet.contains(plugsOneChoiceBox.getValue()) || plugTwoChoiceBoxSet.contains(plugsTwoChoiceBox.getValue())||plugTwoChoiceBoxSet.contains(plugsOneChoiceBox.getValue())||plugOneChoiceBoxSet.contains(plugsTwoChoiceBox.getValue())) {

                if(plugsOneChoiceBox.getValue().equals(plugsTwoChoiceBox.getValue())){
                    alert.setContentText("A plug cant be between the same letters.");
                    alert.showAndWait();
                }else if(plugOneChoiceBoxSet.contains(plugsOneChoiceBox.getValue())&&plugTwoChoiceBoxSet.contains(plugsTwoChoiceBox.getValue())||plugOneChoiceBoxSet.contains(plugsTwoChoiceBox.getValue())&&plugTwoChoiceBoxSet.contains(plugsOneChoiceBox.getValue())){

                    alert.setContentText("The letters: "+plugsOneChoiceBox.getValue()+" , "+plugsTwoChoiceBox.getValue()+" already appear.");
                    alert.showAndWait();
                }else if(plugOneChoiceBoxSet.contains(plugsOneChoiceBox.getValue())&& !plugTwoChoiceBoxSet.contains(plugsOneChoiceBox.getValue())) {
                    alert.setContentText("The letter "+ plugsOneChoiceBox.getValue()+" already appears.");
                    alert.showAndWait();
                }else if(plugTwoChoiceBoxSet.contains(plugsOneChoiceBox.getValue())&&!plugOneChoiceBoxSet.contains(plugsOneChoiceBox.getValue())){
                    alert.setContentText("The letter " +plugsOneChoiceBox.getValue() +" already appears.");
                    alert.showAndWait();
                }else if(plugOneChoiceBoxSet.contains(plugsTwoChoiceBox.getValue())&& !plugTwoChoiceBoxSet.contains(plugsTwoChoiceBox.getValue())){
                    alert.setContentText("The letter " + plugsTwoChoiceBox.getValue()+" already appears.");
                    alert.showAndWait();
                } else if (plugTwoChoiceBoxSet.contains(plugsTwoChoiceBox.getValue())&&!plugOneChoiceBoxSet.contains(plugsTwoChoiceBox.getValue())) {
                    alert.setContentText("The letter "+plugsTwoChoiceBox.getValue() +" already appears.");
                    alert.showAndWait();
                }else{
                    alert.setContentText("The letter already appears.");
                    alert.showAndWait();
                }
                return;
            }
            plugOneChoiceBoxSet.add(plugsOneChoiceBox.getValue());
            plugTwoChoiceBoxSet.add(plugsTwoChoiceBox.getValue());
        }
        plugOneChoiceBoxSet.add(plugsOneChoiceBox.getValue());
        plugTwoChoiceBoxSet.add(plugsTwoChoiceBox.getValue());

        listViewPlugBoard=new ListView<>();
        VBox plugBoardEdit=new VBox();
        plugBoardEdit.getChildren().add(listViewPlugBoard);

        ObservableList<String>observableList=FXCollections.observableArrayList();
        StringBuilder forOut=new StringBuilder();
        forOut.append(plugsOneChoiceBox.getValue()+"  "+plugsTwoChoiceBox.getValue());
        plugBoardToUse.add(forOut.toString());

        for(int i=0;i<plugBoardToUse.size();i++){
            observableList.add(i,plugBoardToUse.get(i));
        }
        listViewPlugBoard.getItems().clear();
        HBoxPlugBoard.getChildren().clear();

        listViewPlugBoard.setItems(observableList);
        HBoxPlugBoard.getChildren().addAll(plugBoardEdit);
        countPlugs--;
        removePlugButtonProperty.set(true);

        if(countPlugs>0){
            stringBuilder.append(",");
        }
        //if(countPlugs>=0){
        plugBoardShowingArea.appendText(stringBuilder.toString());
        //addPlugButtonProperty.set(false);
        //}
    }
    public FrontRotor duplicate(FrontRotor rotor){
        return new FrontRotor(rotor.getRotorId(),rotor.getNotchPosition(),rotor.getAmountOfEntriesInRotor(),rotor.forwardEntry,rotor.backwardEntry);
    }
    public FrontReflector duplicate(FrontReflector reflector){

        HashMap<Integer,Integer> newHashMap=new HashMap<>();
        newHashMap.putAll(reflector.getReflectorMapping());
        return new FrontReflector(reflector.getReflectorId(),newHashMap);
    }






    public void clearRotorsButtonListener(ActionEvent actionEvent) {

        if(rotorsChoiceBox.getValue()==null){
            return;
        }
        allRotorsLive=new ArrayList<>(counter);
        countRotors=originalCountRotors;
        rotorsShowingTextArea.clear();
        rotorsCheckThatOK.clear();
        addRotorButtonProperty.set(false);
        startingPositionChoiceBox.getItems().clear();
        ArrayList<Character>list=new ArrayList<>();
        ArrayList<String>anotherList=new ArrayList<>();
        list=this.allRotorsReal.get(rotorsChoiceBox.getValue().intValue()-1).forwardEntry;

        for(int i=0;i<list.size();i++) {
            anotherList.add(list.get(i).toString());
        }
        startingPositionChoiceBox.getItems().addAll(anotherList);

        HBoxRotors.getChildren().clear();
        listViewRotors.getSelectionModel().clearSelection();
        setRotorsInMachineButtonProperty.set(false);
        if(countRotors!=0||countReflectors!=0){
            setCodeManuallyButtonProperty.set(false);
        }else{
            setCodeManuallyButtonProperty.set(true);
        }

    }

    public void clearReflectorButtonListener(ActionEvent actionEvent) {

        reflectorShowingArea.clear();
        listViewReflector.getItems().clear();
        listViewReflector.getSelectionModel().clearSelection();
        HBoxReflector.getChildren().clear();

        setReflectorInMachineButtonProperty.set(false);
        if(reflectorChoiceBox.getValue()!=null ){
            addReflectorButtonProperty.set(true);
        }
        countReflectors=1;
        if(countRotors!=0||countReflectors!=0){
            setCodeManuallyButtonProperty.set(false);
        }else{
            setCodeManuallyButtonProperty.set(true);
        }
    }

    public void clearPlugsButtonListener(ActionEvent actionEvent) {
        plugBoardShowingArea.clear();
        listViewPlugBoard.getItems().clear();
        HBoxPlugBoard.getChildren().clear();
        listViewPlugBoard.getSelectionModel().clearSelection();
        plugBoardToUse=new ArrayList<>();
        plugOneChoiceBoxSet.clear();
        plugTwoChoiceBoxSet.clear();
        countPlugs=countPlugsReal;
        if(plugsOneChoiceBox.getValue()!=null && plugsTwoChoiceBox.getValue()!=null){
            addPlugButtonProperty.set(true);
        }
    }

    public void removeRotorButtonListener(ActionEvent actionEvent) {


        ObservableList<Node> res=HBoxRotors.getChildren();
        boolean result=false;
        int save=0;
        for(int i=0;i<allRotorsLive.size();i++) {
            if(Integer.valueOf(res.get(i).getId())==(rotorsChoiceBox.getValue())){
                HBoxRotors.getChildren().remove(i);
                save=i;
                result=true;
                break;
            }
        }
        if(result==false){
            return;
        }
        for(int j=0;j<allRotorsLive.size();j++){
            if(allRotorsLive.get(j).getRotorId()==rotorsChoiceBox.getValue().intValue()){
                allRotorsLive.remove(j);
            }
        }
        StringBuilder stringBuilderNew=new StringBuilder();
        for(int i=0;i<allRotorsLive.size();i++){
            stringBuilderNew.append(res.get(i).getId());
            stringBuilderNew.append(',');
        }
        rotorsShowingTextArea.setText(stringBuilderNew.toString());
        countRotors++;
        rotorsCheckThatOK.remove(rotorsChoiceBox.getValue().toString());
        if(countRotors!=0||countReflectors!=0){
            setCodeManuallyButtonProperty.set(false);
        }else{
            setCodeManuallyButtonProperty.set(true);
        }
    }

    public void setTheManuallyConfiguration(DataFromEngineToUiShowingTheMachine resultData) {
       // if(currentMachineConfiguration.getText()!=null){
         //   currentMachineConfiguration.clear();
        //}
        //currentMachineConfigurationProperty.set(resultData.getCurrentStage());
    }

    public void removePlugButtonListener(ActionEvent actionEvent) {
        ObservableList<Node> res=HBoxPlugBoard.getChildren();
        ObservableList<String>resultPlugs=listViewPlugBoard.getItems();
        ArrayList<String> newPlugArray=new ArrayList<>();
        for(int i=0;i<listViewPlugBoard.getItems().size();i++){
            if(listViewPlugBoard.getItems().get(i).contains(plugsOneChoiceBox.getValue())&&listViewPlugBoard.getItems().get(i).contains(plugsTwoChoiceBox.getValue())){
                listViewPlugBoard.getItems().remove(i);
            }
        }

        plugBoardToUse.clear();
        if(plugOneChoiceBoxSet.contains(plugsOneChoiceBox.getValue())) {
            plugOneChoiceBoxSet.remove(plugsOneChoiceBox.getValue());
        }
        if(plugOneChoiceBoxSet.contains(plugsTwoChoiceBox.getValue())) {
            plugOneChoiceBoxSet.remove(plugsTwoChoiceBox.getValue());
        }
        if(plugTwoChoiceBoxSet.contains(plugsTwoChoiceBox.getValue())) {
            plugTwoChoiceBoxSet.remove(plugsTwoChoiceBox.getValue());
        }
        if(plugTwoChoiceBoxSet.contains(plugsOneChoiceBox.getValue())) {
            plugTwoChoiceBoxSet.remove(plugsOneChoiceBox.getValue());
        }

        ArrayList<String>plugBoardToUseNewForChars=new ArrayList<>();
        for(int l=0;l>plugBoardToUse.size();l++){
            plugBoardToUseNewForChars.add(plugBoardToUse.get(l));
        }
        for(int j=0;j<listViewPlugBoard.getItems().size();j++){
            newPlugArray.add(listViewPlugBoard.getItems().get(j));
            for(int i=0;i< newPlugArray.size();i++){
                char[]array=newPlugArray.get(j).toCharArray();
                StringBuilder tmp=new StringBuilder();
                tmp.append(array[0]);
                tmp.append(array[1]);
                tmp.append(array[2]);
                tmp.append(array[3]);
                plugBoardToUse.add(tmp.toString());
            }

            for(char ch:newPlugArray.get(j).toCharArray()){
                if(ch!=' '){
                    plugBoardToUseNewForChars.add(String.valueOf(ch));
                }
            }
        }
        plugBoardShowingArea.setText("");
        StringBuilder showingPlugStr=new StringBuilder();
        for(int w=0;w<plugBoardToUseNewForChars.size();w=w+2){
            showingPlugStr.append(plugBoardToUseNewForChars.get(w));
            showingPlugStr.append("|");
            showingPlugStr.append(plugBoardToUseNewForChars.get(w+1));
            showingPlugStr.append(",");
        }
        //for(int w=0;w<plugBoardToUse.size();w++){
        //System.out.println(plugBoardToUse.get(w));
        //char c1=plugBoardToUse.get(w).charAt(0);
        //char c2=plugBoardToUse.get(w).charAt(3);
        //showingPlugStr.append(c1);
        //showingPlugStr.append("|");
        //showingPlugStr.append(c2);
        //showingPlugStr.append(",");

        //}

        plugBoardShowingArea.setText(showingPlugStr.toString());
        countPlugs++;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //addRotorButton.disableProperty().set(true);
        //addReflectorButton.disableProperty().set(true);
        rotorsChoiceBox.setOnAction(this::setRotorsChoiceBox);
        startingPositionChoiceBox.setOnAction(this::setPositionChoiceBox);
        reflectorChoiceBox.setOnAction(this::setReflectorChoiceBox);
        plugsOneChoiceBox.setOnAction(this::setPlugOneChoiceBox);
        plugsTwoChoiceBox.setOnAction(this::setPlugTwoChoiceBox);


    }
    public int convertReflectorsFromStringToInt(String input){

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
    public String getRotorsFromTextArea(){
        return rotorsShowingTextArea.getText();
    }
    public String getReflectorFromTextArea(){
        return reflectorShowingArea.getText();
    }
    public String getStartingPosition(){
        StringBuilder strPositions=new StringBuilder();
        for(int i=0;i<this.allRotorsLive.size();i++){
            strPositions.append(allRotorsLive.get(i).forwardEntry.get(0));
        }

        return strPositions.toString();
    }


}
