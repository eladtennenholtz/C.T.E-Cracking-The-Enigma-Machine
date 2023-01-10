package logic;

import data.DataCandidatesToAllies;
import data.DataCandidatesToUBoat;
import data.DataFromAgentToDm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;

public class BattleField {

    private String battleField;
    private String UBoatName;
    private int allies;
    private int alliesLogged;
    private String level;
    private boolean status;
    private Engine engine;

    private String encryptedStringForContest;
    private boolean uBoatReadyToStartContest;

    private HashMap<Ally, DmManagerNew> alliesInBattleMap;
    private ArrayList<String> alliesInBattle;
    boolean alliesReadyToStartContest;
    private int numberAlliesReadyToStartContest;
    private int totalTasks;

    private boolean contestStarted;
    private int sizeOfTaskAlly;
    private String myXml;
    private String originalCode;

    private int allConfigurations;
    private ArrayList<DataFromAgentToDm>dataFromAgentToDms;
    private ArrayList<DataCandidatesToUBoat>candidatesToUBoats;

    private int candidatePositionInArray;
    private boolean isContestOver;
    private String winnerOfContest;
    private int index;



    public BattleField() {

        alliesInBattle = new ArrayList<>();
        alliesInBattleMap = new HashMap<>();
        alliesLogged = 0;
        uBoatReadyToStartContest = false;
        alliesReadyToStartContest = false;
        contestStarted = false;
        numberAlliesReadyToStartContest = 0;
        encryptedStringForContest = "";
        myXml="";
        originalCode="";
        candidatePositionInArray=0;
        isContestOver=false;
        status=false;
    }

    public String getMyXml() {
        return myXml;
    }

    public void setMyXml(String myXml) {
        this.myXml = myXml;
    }

    public synchronized void setBattleFieldName(String battleField) {
        this.battleField = battleField;
    }

    public synchronized void setUBoatName(String UBoatName) {
        this.UBoatName = UBoatName;
    }

    public synchronized void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setAllies(int allies) {
        this.allies = allies;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setAlliesLogged(int alliesLogged) {
        this.alliesLogged = alliesLogged;
    }

    public void setBattleField(String battleField) {
        this.battleField = battleField;
    }

    public boolean addAllieToBattle(Ally ally) {
        if (alliesInBattle.contains(ally.getName())) {
            return false;
        } else {
            if (alliesLogged == allies) {
                return false;
            } else {
                alliesInBattle.add(ally.getName());
                alliesInBattleMap.put(ally, new DmManagerNew());
                alliesLogged++;
                return true;
            }
        }
    }

    public Engine getEngine() {
        return engine;
    }

    public String getBattleField() {
        return battleField;
    }

    public String getLevel() {
        return level;
    }

    public int getAllies() {
        return allies;
    }

    public String getUBoatName() {
        return UBoatName;
    }

    public int getAlliesLogged() {
        return alliesLogged;
    }

    public boolean isStatus() {
        return status;
    }

    public ArrayList<String> getAlliesInBattle() {
        return alliesInBattle;
    }

    public String getEncryptedStringForContest() {
        return encryptedStringForContest;
    }

    public HashMap<Ally, DmManagerNew> getAlliesInBattleMap() {
        return alliesInBattleMap;
    }

    public boolean isAlliesReadyToStartContest() {
        return alliesReadyToStartContest;
    }

    public boolean isuBoatReadyToStartContest() {
        return uBoatReadyToStartContest;
    }

    public void setUBoatReadyToStartContest(boolean res) {
        uBoatReadyToStartContest = true;
        originalCode=this.engine.displayingTheMachineSetting().getFirstStage();
        //לא בטוח שזה טוב לשים את זה פה..לחזור לבדוק אם צריך
        if (alliesReadyToStartContest) {
            startContest();
        }
    }


    public void setAlliesReadyToStartContest(boolean alliesReadyToStartContest) {
        this.alliesReadyToStartContest = alliesReadyToStartContest;
    }

    public synchronized void setNumberAlliesReadyToStartContest() {
        this.numberAlliesReadyToStartContest++;
    }

    public int getNumberAlliesReadyToStartContest() {
        return numberAlliesReadyToStartContest;
    }

    public void setEncryptedStringForContest(String encryptedStringForContest) {
        this.encryptedStringForContest = encryptedStringForContest;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getAllConfigurations() {
        return allConfigurations;
    }

    public boolean isContestStarted() {
        return contestStarted;
    }

    public void startContest() {
        dataFromAgentToDms=new ArrayList<>();
        candidatesToUBoats=new ArrayList<>();
        status = true;
        contestStarted = true;
        int res = (int) Math.pow(engine.getDetails().getSizeOfAlphabet(), engine.getDetails().getUsedRotorsSize());
        allConfigurations=res;
        int number = engine.getDetails().getUsedRotorsSize();
        int f, fact = 1;
        for (f = 1; f <= number; f++) {
            fact = fact * f;
        }
        if (level.toLowerCase().equals("easy")) {

            totalTasks = res / sizeOfTaskAlly;
            allConfigurations=res;

        } else if (level.toLowerCase().equals("medium")) {
            totalTasks = (res * engine.getDetails().getAllReflectors().size()) / sizeOfTaskAlly;
            allConfigurations=res * engine.getDetails().getAllReflectors().size();

        } else if (level.toLowerCase().equals("hard")) {
            totalTasks = (res * engine.getDetails().getAllReflectors().size() * fact) / sizeOfTaskAlly;
            allConfigurations=res * engine.getDetails().getAllReflectors().size() * fact;

        } else if (level.toLowerCase().equals("impossible")||level.toLowerCase().equals("insane")) {
            totalTasks = (res * engine.getDetails().getAllReflectors().size() * fact * generate(engine.getDetails().getAllRotors().size(), engine.getDetails().getUsedRotorsSize())) / sizeOfTaskAlly;
            allConfigurations=res * engine.getDetails().getAllReflectors().size() * fact * generate(engine.getDetails().getAllRotors().size(), engine.getDetails().getUsedRotorsSize());
        }

            alliesInBattleMap.forEach((ally, dmManagerNew) -> {
                //dmManagerNew = new DmManagerNew(engine, sizeOfTaskAlly, getLevel(), encryptedStringForContest,totalTasks);
                //new Thread(dmManagerNew).start();
                ally.setTotalTasks(totalTasks);
                dmManagerNew.startTheDmWork(engine, ally.getSizeOfTask(), getLevel(), encryptedStringForContest,totalTasks);
            });
        }
        public int calculateAllConfig(){
            int res = (int) Math.pow(engine.getDetails().getSizeOfAlphabet(), engine.getDetails().getUsedRotorsSize());
            allConfigurations=res;
            int number = engine.getDetails().getUsedRotorsSize();
            int f, fact = 1;
            for (f = 1; f <= number; f++) {
                fact = fact * f;
            }
            if (level.toLowerCase().equals("easy")) {

                return res;

            } else if (level.toLowerCase().equals("medium")) {
              return res * engine.getDetails().getAllReflectors().size();

            } else if (level.toLowerCase().equals("hard")) {
                return res * engine.getDetails().getAllReflectors().size() * fact;

            } else if (level.toLowerCase().equals("impossible")||level.toLowerCase().equals("insane")) {
               return res * engine.getDetails().getAllReflectors().size() * fact * generate(engine.getDetails().getAllRotors().size(), engine.getDetails().getUsedRotorsSize());
            }else {
                return 0;
            }
        }
    public void setSizeOfTaskAlly(int sizeOfTaskAlly) {
        this.sizeOfTaskAlly = sizeOfTaskAlly;
    }

    public static int generate(int n, int r) {
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
    public void dataCandidatesToSendUBoat(DataFromAgentToDm[]dataFromAgentToDmsStaticArray){

        for(int i=0;i<dataFromAgentToDmsStaticArray.length;i++){
            index=i;
            dataFromAgentToDms.add(dataFromAgentToDmsStaticArray[i]);
            candidatesToUBoats.add(new DataCandidatesToUBoat(dataFromAgentToDmsStaticArray[i].getDecrypted(),dataFromAgentToDmsStaticArray[i].getTeamName(),dataFromAgentToDmsStaticArray[i].getConfiguration()));
            String name=dataFromAgentToDmsStaticArray[i].getTeamName();
            this.alliesInBattleMap.forEach((ally, dmManagerNew) ->{
                if(ally.getName().equals(name)){

                    ally.addToDataCandidates(new DataCandidatesToAllies(dataFromAgentToDmsStaticArray[index].getDecrypted(),dataFromAgentToDmsStaticArray[index].getAgentName(),dataFromAgentToDmsStaticArray[index].getConfiguration()));
                }
            });
        }
    }

    public ArrayList<DataCandidatesToUBoat> getCandidatesToUBoats() {
        return candidatesToUBoats;
    }

    public int getCandidatePositionInArray() {
        return candidatePositionInArray;
    }

    public void setCandidatePositionInArray(int candidatePositionInArray) {
        this.candidatePositionInArray = candidatePositionInArray;
    }

    public String getWinnerOfContest() {
        return winnerOfContest;
    }

    public void setWinnerOfContest(String winnerOfContest) {
        this.winnerOfContest = winnerOfContest;
    }

    public boolean isContestOver() {
        return isContestOver;
    }

    public void setContestOver(boolean contestOver) {
        isContestOver = contestOver;
    }


    public void removeDmFromAlly(Ally ally) {
        this.getAlliesInBattleMap().get(ally).setContestFinished(true);
        this.getAlliesInBattleMap().get(ally).getTaskCreator().interrupt();
        //this.getAlliesInBattleMap().replace(ally,new DmManagerNew());

    }

    public void removeAll() {
        alliesInBattle = new ArrayList<>();
        alliesInBattleMap = new HashMap<>();
        alliesLogged = 0;
        uBoatReadyToStartContest = false;
        alliesReadyToStartContest = false;
        contestStarted = false;
        numberAlliesReadyToStartContest = 0;
        encryptedStringForContest = "";
        myXml="";
        originalCode="";
        candidatePositionInArray=0;
        isContestOver=false;
        dataFromAgentToDms.clear();
        winnerOfContest="";
        status=false;
        setContestOver(true);
        setCandidatePositionInArray(0);

    }
    public synchronized void removeAlly(Ally ally){
        this.getAlliesInBattleMap().remove(ally);
        for(int i=0;i<this.alliesInBattle.size();i++){
            if(alliesInBattle.get(i).equals(ally.getName())){
                alliesInBattle.remove(i);
            }
        }
        alliesLogged--;

    }
}