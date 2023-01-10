import data.*;
import dm.DmManager;
import exceptionFromUserInput.*;
import exceptionsFromXml.*;
import logic.Engine;
import logic.EngineFirst;

import java.util.ArrayList;
import java.util.Scanner;

public class UiMain {

    public static boolean noChoice=false;
    public static int counter=0;
    public static void printMenu(String[] options) {
        System.out.println("Please enter your option from the following 1-8 options:");
        for (String option : options) {
            System.out.println(option);
        }
        System.out.print("Your choice : ");
    }
    public static void printReflectorMenu(String[] reflectorOptions) {
        System.out.println("Please enter your option from the following 1-5 options:");
        for (String reflector : reflectorOptions) {
            System.out.println(reflector);
        }
    }
    public static void main(String[] args) {

        Engine engine = new EngineFirst();


        String[] options = {"1- Load machine xml file settings",
                "2- Show machine settings",
                "3- Set up machine settings",
                "4- Random machine settings",
                "5- Encode input",
                "6-Reset settings",
                "7-Statistics and history",
                "8-Exit"
        };
        String[] reflectorOptions = {"1-I",
                "2-II",
                "3-III",
                "4-IV",
                "5-V"

        };
        Scanner scanner = new Scanner(System.in);
        String option = "";
        boolean systemIsRunning=true;
        boolean firstCommandDone=false;
        boolean insertedSettingMachine=false;
        boolean firstSettingToTheMachine=false;
        boolean firstEver=false;
        boolean all=false;

        String path = "";
        while (systemIsRunning) {
            printMenu(options);
            option = scanner.nextLine();
            boolean chosenToInsertXmlFile = true;
            //"C:\\Users\\new\\IdeaProjects\\C.T.E-Cracking The Enigma Machine\\engine\\src\\resources\\ex1-sanity-small.xml"
            if (option.equals("1")) {
                firstCommandDone=false;

                while (chosenToInsertXmlFile) {

                    System.out.println("Please enter a path to your XML file:");
                    Scanner input = new Scanner(System.in);
                    String pathRes = input.nextLine();
                    try {
                        DataFromEngineToUiXml data = engine.readTheMachineXmlFile(pathRes);
                        System.out.println(data.getXmlFileLoaded());
                        path = pathRes;
                        firstCommandDone=true;
                        firstEver=true;
                        insertedSettingMachine=false;
                        all=false;
                        break;

                    } catch (FileDoesNotExistException e) {
                        e.printException();


                    } catch (XmlException e) {
                        e.printException();

                    } catch (JaxbException e) {
                        e.printException();
                    }
                    catch (RotorsCountException e){
                        System.out.println(e.getAnswer());
                    }
                    catch (AlphabetSizeException e){
                        System.out.println(e.getAnswer());
                    }
                    catch (IdRotorException e){
                        e.printException();
                    }
                    catch (MappingRotorException e){
                        e.printException();
                    }
                    catch (NotchException e){
                        e.printException();
                    }
                    catch (IdReflectorException e){
                        System.out.println(e.getAnswer());
                    }
                    catch (MappingReflectorException e){
                        e.printException();
                    }
                    catch (ExceptionAll e){
                        System.out.println(e.getResult());
                    }

                    chosenToInsertXmlFile=inputChecking(chosenToInsertXmlFile);

                        }
                if(firstEver==true){
                    firstCommandDone=true;
                }
                    }

             else if (option.equals("2")) {
                 if(firstCommandDone==true) {
                     DataFromEngineToUiShowingTheMachine resultData=engine.displayingTheMachineSetting();
                     System.out.println("There are "+resultData.getHowManyRotorsTotal()+ " rotors total available.");
                     System.out.println("There are "+resultData.getHowManyUsedRotors()+" rotors used in the machine.");
                     System.out.println("There are "+resultData.getTotalReflectors()+" reflectors total available");
                     System.out.println("There were "+resultData.getHowManyEncrypt()+" encoded/decoded procedures.");
                     if(resultData.getFirstStage()==""){
                         System.out.println("There were no setting of the machine.");
                     }else {
                         System.out.println("The origin configuration of the machine is "+resultData.getFirstStage()+".");
                         System.out.println("The current configuration of the machine is"+resultData.getCurrentStage()+".");
                     }
                 }else{
                     System.out.println("You need to load the content of the xml file of the machine first.");
                 }

            } else if (option.equals("3")) {

                 if(firstCommandDone==true) {
                     boolean inputRotors = true;
                     boolean inputPositions = true;
                     boolean inputReflector=true;
                     boolean inputPlugBoards=true;
                     noChoice=false;
                     ArrayList<String>inputs=new ArrayList<>();
                     insertedSettingMachine=false;


                     while (inputRotors == true) {
                         System.out.println("Please enter all the rotors (by there Id number) that you want to use.");
                         System.out.println("The input  will be so that the last rotor you choose will be the rightest rotor in the machine and the first input will be the leftest.");
                         System.out.println("The input should be decimal numbers seperated with commas with no spaces between each other. for instance : 23,54,13");
                         Scanner input = new Scanner(System.in);
                         String resultRotors = input.nextLine();
                         DataFromUiToEngineInputRotors data = new DataFromUiToEngineInputRotors(resultRotors);
                         try {
                             DataFromEngineToUiRotors resultData = engine.settingRotorsToMachine(data);
                             System.out.println(resultData.getData());
                             inputs.add(data.getRotors());
                             inputRotors = false;

                         } catch (RotorExceptionsFromUi exceptions) {
                             System.out.println(exceptions.getAnswer());
                             inputRotors = inputChecking(inputRotors);
                             if(noChoice==true){
                                 break;
                             }
                         }
                     }

                     while (inputPositions == true && noChoice==false) {
                         System.out.println("Please enter what you want the starting position of each rotor to be: ");
                         System.out.println("The input should be within the alphabet of the machine.");
                         System.out.println("The input should be continuous so that the first input is for the leftest rotor starting position and the last input is for the rightest rotor starting position. ");
                         System.out.println("For instance : 4DBA");
                         Scanner input = new Scanner(System.in);
                         String resultPositions = input.nextLine();
                         DataFromUiToEngineInputRotorsPositioning data = new DataFromUiToEngineInputRotorsPositioning(resultPositions.toUpperCase());
                         try {
                             DataFromEngineToUiRotorsPositioning resultData=engine.settingPositionsToMachine(data);
                             System.out.println(resultData.getAnswer());
                             inputs.add(data.getPositions());
                             inputPositions=false;
                         } catch (RotorPositionsExceptionFromUi e) {
                             System.out.println(e.getAnswer());
                             inputPositions = inputChecking(inputPositions);
                             if(noChoice==true){
                                 break;
                             }
                         }
                     }
                     while(inputReflector==true &&noChoice==false) {
                         System.out.println("Please enter what reflector ID do you want to use.");
                         printReflectorMenu(reflectorOptions);
                         Scanner input=new Scanner(System.in);
                         String resultReflectors = input.nextLine();
                         DataFromUiToEngineReflector data=new DataFromUiToEngineReflector(resultReflectors);

                         try {
                             DataFromEngineToUiReflector resultData=engine.settingReflectors(data);
                             System.out.println(resultData.getAnswer());
                             inputs.add(data.getReflector());
                             inputReflector=false;
                         }catch (ReflectorExceptionsFromUi e){
                             System.out.println(e.getAnswer());
                             inputReflector=inputChecking(inputReflector);
                             if(noChoice==true){
                                 break;
                             }
                         }
                     }

                     while(inputPlugBoards==true && noChoice==false){
                         System.out.println("Please enter all the plugins that you want to include in the machine.");
                         System.out.println("The input should be continuous so that each plugin should be 2 different letters from the alphabet. ");
                         System.out.println("There cannot be the same letter in different plugins. If you dont want plugins in the machine, please press only the key 'enter'.");
                         System.out.println("Input for instance: dk49  This input makes a plugin with the letters 'd' and 'k' and another plugin with '4' and '9'.");
                         Scanner input = new Scanner(System.in);
                         String resultPlugins = input.nextLine();
                         DataFromUiToEngineInputPlugBoard data = new DataFromUiToEngineInputPlugBoard(resultPlugins.toUpperCase());
                         try {
                            DataFromEngineToUiPlugBoard resultData= engine.settingPlugBoard(data);
                             System.out.println(resultData.getAnswer());
                             inputs.add(data.getPlugBoard());
                             inputPlugBoards=false;
                         }catch (PlugBoardExceptionsFromUi e){
                             System.out.println(e.getAnswer());
                             inputPlugBoards=inputChecking(inputPlugBoards);
                             if(noChoice==true){
                                 break;
                             }
                         }
                     }
                     if(inputs.size()==4) {
                         engine.settingAllManually(inputs);
                         System.out.println("Machine was loaded successfully.");
                         insertedSettingMachine=true;
                         firstSettingToTheMachine=true;
                         all=true;
                         DmManager dmManager=new DmManager(engine,3,"easy",10);
                         dmManager.startBruteForce();
                     }

                 }else{
                     System.out.println("You need to load the content of the xml file of the machine first.");
                 }
            }
             else if (option.equals("4")) {
                 if(firstCommandDone==true) {
                     firstSettingToTheMachine=true;
                     insertedSettingMachine=true;
                     DataFromEngineToUi resultedData = engine.selectingAnInitialCodeConfigurationAutomatically();
                     System.out.println("The automatic starting configuration that has been chosen is: " + resultedData.getResult());
                     all=true;
                 }else{
                     System.out.println("You need to load the content of the xml file of the machine first.");
                 }

            } else if (option.equals("5")) {
                 if(firstEver==true) {
                     firstCommandDone=true;
                 }
                 if(firstSettingToTheMachine==true && all==true){
                     insertedSettingMachine=true;
                 }

                 if(insertedSettingMachine==true &&firstCommandDone==true){
                     boolean inputEncrypted=true;
                     noChoice=false;
                     while(inputEncrypted==true && noChoice==false) {
                         System.out.println("Please enter the string for the machine:");
                         Scanner input = new Scanner(System.in);
                         String result = input.nextLine();
                         DataFromUiToEngineStringToEncrypt data = new DataFromUiToEngineStringToEncrypt(result.toUpperCase());
                         try {
                             DataFromEngineToUiEncryptedString resultedString = engine.inputProcessing(data);
                             System.out.println("The encrypted string: "+resultedString.getEncryptedString());
                             counter++;
                             inputEncrypted=false;
                         } catch (InputStringToEncryptException e) {
                             System.out.println(e.getAnswer());
                             inputEncrypted=inputChecking(inputEncrypted);
                             if(noChoice==true){
                                 break;
                             }
                         }
                     }
                 }else if(insertedSettingMachine==false && firstCommandDone==true){
                     System.out.println("You need to set up the machine setting. Choose option 3 or 4.");
                 }
                 else{
                     System.out.println("You need to load the content of the xml file of the machine first.");
                 }

            } else if (option.equals("6")) {
                if(firstEver==true) {
                    firstCommandDone=true;
                }
                if(firstSettingToTheMachine==true){
                    insertedSettingMachine=true;
                }
                 if(firstCommandDone==true &&insertedSettingMachine==true) {

                     DataFromEngineToUi resultedData = engine.resetSettings();
                     System.out.println(resultedData.getResult());
                 }else if(firstCommandDone==true && insertedSettingMachine==false){
                     System.out.println("You need to set up the machine setting. Choose option 3 or 4. ");
                 }else{
                     System.out.println("You need to load the content of the xml file of the machine first.");
                 }

            } else if (option.equals("7")) {
                 if(firstCommandDone==true) {
                     if(all==true) {
                         DataFromEngineToUi dataRes = engine.historyAndStatistics();
                         System.out.println(dataRes.getResult());
                     }
                     else {
                         System.out.println("There is no history to show. You can choose options 3 or 4 to set the machine.");
                     }
                 }else{
                     System.out.println("You need to load the content of the xml file of the machine first.");
                 }
            } else if (option.equals("8")) {
                System.out.println("exiting....");
                systemIsRunning=false;
            } else {
                System.out.println("You didnt choose an option between 1-8");
            }
        }
    }

    public static boolean inputChecking(boolean outerLoop) {
        boolean answers = true;
        Scanner inputForChecking = new Scanner(System.in);

        while (answers) {
            System.out.println("Would you like to try again?");
            System.out.println("If yes please insert 'y' else insert 'n'");

            String choice = inputForChecking.nextLine();

            if (!(choice.equals("Y") || choice.equals("y"))) {
                if (!(choice.equals("N") || choice.equals("n"))) {
                    System.out.println("You entered incorrectly. Please enter 'y' or 'n' . ");
                } else {
                    answers = false;
                    outerLoop = false;
                }
            }
            if (choice.equals("Y") || choice.equals("y")) {
                answers = false;
                return outerLoop;
            }
        }
        noChoice=true;
        return outerLoop;
    }
}
