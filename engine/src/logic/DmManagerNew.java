package logic;

import data.DataFromAgentToDm;
import machine.EnigmaMachine;
import reflector.Reflector;
import rotor.Rotor;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class DmManagerNew {


    private Engine engine;
    //private ThreadFactory customThreadFactory;
    private BlockingQueue<Task> blockingQueue;
    //private BlockingQueue<DataFromAgentToDm>dataFromAgentToDmBlockingQueue;
    //private ThreadPoolExecutor executor;
    private Thread taskCreator;
    //private Thread taskReceiver;
    private String encryptedString;
    private int numberOfAgents;
    private String levelOfTask;
    private int sizeOfTask;
    //private int allConfigPossible;
    private HashSet<String> dictionary;
    double index=0;
   // Consumer<Double> toSendCounter;
   // Consumer<Long>toSendTimeOfAll;
    int totalTasks;
    Object pausingObject;
    Object lockForQueue;
    private int counterForQueue;
    private int taskedPushedToQueue;
    private boolean contestFinished;

    public DmManagerNew() {

    }
    public void startTheDmWork(Engine engine,int sizeOfTask,String levelOfTask,String encryptedString,int totalTasks) {
        this.engine=engine;
        this.numberOfAgents=numberOfAgents;
        this.levelOfTask=levelOfTask;
        this.sizeOfTask=sizeOfTask;
        this.totalTasks=totalTasks;
        this.encryptedString=encryptedString;
        this.dictionary=this.engine.getDetails().getDictionary();
        this.blockingQueue=new LinkedBlockingQueue<>(1000);
        lockForQueue=new Object();
        this.counterForQueue=0;
        contestFinished=false;


        this.taskCreator=new Thread(()->{

            if(levelOfTask.toLowerCase().equals("easy")) {

                easyTaskCreator(false,0,false,new int[0]);

            }else if(levelOfTask.toLowerCase().equals("medium")){
                mediumTaskCreator(false,new int[0]);

            }else if(levelOfTask.toLowerCase().equals("hard")){

                hardTaskCreator(false,new int[0]);

            }else if(levelOfTask.toLowerCase().equals("impossible")||levelOfTask.toLowerCase().equals("insane")){

                impossibleTaskCreator();


            }else{

            }

        });
        taskCreator.start();
    }

    /*
    @Override
    public void run() {

        if(levelOfTask=="easy"){

            easyTaskCreator(false,0,false,new int[0]);

        }else if(levelOfTask=="medium"){
            mediumTaskCreator(false,new int[0]);

        }else if(levelOfTask=="hard"){

            hardTaskCreator(false,new int[0]);

        }else if(levelOfTask=="impossible"){

            impossibleTaskCreator();


        }else{

        }

    }

     */

    private void easyTaskCreator(boolean res,int reflectorId,boolean ifHardHappen,int[]hardRotors){

        int[]array=new int[this.engine.getDetails().getUsedRotorsSize()];
        int[] usedRotors=new int[engine.getDetails().getUsedRotorsSize()];


        ArrayList<ArrayList<Integer>>myArray=new ArrayList<>();
        for (int i=0;i<this.engine.getDetails().getUsedRotorsSize();i++){
            ArrayList<Integer>tmp=new ArrayList<>();
            for(int j=0;j<this.engine.getDetails().getAlphabet().length();j++){
                tmp.add(j);
            }
            myArray.add(tmp);
        }

        for(int w=0;w< array.length;w++){
            array[w]=0;
        }

        Reflector reflector;
        double howMuchConfig;
        if(res==false) {
            reflector = duplicateReflector(engine.getDetails().getMachine().usedReflector);
        }else{
            reflector=duplicateReflector(engine.getDetails().getAllReflectors().get(reflectorId));
        }

        if(ifHardHappen==true){
            for(int k=0;k<this.engine.getDetails().getMachine().usedRotors.size();k++){
                usedRotors[k]=hardRotors[k];
            }

        }else{
            for(int k=0;k<this.engine.getDetails().getMachine().usedRotors.size();k++){
                usedRotors[k]=this.engine.getDetails().getMachine().usedRotors.get(k).getRotorId();
            }
        }


        taskedPushedToQueue=0;
        howMuchConfig=Math.pow(engine.getDetails().getAlphabet().length(),engine.getDetails().getUsedRotorsSize());
        for(int i=0;i<howMuchConfig/sizeOfTask;i++) {
            try {
                EnigmaMachine machine=new EnigmaMachine(engine.getDetails().getUsedRotorsSize(),engine.getDetails().getAlphabet());
                machine.addReflector(reflector.getReflectorId(), reflector.getReflectorMapping());
                for(int j=0;j<this.engine.getDetails().getAllRotors().size();j++){
                    machine.addRotor(engine.getDetails().getAllRotors().get(j+1).getRotorId(),engine.getDetails().getAllRotors().get(j+1).getNotchPosition(),engine.getDetails().getAllRotors().get(j+1).forwardEntry,engine.getDetails().getAllRotors().get(j+1).backwardEntry);
                }
                //int[] usedRotors=new int[engine.getDetails().getUsedRotorsSize()];
                //for(int k=0;k<this.engine.getDetails().getMachine().usedRotors.size();k++){
                //  usedRotors[k]=this.engine.getDetails().getMachine().usedRotors.get(k).getRotorId();
                //}
                machine.chooseRotorsToBeUsed(usedRotors);
                machine.chooseReflectorToBeUsed(reflector.getReflectorId());

                ArrayList<ArrayList<Integer>>thePositions=new ArrayList<>();



                int tmpSizeOfTask=sizeOfTask;
                while(tmpSizeOfTask>0) {

                    ArrayList<Integer>posForConfig=new ArrayList<>();

                    for (int r = 0; r < myArray.size(); r++) {

                        posForConfig.add(myArray.get(r).get(0));
                    }
                    thePositions.add(posForConfig);

                    for(int f=0;f<myArray.size();f++){
                        if(f==0){
                            if(myArray.get(f).get(0)==this.engine.getDetails().getAlphabet().length()-1){
                                Collections.rotate(myArray.get(f),-1);
                                array[0]=1;

                            }else{
                                Collections.rotate(myArray.get(f),-1);
                            }
                        }
                        else {

                            if(array[f-1]==1){
                                if(myArray.get(f).get(0)==this.engine.getDetails().getAlphabet().length()-1) {
                                    array[f] = 1;
                                }
                                Collections.rotate(myArray.get(f),-1);
                                array[f-1]=0;
                            }
                        }
                    }

                    tmpSizeOfTask--;
                }

               // System.out.println(thePositions);
                //System.out.println(Thread.currentThread());
                //System.out.println(thePositions.size());
                index++;
                //if(stop==true){
                  //  return;
                //}
                //toSendCounter.accept(index/486000);
                //while(pause==true){
                  //  synchronized (pausingObject){
                    //    pausingObject.wait();
                    //}
                //}
                //if(stop==true){
                  //  return;
                //}
                //synchronized (lockForQueue) {
                   // ArrayList<Integer>integerArrayList=new ArrayList<>();
                    //for(int w=0;w<usedRotors.length;w++){
                      //  integerArrayList.add(usedRotors[w]);
                    //}
                    //getBlockingQueue().put(new Task(usedRotors,reflector.getReflectorId(), thePositions,  index, totalTasks));

                taskedPushedToQueue++;

                if(contestFinished){
                    break;
                }

                    blockingQueue.put(new Task(usedRotors,reflector.getReflectorId(),thePositions.get(0),index, totalTasks));
                //}
            } catch (Exception e) {

            }
            if(contestFinished){
                break;
            }
        }

    }
    private void mediumTaskCreator(boolean resOfIfHardWasSent,int[]rotors) {

        for(int i=0;i<this.engine.getDetails().getAllReflectors().size();i++){
            if(resOfIfHardWasSent==true) {
                easyTaskCreator(true, i + 1,true,rotors);
            }else{
                easyTaskCreator(true,i+1,false,rotors);
            }

        }
    }
    private void hardTaskCreator(boolean isFromImpossible,int[]rotors){

        int f,fact=1;
        int number=this.engine.getDetails().getUsedRotorsSize();//It is the number to calculate factorial
        for(f=1;f<=number;f++){
            fact=fact*f;
        }

        if(isFromImpossible==false) {
            int[][] allTheLists = new int[fact][this.engine.getDetails().getUsedRotorsSize()];
            int[] arr = new int[this.engine.getDetails().getUsedRotorsSize()];
            for (int i = 0; i < this.engine.getDetails().getUsedRotorsSize(); i++) {
                arr[i] = this.engine.getDetails().getMachine().usedRotors.get(i).getRotorId();
            }
            permuteCounter = 0;
            permute(arr, 0, allTheLists);
            permuteCounter = 0;
            for (int j = 0; j < this.engine.getDetails().getUsedRotorsSize() * 2; j++) {
                mediumTaskCreator(true, allTheLists[j]);
            }
        }else{

            int[][] allTheListsSecond = new int[fact][this.engine.getDetails().getUsedRotorsSize()];
            permuteCounter = 0;
            permute(rotors, 0, allTheListsSecond);
            permuteCounter = 0;
            for (int j = 0; j < this.engine.getDetails().getUsedRotorsSize() * 2; j++) {
                mediumTaskCreator(true, allTheListsSecond[j]);
            }
        }
    }

    public static void permute(int[] intArray, int start,int[][]allTheLists) {
        for(int i = start; i < intArray.length; i++){
            int temp = intArray[start];
            intArray[start] = intArray[i];
            intArray[i] = temp;
            permute(intArray, start + 1,allTheLists);
            intArray[i] = intArray[start];
            intArray[start] = temp;
        }
        if (start == intArray.length - 1) {
            //System.out.println(java.util.Arrays.toString(intArray));
            allTheLists[permuteCounter]= Arrays.stream(intArray).toArray();
            permuteCounter++;
        }
    }
    public static int permuteCounter=0;

    private void impossibleTaskCreator() {


        ArrayList<int[]>myListOfArrays=generate(this.engine.getDetails().getAllRotors().size(),this.engine.getDetails().getUsedRotorsSize());
        for (int i=0;i<myListOfArrays.size();i++){
            int[]tmp=new int[myListOfArrays.get(i).length];
            tmp=myListOfArrays.get(i);
            for(int j=0;j<tmp.length;j++){
                tmp[j]++;
            }
            hardTaskCreator(true,tmp);

        }

    }

    public static ArrayList<int[]> generate(int n, int r) {
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

        return combinations;
    }
    public Reflector duplicateReflector(Reflector reflector)
    {
        Reflector retVal=new Reflector(reflector.getReflectorId(),reflector.getReflectorMapping());
        return retVal;
    }

    public  BlockingQueue<Task> getBlockingQueue() {
        return blockingQueue;
    }

    public int getCounterForQueue() {
        return counterForQueue;
    }

    public void setCounterForQueue(int counterForQueue) {
        this.counterForQueue = counterForQueue;
    }

    public  void stopBruteForce() throws InterruptedException {

        //executor.shutdown();
        //executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    public int getTaskedPushedToQueue() {
        return taskedPushedToQueue;
    }

    public void setContestFinished(boolean contestFinished) {
        this.contestFinished = contestFinished;
    }

    public Thread getTaskCreator() {
        return taskCreator;
    }

}
