

package logic;

import data.*;
import exceptionFromUserInput.*;
import exceptionsFromXml.*;
import generated.*;

import machine.EnigmaMachine;
import reflector.Reflector;
import rotor.Rotor;
import users.UserUBoatManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.lang.Exception;
import java.util.*;
import java.util.stream.Collectors;


public class EngineFirst implements Engine {


    private EnigmaMachine enigmaMachine;
    private HashSet<String> dictionary;
    private int numberOfAllies;
    private String level;
    private int numberOfAgents;
    private DataFromEngineToUiXml dataXml;
    private DataFromEngineToUiMachineSettings dataMachine;
    public static int encodedCounter;
    boolean newLoadingFile=false;
    boolean resetPressed=false;
    int allConfig;
    boolean changedConfig=false;
    private ArrayList<Character>usedOnesPositions;
    private ArrayList<String>rotorsFirstLetter;
    private ArrayList <Integer> usedRotorsFirstPositions;
    private MachineSettingStartManually startManuallyFirstOne;
    private MachineSettingStartManually startManuallyCurrent;
    private MachineSettingStartManually theFirstOneSinceFileEpoch;
    private ArrayList<MachineSettingStartManually> allOfTheHistory;
    private ArrayList<String>allCodeConfig;
    private CurrentMachineDetails details;
    private ArrayList<String> chars;
    private ArrayList<Long> times;


    //-----------------------------------first------------------------------------------------------------
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "generated";
    //function that creates the instance of the xml file to a java object
    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    public EngineFirst(){
        encodedCounter=0;
    }

    @Override
    public DataFromEngineToUiXml readTheMachineXmlFile(InputStream inputStream,String name) {

        //checkIfInputPathIsValid(path);

        try {

                //InputStream inputStream = new FileInputStream(path);
                CTEEnigma enigma = deserializeFrom(inputStream);
                checkAlphabetMachineData(enigma);
                checkRotorsCount(enigma);
                checkRotorsId(enigma);
                checkNotchInRotors(enigma);
                checkReflectorsId(enigma);
                checkMappingInReflectors(enigma);
                checkMappingInRotor(enigma);
                checkIfNumberOfAgentsIsLegal(enigma);
                createMyMachine(enigma);
                insertDataOfDictionary(enigma);
                checkIfBattleFieldIsLegal(enigma,name);
                //checkIfNumberOfAgentsIsLegal(enigma);
                //this.numberOfAgents=enigma.getCTEDecipher().getAgents();
                insertAllDataOfMachine();
                encodedCounter=0;
                allConfig=-1;
                newLoadingFile=true;
                this.allCodeConfig=new ArrayList<>();
                this.allOfTheHistory=new ArrayList<>();
                this.usedOnesPositions=new ArrayList<>();
                this.usedRotorsFirstPositions=new ArrayList<>();
                this.rotorsFirstLetter=new ArrayList<>();
                this.dataXml=new DataFromEngineToUiXml();
                return this.dataXml;

            } catch(JAXBException e){
                throw new JaxbException();
        }
        }
//-----------------------translation to my machine from the xml cte machine----------------------

    public void createMyMachine(CTEEnigma cteEnigma){

        String alphabet=cteEnigma.getCTEMachine().getABC();
        String resultAlphabetWithTrim=null;
        resultAlphabetWithTrim=alphabet.trim();
        this.enigmaMachine=new EnigmaMachine(cteEnigma.getCTEMachine().getRotorsCount(),resultAlphabetWithTrim);
        addReflectors(cteEnigma.getCTEMachine().getCTEReflectors());//adding all the reflectors
        addRotors(cteEnigma.getCTEMachine().getCTERotors());

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

//----------------------validations---------------------------------

    //1.1
    public void checkIfInputPathIsValid(String path){

        File file = new File(path);
        if(file.exists()){
            String str=file.toString();
            if(str.endsWith(".xml")){
                return;
            }
            else{
                throw new XmlException();
            }
        }
        throw new FileDoesNotExistException();
    }
    //1.2
    public void checkAlphabetMachineData(CTEEnigma enigma){

        HashSet<Character>alphabet=new HashSet<>();
        String abc=enigma.getCTEMachine().getABC().trim().toUpperCase();

        for(int i=0;i<abc.length();i++){
            alphabet.add(abc.charAt(i));
        }
        if(enigma.getCTEMachine().getABC().trim().length()>alphabet.size()){
            throw new AlphabetSizeException("There was an error. There were letters that apeard more than once.");
        }

        if(enigma.getCTEMachine().getABC().trim().length()%2!=0){

            throw new AlphabetSizeException("Alphabet size is not an even number.");
        }

    }
    //1.3 , 1.4
    public void checkRotorsCount(CTEEnigma enigma){

        if (enigma.getCTEMachine().getRotorsCount()<2){
            throw new RotorsCountException("There has to be minimum 2 rotors at use in the machine. In the xml there are less.");
        }
        if(enigma.getCTEMachine().getRotorsCount()>99){
            throw new RotorsCountException("There could be maximum 99 rotors at use in the machine. In the xml there are more.");
        }
        if(enigma.getCTEMachine().getCTERotors().getCTERotor().size()<enigma.getCTEMachine().getRotorsCount()){

            throw new RotorsCountException("There cant be more rotors in use then totally rotors that are relevent to the machine.");
        }

    }

    //1.5
    public void checkRotorsId(CTEEnigma enigma){

        List<CTERotor>rotors=enigma.getCTEMachine().getCTERotors().getCTERotor();
        ArrayList<Integer>temp=new ArrayList<>();
        for(CTERotor rotor: rotors){
            temp.add(rotor.getId());
        }
        List<Integer> sortedList = temp.stream().sorted().collect(Collectors.toList());

        for(int i=0;i<sortedList.size();i++){
            if(sortedList.get(i)!=i+1){
                throw new IdRotorException();
            }
        }
    }

    //1.6
    public void checkMappingInRotor(CTEEnigma enigma){

       int countRight;
       int countLeft;

        List<CTERotor>rotors=enigma.getCTEMachine().getCTERotors().getCTERotor();
        for(CTERotor rotor:rotors){
            ArrayList<String>right=new ArrayList<>();
            ArrayList<String>left=new ArrayList<>();
            List<CTEPositioning>positioning=rotor.getCTEPositioning();
            for(CTEPositioning position:positioning){
               right.add(position.getRight());
               left.add(position.getLeft());

            }
            for(int i=0;i<right.size();i++){
                 countRight=checkHowManyTimesExist(right,right.get(i));
                 countLeft=checkHowManyTimesExist(left,left.get(i));
                if(countRight==1 ||countLeft==1) {
                    throw new MappingRotorException();
                }

            }

        }

    }

    public int checkHowManyTimesExist(ArrayList<String>list,String letter){

        int count=0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).equals(letter)){
                count++;
            }
            if(count>1){
                return 1;
            }
        }

        return 0;

    }
    //1.7
    public void checkNotchInRotors(CTEEnigma enigma){

        List<CTERotor>rotors=enigma.getCTEMachine().getCTERotors().getCTERotor();
        for(CTERotor rotor: rotors){
            if(rotor.getNotch()>enigma.getCTEMachine().getABC().trim().length()|| rotor.getNotch()<1){
                throw new NotchException();
            }
        }
    }

    //1.8
    public void checkReflectorsId(CTEEnigma enigma){

        List<CTEReflector>reflectors=enigma.getCTEMachine().getCTEReflectors().getCTEReflector();
        ArrayList<Integer>temp=new ArrayList<>();
        HashSet <String>resultsOfID=new HashSet<>();
        for(CTEReflector reflector: reflectors){
            if(changeFromRomeToInt(reflector.getId())!=0) {
                temp.add(changeFromRomeToInt(reflector.getId()));
            }
            else{
                resultsOfID.add(reflector.getId());
            }
        }
        if(!resultsOfID.isEmpty()) {
            throw new IdReflectorException("The reflectors Id" + resultsOfID + " are not romanian and are not part of the machine.");
        }
        List<Integer> sortedList = temp.stream().sorted().collect(Collectors.toList());
        HashSet<String>myHashSet=new HashSet<>();


        for(int i=0;i<sortedList.size();i++){
            if(sortedList.get(i)!=i+1){
                myHashSet.add(changeFromIntToRome(sortedList.get(i)));
            }
        }
        if(!myHashSet.isEmpty()) {
            throw new IdReflectorException("The reflectors"+myHashSet+" appear more than one time. Id of reflector needs to be unique.");
        }
        if(sortedList.size()>5){
            throw new IdReflectorException("There can be at most 5 reflectors in the machine.");
        }
    }


    //1.9
    public void checkMappingInReflectors(CTEEnigma enigma){
        List<CTEReflector> reflectors=enigma.getCTEMachine().getCTEReflectors().getCTEReflector();

        for(CTEReflector reflector: reflectors){

            for(int j=0;j<reflector.getCTEReflect().size();j++) {
                if (reflector.getCTEReflect().get(j).getInput() == reflector.getCTEReflect().get(j).getOutput()) {
                    throw new MappingReflectorException();
                }
            }

            }
    }
    public void checkIfNumberOfAgentsIsLegal(CTEEnigma enigma){

        //if(enigma.getCTEDecipher().getAgents()<2||enigma.getCTEDecipher().getAgents()>50){
          //  throw new ExceptionAll("Error. The amount of agent's should be between 2 to 50");
        //}

    }
    public void insertDataOfDictionary(CTEEnigma enigma){
        this.dictionary=new HashSet<>();
        String words=enigma.getCTEDecipher().getCTEDictionary().getWords().trim();
        String excluded=enigma.getCTEDecipher().getCTEDictionary().getExcludeChars();
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
    }
    public void checkIfBattleFieldIsLegal(CTEEnigma enigma,String name){

        String battle=enigma.getCTEBattlefield().getBattleName();
        if(UserUBoatManager.addBattle(battle,name)==false){
            throw new ExceptionAll("There is already a battle with that name");
        }
        numberOfAllies=enigma.getCTEBattlefield().getAllies();
        level=enigma.getCTEBattlefield().getLevel();
    }

//----------------------------------------------------------second----------------------------------------------------------------
    @Override
    public DataFromEngineToUiShowingTheMachine displayingTheMachineSetting() {

        int allOfTheRotors=this.enigmaMachine.allRotors.size();
        int usedRotors=this.enigmaMachine.getUsedRotorsCount();
        int reflectors=this.enigmaMachine.allReflectors.size();
        if(!this.enigmaMachine.usedRotors.isEmpty())
        {
            if(resetPressed==true){

                this.startManuallyCurrent=this.theFirstOneSinceFileEpoch;
                this.startManuallyFirstOne=this.theFirstOneSinceFileEpoch;
                resetPressed=false;
            }
            else {
                int[] usedRotorsArray = new int[usedRotors];
                int[] positionsOfRotorArrayInteger = new int[usedRotors];
                char[]positionsOfRotorArrayCharacter=new char[usedRotors];

                 int m=this.enigmaMachine.getUsedRotorsCount()-1;
                for (int i = 0; i < usedRotors; i++) {
                    usedRotorsArray[i] = this.enigmaMachine.usedRotors.get(i).getRotorId();
                    positionsOfRotorArrayInteger[i] = this.enigmaMachine.usedRotors.get(m).getRotorPosition();
                    positionsOfRotorArrayCharacter[i]=this.enigmaMachine.usedRotors.get(i).forwardEntry.get(0);
                    m--;

                }
                int reflectorToSend = this.enigmaMachine.usedReflector.getReflectorId();

                Set<Map.Entry<Integer, Integer>> s = this.enigmaMachine.plugBoard.mappingPairs.entrySet();
                List<Map.Entry<Integer, Integer>> array = new ArrayList<>(s);
                Set<Integer> numbers = new HashSet<>();
                int[] plugBoardToSend = new int[array.size()];
                int j = 0;
                for (int i = 0; i < array.size(); i++) {

                    if (!numbers.contains(array.get(i).getValue()) && !numbers.contains(array.get(i).getKey())) {
                        plugBoardToSend[j] = array.get(i).getKey();
                        plugBoardToSend[j + 1] = array.get(i).getValue();
                        j = j + 2;
                    }
                    numbers.add(array.get(i).getValue());
                    numbers.add(array.get(i).getKey());

                }

                ArrayList<String> resultsFromChange = convertNew(usedRotorsArray, positionsOfRotorArrayCharacter, reflectorToSend, plugBoardToSend);
                //ArrayList<String> resultsFromChange = convertToStrings(usedRotorsArray, positionsOfRotorArray, reflectorToSend, plugBoardToSend);
                int[] notch = new int[this.enigmaMachine.getUsedRotorsCount()];
                int w=this.enigmaMachine.getUsedRotorsCount()-1;

                for (int i = 0; i < notch.length; i++) {
                    notch[i] = this.enigmaMachine.usedRotors.get(w).getNotchPosition();
                    w--;
                }

                this.startManuallyCurrent = new MachineSettingStartManually(resultsFromChange,positionsOfRotorArrayInteger, this.enigmaMachine.getAlphabet(), notch);
                this.startManuallyCurrent.makeStringToUser();
            }

            DataFromEngineToUiShowingTheMachine dataWithStages=new DataFromEngineToUiShowingTheMachine(allOfTheRotors,usedRotors,reflectors,encodedCounter,this.startManuallyFirstOne.resultString,this.startManuallyCurrent.resultString);
            return dataWithStages;
        }else {
            DataFromEngineToUiShowingTheMachine data = new DataFromEngineToUiShowingTheMachine(allOfTheRotors, usedRotors, reflectors, encodedCounter, "", "");
            return data;
        }
    }



//-----------------------------------------------------third--------------------------------------------------------------------


    //------------------------------------------rotors settings-------------------------------------
    @Override
    public DataFromEngineToUiRotors settingRotorsToMachine(DataFromUiToEngineInputRotors rotors){

        checkIfRotorsInputIsLegal(rotors.getRotors());
        checkIfRotorsCanFitInMachineLegally(rotors.getRotors());
        DataFromEngineToUiRotors data=new DataFromEngineToUiRotors("Great! the rotors have been placed in the machine.");
        return data;
    }

    public void checkIfRotorsInputIsLegal(String rotors) {

        char[] strOfChars = rotors.toCharArray();
        for (int i = 0; i < strOfChars.length; i++) {
            if (strOfChars[i] != ',' && (strOfChars[i] < '0' || strOfChars[i] > '9')) {
                throw new RotorExceptionsFromUi("The input should include only numeric numbers and commas.");
            }
        }
        String regexComma = ",";
        String[] str = rotors.split(regexComma);

        for (String string : str) {

            if (string.equals("")) {
                throw new RotorExceptionsFromUi("The input is incorrect. You need to insert 2 numeric numbers with a comma between them." +
                        "You included to much commas.");
            }
        }
    }
    public void checkIfRotorsCanFitInMachineLegally(String rotors) {
        String regexComma = ",";
        String[] str = rotors.split(regexComma);
        ArrayList<Integer> rotorsList = new ArrayList<>();

        for (String string : str) {
            rotorsList.add(Integer.parseInt(string));

        }
        int res;
        HashSet<Integer> idNumbersToCheckForDuplicates = new HashSet<>();
        HashSet<Integer> idNumbersToProceed = new HashSet<>();

        for (int i = 0; i < rotorsList.size(); i++) {
            res = Collections.frequency(rotorsList, rotorsList.get(i));
            if (res > 1) {
                idNumbersToCheckForDuplicates.add(rotorsList.get(i));
            }
            idNumbersToProceed.add(rotorsList.get(i));
        }
        if (!idNumbersToCheckForDuplicates.isEmpty()) {
            throw new RotorExceptionsFromUi("The input is not legal. You need to enter each id of rotor once." +
                    "You entered these rotors id's more than one time: " + idNumbersToCheckForDuplicates.toString());
        }
        if (this.enigmaMachine.getUsedRotorsCount() < idNumbersToProceed.size()) {
            throw new RotorExceptionsFromUi("You entered " + idNumbersToProceed.size() + " rotors." +
                    "In the machine you can only use " + this.enigmaMachine.getUsedRotorsCount() + ". " +
                    "Please enter less.");
        }
        if (this.enigmaMachine.getUsedRotorsCount() > idNumbersToProceed.size()) {
            throw new RotorExceptionsFromUi("You entered " + idNumbersToProceed.size() + " rotors." +
                    "In the machine you can only use " + this.enigmaMachine.getUsedRotorsCount() + ". " +
                    "Please enter more.");
        }
        ArrayList<Integer>allOfTheRotorsInTheMachine=new ArrayList<>();
        HashSet<Integer> idNumbersWhoDontExistInMachine = new HashSet<>();
        for(Integer number: idNumbersToProceed){
            allOfTheRotorsInTheMachine.add(number);
        }

        for(int j=0;j<allOfTheRotorsInTheMachine.size();j++){
            if(!this.enigmaMachine.allRotors.containsKey(allOfTheRotorsInTheMachine.get(j))){
                idNumbersWhoDontExistInMachine.add(allOfTheRotorsInTheMachine.get(j));
            }
        }

        if(!(idNumbersWhoDontExistInMachine.isEmpty())){
            throw new RotorExceptionsFromUi("The rotors "+idNumbersWhoDontExistInMachine.toString()+" do not exist in the machine.");
        }
    }

    public void setTheRotorsInMachine(String rotors){

        String regexComma = ",";
        String[] str = rotors.split(regexComma);
        ArrayList<Integer> rotorsListToInsert = new ArrayList<>();

        for (String string : str) {
            rotorsListToInsert.add(Integer.parseInt(string));
        }
        int[]usedRotors=new int[rotorsListToInsert.size()];
        int j=usedRotors.length-1;
        for(int i=0;i<usedRotors.length;i++){
            usedRotors[i]=rotorsListToInsert.get(j);
            j--;
        }
        this.enigmaMachine.chooseRotorsToBeUsed(usedRotors);
    }
//--------------------------------------position settings-----------------------------------------------
    @Override
    public DataFromEngineToUiRotorsPositioning settingPositionsToMachine(DataFromUiToEngineInputRotorsPositioning positioning){
        checkIfPositionInputIsLegal(positioning.getPositions());
        DataFromEngineToUiRotorsPositioning data=new DataFromEngineToUiRotorsPositioning("Great! the rotors starting positions have been placed in the machine.");
        return data;

    }
    public void checkIfPositionInputIsLegal(String positions){
        String alphabet=this.enigmaMachine.getAlphabet();
        String[] str=positions.split("");
        if(str.length>this.enigmaMachine.getUsedRotorsCount()){
            throw new RotorPositionsExceptionFromUi("Wrong input, you entered "+str.length+ " positions for each rotor.\n There are  "+this.enigmaMachine.getUsedRotorsCount()+" rotors used  in the machine. Please enter less positions.");
        }
        if(str.length <this.enigmaMachine.getUsedRotorsCount()){
            throw new RotorPositionsExceptionFromUi("Wrong input, you entered "+str.length+ " positions for each rotor.\n There are  "+this.enigmaMachine.getUsedRotorsCount()+" rotors used  in the machine. Please enter more positions.");
        }

        HashSet<String>stringHashSet=new HashSet<>();

        for(String string:str){
            if(!alphabet.contains(string)){
                stringHashSet.add(string);
            }

        }
        if(!stringHashSet.isEmpty()){
            throw new RotorPositionsExceptionFromUi("The letters:" +stringHashSet+ " are not in the alphabet of the machine.");
        }

    }
    public void setTheRotorsPositionsInMachine(String positions){

        HashMap<Character,Integer>thePositions=new HashMap<>();
        String regexComma = "";
        String[] str = positions.split(regexComma);
        String alphabetOfMachine=this.enigmaMachine.getAlphabet();
        int k=str.length-1;
        for(Rotor rotor:this.enigmaMachine.usedRotors){
            //int res=this.enigmaMachine.usedRotors.get(k).forwardEntry.get(thePositions.get(str[k].charAt(0)));
           int res=rotor.forwardEntry.indexOf(str[k].charAt(0));
           rotor.setRotorToStartingPosition(res);

            //rotor.setRotorToStartingPosition(thePositions.get(str[k].charAt(0)));
            k--;
        }
    }
    //------------------------------------------reflector settings-------------------------------------------------
    @Override
    public DataFromEngineToUiReflector settingReflectors(DataFromUiToEngineReflector reflector){
        checkIfReflectorIsLegal(reflector.getReflector());
        DataFromEngineToUiReflector data=new DataFromEngineToUiReflector("Great! the reflector has been placed in the machine.");
        return data;

    }
    public void checkIfReflectorIsLegal(String reflector){
        HashMap reflectorsInMachine=this.enigmaMachine.allReflectors;
        try {
            int rotorInt = Integer.parseInt(reflector);

            if (rotorInt > 5) {
                throw new ReflectorExceptionsFromUi("You entered the option " + rotorInt + ". You need to enter an option between 1-5.");
            }

            if (!reflectorsInMachine.containsKey(rotorInt)) {
                throw new ReflectorExceptionsFromUi("The machime doesn't have the Id " + reflector + ".");
            }
        }
        catch (NumberFormatException e){
            throw new ReflectorExceptionsFromUi("Error. You need to enter a number between 1-5.");
        }
    }

    public void setTheReflectorInMachine(String reflector){
        int rotorInt=Integer.parseInt(reflector);
        this.enigmaMachine.chooseReflectorToBeUsed(rotorInt);
    }
    //------------------------------------------plug board settings------------------------------------------------------
    @Override
    public DataFromEngineToUiPlugBoard settingPlugBoard(DataFromUiToEngineInputPlugBoard plugBoard){

        String plugBoardStr=plugBoard.getPlugBoard();

        //Checking if the input of the user is in the right length
        if(plugBoardStr.length()%2!=0) {
            throw new PlugBoardExceptionsFromUi("The input should be an even number of letters so that the machine could build pairs.\nYou entered an odd number of letters.");
        }
        //Checking if the input of the user is between plug in from letters in the alphabet of the machine
        HashSet<String>stringHashSet=new HashSet<>();
        String alphabet=this.enigmaMachine.getAlphabet();
        String[] str=plugBoardStr.split("");

        for(String string:str){
            if(!alphabet.contains(string)){
                stringHashSet.add(string);
            }
        }
        if(!(stringHashSet.isEmpty())){
            throw new PlugBoardExceptionsFromUi("The letters "+stringHashSet+" do not appear in the alphabet of the machine.");
        }

        //check if the mapping is not the same letter
        char[]tmp=new char[2];
        HashSet<Character>lettersWhoAreMappedToMyself=new HashSet<>();
        for(int i=0;i<plugBoardStr.length();i=i+2){
            tmp[0]=plugBoardStr.charAt(i);
            tmp[1]=plugBoardStr.charAt(i+1);
            if(tmp[0]==tmp[1]){
                lettersWhoAreMappedToMyself.add(tmp[0]);
            }
        }
        if(!(lettersWhoAreMappedToMyself).isEmpty()){
            throw new PlugBoardExceptionsFromUi("The letters"+lettersWhoAreMappedToMyself+" are mapped to thereselves. That can not be in the machine.");
        }
        //check if there is no letter that appears in more than one plugin
        int res=0;
        HashSet<Character> numbersToCheckForDuplicates = new HashSet<>();
        ArrayList<Character>listOfLetters=new ArrayList<>();
        for (int i=0;i<plugBoardStr.length();i++){
            listOfLetters.add(plugBoardStr.charAt(i));
        }
        for (int j=0;j<listOfLetters.size();j++){
            res=Collections.frequency(listOfLetters,listOfLetters.get(j));
            if(res>1){
                numbersToCheckForDuplicates.add(listOfLetters.get(j));
            }
        }
        if(!numbersToCheckForDuplicates.isEmpty()){
            throw new PlugBoardExceptionsFromUi("The letters "+numbersToCheckForDuplicates+" appear in more than one plugin. That can not bo in the machine.");

        }
        if(plugBoardStr.length()==0){
            DataFromEngineToUiPlugBoard anotherData=new DataFromEngineToUiPlugBoard("");
            return anotherData;
        }else {
            DataFromEngineToUiPlugBoard data = new DataFromEngineToUiPlugBoard("Great! the plug board has been placed in the machine.");
            return data;
        }
    }

    public void setThePlugBoardInTheMachine(String plugBoard){

        int res1;
        int res2;
        for(int i=0;i<plugBoard.length();i=i+2){
            res1=findWhereInTheAlphabet(plugBoard.charAt(i));
            res2=findWhereInTheAlphabet(plugBoard.charAt(i+1));
            this.enigmaMachine.plugBoard.addAnotherPlugin(res1,res2);
        }
    }
    public int findWhereInTheAlphabet(char letter){

        String alphabet=this.enigmaMachine.getAlphabet();
        for(int i=0;i<alphabet.length();i++){
            if(letter==alphabet.charAt(i)){
                return i;
            }
        }
        return 0;
    }

    @Override
    public void settingAllManually(ArrayList<String>results) {

        //Checking if the machine has already been input to something.
        if(!this.enigmaMachine.usedRotors.isEmpty()){
            for(int i=0;i<this.enigmaMachine.usedRotors.size();i++){
                //this.enigmaMachine.usedRotors.get(i).setRotorToStartingPosition(0);
                this.enigmaMachine.usedRotors.removeAll(this.enigmaMachine.usedRotors);
            }
        }
        if(!(this.enigmaMachine.plugBoard.mappingPairs.isEmpty())){

            this.enigmaMachine.plugBoard.mappingPairs.clear();
        }

        setTheRotorsInMachine(results.get(0));
        setTheRotorsPositionsInMachine(results.get(1));
        setTheReflectorInMachine(results.get(2));
        setThePlugBoardInTheMachine(results.get(3));
        int[]notch=new int[this.enigmaMachine.getUsedRotorsCount()];
        int j=this.enigmaMachine.getUsedRotorsCount()-1;
        for(int i=0;i<notch.length;i++){
            notch[i]=this.enigmaMachine.usedRotors.get(j).getNotchPosition();
            j--;
        }
        int[] integerPosition=new int[this.enigmaMachine.getUsedRotorsCount()];

        j=this.enigmaMachine.getUsedRotorsCount()-1;
        for(int i=0;i<this.enigmaMachine.getUsedRotorsCount();i++){
            integerPosition[i]=this.enigmaMachine.usedRotors.get(j).getRotorPosition();
            j--;

        }
        this.startManuallyFirstOne=new MachineSettingStartManually(results,integerPosition,this.enigmaMachine.getAlphabet(),notch);
        this.startManuallyFirstOne.makeStringToUser();
        allConfig++;
        this.allOfTheHistory.add(this.startManuallyFirstOne);

        //if(newLoadingFile==true){
            this.theFirstOneSinceFileEpoch=this.startManuallyFirstOne;
            this.usedRotorsFirstPositions=new ArrayList<>();
            //this.usedOnesPositions=new ArrayList<>();
           //for(int i=0;i<this.enigmaMachine.getUsedRotorsCount();i++){
               //usedOnesPositions.remove(i);
           //}
        if(!this.usedOnesPositions.isEmpty()){
            for(Rotor r:this.enigmaMachine.usedRotors){

            }
           usedOnesPositions.removeAll(usedOnesPositions);
        }


            for(Rotor rotor:this.enigmaMachine.usedRotors){
                usedOnesPositions.add(rotor.forwardEntry.get(0).charValue());
                usedRotorsFirstPositions.add(rotor.getRotorPosition());
            }
            this.allCodeConfig.add(this.startManuallyFirstOne.resultString);
            newLoadingFile=false;

            changedConfig=true;
        //}
    }
    @Override
    public DataFromEngineToUi selectingAnInitialCodeConfigurationAutomatically() {

        //Checking if the machine has already been input to something.
        if (!this.enigmaMachine.usedRotors.isEmpty()) {
            for (int i = 0; i < this.enigmaMachine.usedRotors.size(); i++) {
                this.enigmaMachine.usedRotors.removeAll(this.enigmaMachine.usedRotors);
            }
        }
        if (!(this.enigmaMachine.plugBoard.mappingPairs.isEmpty())) {

            this.enigmaMachine.plugBoard.mappingPairs.clear();
        }

        int[] usedRotors = new int[this.enigmaMachine.getUsedRotorsCount()];
        int res;
        HashSet<Integer>idRotors=new HashSet<>();
        Random random=new Random();
        for(int i=0;i<this.enigmaMachine.getUsedRotorsCount();i++){

            res=random.nextInt(this.enigmaMachine.allRotors.size()+1);
            while(idRotors.contains(res)||res==0)
            {
                res= random.nextInt(this.enigmaMachine.allRotors.size()+1);
            }
            while(res==0){
                res= random.nextInt(this.enigmaMachine.allRotors.size()+1);
            }
            idRotors.add(res);
            usedRotors[i]=res;
        }
        //int[] positions = new int[this.enigmaMachine.getUsedRotorsCount()];
        char[]positions=new char[this.enigmaMachine.getUsedRotorsCount()];
        int[] plugBoard=new int[this.enigmaMachine.getAlphabetSize()];
        int alphabetOfMachineSize = this.enigmaMachine.getAlphabetSize();
        randomPicks(positions, alphabetOfMachineSize);

        //Random pick of the reflector for the machine.
        Random randomReflector = new Random();
        int reflector = randomReflector.nextInt(this.enigmaMachine.allReflectors.size()+1);
        while(reflector==0){
            reflector = randomReflector.nextInt(this.enigmaMachine.allReflectors.size()+1);
        }

        //Random pick of how many plugs for the machine
        Random randomPlugs=new Random();
        int amountOfPlugsInMachine=randomPlugs.nextInt((this.enigmaMachine.getAlphabetSize()/2)+1);
        HashSet<Integer>numbers=new HashSet<>();
        if(amountOfPlugsInMachine!=0){
            plugBoard[0]=plugBoard[1]=-1;
        }

        //Random pick of plugs

        for(int i=0;i<amountOfPlugsInMachine*2;i=i+2){
            while(plugBoard[i]==plugBoard[i+1]) {
                plugBoard[i] = randomPlugs.nextInt(this.enigmaMachine.getAlphabetSize());
                plugBoard[i+1] = randomPlugs.nextInt(this.enigmaMachine.getAlphabetSize());
            }
            while(numbers.contains(plugBoard[i])){
                plugBoard[i]=randomPlugs.nextInt(this.enigmaMachine.getAlphabetSize());
            }
            numbers.add(plugBoard[i]);
            while (numbers.contains(plugBoard[i+1])){
                plugBoard[i+1]=randomPlugs.nextInt(this.enigmaMachine.getAlphabetSize());
            }
            numbers.add(plugBoard[i+1]);
        }
        int[]actualPlugBoard=new int[amountOfPlugsInMachine*2];
        for(int j=0;j<actualPlugBoard.length;j++){
            actualPlugBoard[j]=plugBoard[j];
        }
        /////////////////////////////////////////////////////////////////////////////////here!
        actualPlugBoard=new int[0];


        //initTheMachine(usedRotors,positions,reflector,actualPlugBoard);
        //ArrayList<String> resultsToUse=convertNew(usedRotors,positions,reflector,actualPlugBoard);
        initTheMachine(usedRotors,positions,reflector,actualPlugBoard);
        ArrayList<String> resultsToUse=convertNew(usedRotors,positions,reflector,actualPlugBoard);
        int[]notch=new int[this.enigmaMachine.getUsedRotorsCount()];
        int j=this.enigmaMachine.getUsedRotorsCount()-1;
        for(int i=0;i<notch.length;i++){
            notch[i]=this.enigmaMachine.usedRotors.get(j).getNotchPosition();
            j--;
        }
        int[] integerPosition=new int[this.enigmaMachine.getUsedRotorsCount()];
        int m=this.enigmaMachine.getUsedRotorsCount()-1;
        for(int i=0;i<this.enigmaMachine.getUsedRotorsCount();i++){
            integerPosition[i]=this.enigmaMachine.usedRotors.get(m).getRotorPosition();
            m--;
        }

        this.startManuallyFirstOne=new MachineSettingStartManually(resultsToUse,integerPosition,this.enigmaMachine.getAlphabet(),notch);
        this.startManuallyFirstOne.makeStringToUser();
        DataFromEngineToUi data=new DataFromEngineToUi(this.startManuallyFirstOne.resultString);

        //if(newLoadingFile==true){
            this.theFirstOneSinceFileEpoch=this.startManuallyFirstOne;
            this.usedRotorsFirstPositions=new ArrayList<>();
            for(Rotor rotor:this.enigmaMachine.usedRotors){
                usedRotorsFirstPositions.add(rotor.getRotorPosition());
                usedOnesPositions.add(rotor.forwardEntry.get(0).charValue());
            }
           // newLoadingFile=false;
        //}

        this.allCodeConfig.add(this.startManuallyFirstOne.resultString);
        allConfig++;
        this.allOfTheHistory.add(this.startManuallyFirstOne);
        changedConfig=true;
        return data;


        //return new data.DataFromEngineToUi("hello");
    }
    public ArrayList<String> convertNew(int[]rotors,char[]positionsChar,int reflector,int[]plugBoard){

        StringBuilder rotorString=new StringBuilder();
        for(int i=rotors.length-1;i>=0;i--){
            rotorString.append(rotors[i]);
            if(i!=0){
                rotorString.append(",");
            }
        }
        StringBuilder positionCharString=new StringBuilder();

        for(int i=rotors.length-1;i>=0;i--){

            positionCharString.append(positionsChar[i]);

            //positionString.append(this.enigmaMachine.getAlphabet().charAt(positions[i]));
        }
        String reflectorStr=Integer.toString(reflector);

        StringBuilder plugBoardStr=new StringBuilder();
        for(int i=0;i<plugBoard.length;i++){
            plugBoardStr.append(this.enigmaMachine.getAlphabet().charAt(plugBoard[i]));
        }

        ArrayList<String>result=new ArrayList<>();
        result.add(rotorString.toString());
        result.add(positionCharString.toString());
        result.add(reflectorStr);
        result.add(plugBoardStr.toString());
        return result;
    }

    public void initTheMachine(int[]usedRotors,char[]positions,int reflector,int[]plugBoard){
        /////////////////////////////////////////here

        this.enigmaMachine.chooseRotorsToBeUsed(usedRotors);
        //int i=this.enigmaMachine.getUsedRotorsCount()-1;
        int i=0;
        for(Rotor rotor:this.enigmaMachine.usedRotors) {
            while (rotor.forwardEntry.get(0) != positions[i]) {
                rotor.turnAround();
            }
            //i--;
            i++;
        }
        //for(int i=0;i<this.enigmaMachine.getUsedRotorsCount();i++){
            //this.enigmaMachine.usedRotors.get(i).setRotorToStartingPosition(positions[i]);

        //}
        this.enigmaMachine.chooseReflectorToBeUsed(reflector);

        int res1;
        int res2;
        for(int j=0;j<plugBoard.length;j=j+2){
            res1=plugBoard[j];
            res2=plugBoard[j+1];
            this.enigmaMachine.plugBoard.addAnotherPlugin(res1,res2);
        }
    }
        public void randomPicks(char[]array,int size){
            int res;

            for(int i=0;i<this.enigmaMachine.getUsedRotorsCount();i++){
                Random random=new Random();
                res=random.nextInt(size);
                array[i]=this.enigmaMachine.getAlphabet().charAt(res);
            }
        }

    @Override
    public DataFromEngineToUiEncryptedString inputProcessing(DataFromUiToEngineStringToEncrypt encrypt) {

        String strToEncrypt=encrypt.getStr();
        HashSet<Character>lettersNotInAlphabet=new HashSet<>();
        boolean result;
        for(int i=0;i<strToEncrypt.length();i++){
            result=checkIfLetterIsInAlphabet(strToEncrypt.charAt(i));
            if(result==false){
                lettersNotInAlphabet.add(strToEncrypt.charAt(i));
            }
        }
        if(!(lettersNotInAlphabet).isEmpty()){
            throw new InputStringToEncryptException("The letters "+lettersNotInAlphabet+" do not appear in the alphabet of the machine.");
        }

        encodedCounter++;
        long before=System.nanoTime();
        String encryptedStringToReturn=this.enigmaMachine.encryption(strToEncrypt);
        long after=System.nanoTime();
        long res=after-before;
        this.allOfTheHistory.get(allConfig).setAllTheData(encrypt.getStr(),encryptedStringToReturn,res);
        DataFromEngineToUiEncryptedString data=new DataFromEngineToUiEncryptedString(encryptedStringToReturn);

        return data;
    }

    @Override
    public DataFromEngineToUiEncryptedString inputProcessCharacter(DataFromUiToEngineStringToEncrypt encrypt,boolean flag,boolean isDone,String theString){

        if(flag==true){
            chars=new ArrayList<>();
            times=new ArrayList<>();
        }
        String strToEncrypt=encrypt.getStr();
        HashSet<Character>lettersNotInAlphabet=new HashSet<>();
        boolean result;
        for(int i=0;i<strToEncrypt.length();i++){
            result=checkIfLetterIsInAlphabet(strToEncrypt.charAt(i));
            if(result==false){
                lettersNotInAlphabet.add(strToEncrypt.charAt(i));
            }
        }
        if(!(lettersNotInAlphabet).isEmpty()){
            throw new InputStringToEncryptException("The letters "+lettersNotInAlphabet+" do not appear in the alphabet of the machine.");
        }

        encodedCounter++;
        long before=System.nanoTime();
        String encryptedStringToReturn=this.enigmaMachine.encryption(strToEncrypt);
        long after=System.nanoTime();
        long res=after-before;
        chars.add(encrypt.getStr());
        times.add(res);

        if(isDone==true){
            StringBuilder stringBuilder=new StringBuilder();
            long sum=0;
            for(int i=0;i<chars.size()-1;i++){
                stringBuilder.append(chars.get(i));
                sum+=times.get(i);
            }

            this.allOfTheHistory.get(allConfig).setAllTheData(stringBuilder.toString(),theString,sum);
        }
        //this.allOfTheHistory.get(allConfig).setAllTheData(encrypt.getStr(),encryptedStringToReturn,res);
        DataFromEngineToUiEncryptedString data=new DataFromEngineToUiEncryptedString(encryptedStringToReturn);

        return data;
    }


    public boolean checkIfLetterIsInAlphabet(char letter){

        String alphabet=this.enigmaMachine.getAlphabet();
        for(int i=0;i<alphabet.length();i++) {
            if (letter == alphabet.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DataFromEngineToUi resetSettings() {

            this.startManuallyCurrent = this.theFirstOneSinceFileEpoch;
            this.startManuallyFirstOne = this.theFirstOneSinceFileEpoch;

            int i=0;
            for(Rotor rotor:this.enigmaMachine.usedRotors){
                int res=this.usedOnesPositions.get(i).charValue();
                while(rotor.forwardEntry.get(0)!=res){
                    rotor.turnAround();
                }
                //rotor.setRotorToStartingPosition(this.usedRotorsFirstPositions.get(i));
                i++;
            }
            resetPressed=true;
            DataFromEngineToUi dataFromEngineToUi;
            if(!this.enigmaMachine.usedRotors.isEmpty()){
                 dataFromEngineToUi =new DataFromEngineToUi("The setting have been reset to the first configuration.");
            }else{
                dataFromEngineToUi=new DataFromEngineToUi("There is no data to reset. Please inert the data first.");
            }
            return dataFromEngineToUi;
    }

    @Override
    public DataFromEngineToUi historyAndStatistics() {
        StringBuilder stringBuilder=new StringBuilder();

        for(int i=0;i<this.allOfTheHistory.size();i++){
                stringBuilder.append(this.allOfTheHistory.get(i).resultString + "\n");

                    stringBuilder.append(this.allOfTheHistory.get(i).allOfDataHisAndStat + "\n");

        }

        DataFromEngineToUi data=new DataFromEngineToUi(stringBuilder.toString());

        return data;
    }


    public void insertAllDataOfMachine(){
       HashMap<Integer,Rotor> allRotors =this.enigmaMachine.allRotors;
       int usedRotors=this.enigmaMachine.getUsedRotorsCount();
       HashMap<Integer, Reflector> allReflectors=this.enigmaMachine.allReflectors;
       HashMap<Integer,Integer>plugBoard=this.enigmaMachine.plugBoard.mappingPairs;
       String alphabet=this.enigmaMachine.getAlphabet();
       this.details=new CurrentMachineDetails();
       details.setAllRotors(allRotors);
       details.setUsedRotorsSize(usedRotors);
       details.setAllReflectors(allReflectors);
       details.setPlugBoard(plugBoard);
       details.setAlphabet(alphabet);
       details.setMachine(this.enigmaMachine);
       details.setDictionary(this.dictionary);
       details.setNumberOfAgents(this.numberOfAgents);
       details.setSizeOfAlphabet(this.enigmaMachine.getAlphabetSize());
       details.setNumberOfAllies(numberOfAllies);
       details.setLevel(level);
       details.setAllNumberOfRotors(allRotors.size());
       details.setAllNumberOfReflectors(allReflectors.size());

    }

    @Override
    public CurrentMachineDetails getDetails() {
        return details;
    }
    public EnigmaMachine getEnigmaMachine(){
        return this.enigmaMachine;
    }
    public HashSet<String> getDictionary(){

        return this.dictionary;

    }
    public int getNumberOfAgents(){
        return this.numberOfAgents;
    }


}
