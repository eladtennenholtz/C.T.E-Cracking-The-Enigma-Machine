package logicTasks;

import atomic.TheAtomicClass;
import data.DataFromAgentToDm;
import machine.EnigmaMachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class AgentTasksNew implements Runnable{


    private int sizeOfTask;
    private EnigmaMachine enigmaMachine;
    private ArrayList<Integer> rotorsId;
    private int reflectorId;
    private String encryptedString;
    private ArrayList<ArrayList<Integer>> thePositions;
    private HashSet<String> dictionary;
    private BlockingQueue<DataFromAgentToDm> dataFromAgentToDmBlockingQueue;
    private double indexOfSize;
    private Consumer<Double> toSendCounter;
    private int totalTasks;
    private Consumer<Long>toSendTimeOfAll;
    private long timeStart;
    private long timeFinish;
    private IntConsumer tasksInQueue;
    private int taskInQueueNum;
    private Object lock;
    private IntConsumer isLast;
    private TheAtomicClass.Counter counter;
    TheAtomicClass.Counter counterCandidates;
    TheAtomicClass.Counter counterCandidatesPreformed;



    public AgentTasksNew(EnigmaMachine machine, String encryptedString, ArrayList<ArrayList<Integer>>thePositions, HashSet<String>dictionary, BlockingQueue<DataFromAgentToDm>dataFromAgentToDmBlockingQueue, double indexOfSize, Consumer<Double> toSendCounter, int totalTasks, Consumer<Long>toSendTimeOfAll, IntConsumer tasksInQueue, int taskInQueueNum, IntConsumer isLast, TheAtomicClass.Counter counter,TheAtomicClass.Counter counterCandidates,TheAtomicClass.Counter counterCandidatesPreformed){

        this.enigmaMachine=machine;
        this.rotorsId=rotorsId;
        this.reflectorId=reflectorId;
        this.encryptedString=encryptedString;
        this.thePositions=thePositions;
        this.dictionary=dictionary;
        this.dataFromAgentToDmBlockingQueue=dataFromAgentToDmBlockingQueue;
        this.indexOfSize=indexOfSize;
        this.toSendCounter=toSendCounter;
        this.totalTasks=totalTasks;
        this.toSendTimeOfAll=toSendTimeOfAll;
        this.tasksInQueue=tasksInQueue;
        this.taskInQueueNum=taskInQueueNum;
        this.isLast=isLast;
        this.counter=counter;
        this.counterCandidates=counterCandidates;
        this.counterCandidatesPreformed=counterCandidatesPreformed;


    }
    @Override
    public void run() {

        //System.out.println("look im here!!!!");
        //System.out.println(Thread.currentThread());
        //System.out.println(thePositions);
        //System.out.println(thePositions.size());
        timeStart=System.nanoTime();
        boolean first=true;
        char save;
        int count=0;

        ArrayList<ArrayList<Character>>entry=new ArrayList<>();
        for(int k=0;k<this.enigmaMachine.getUsedRotorsCount();k++){
            ArrayList<Character>rotor=new ArrayList<>();
            rotor.addAll(this.enigmaMachine.usedRotors.get(k).forwardEntry);
            entry.add(rotor);
        }
        for(int i=0;i<thePositions.size();i++) {

            for(int j=0;j<thePositions.get(i).size();j++){

                enigmaMachine.usedRotors.get(j).setRotorPosition(thePositions.get(i).get(j));

                char result=entry.get(j).get(thePositions.get(i).get(j));

                while(enigmaMachine.usedRotors.get(j).forwardEntry.get(0)!=result) {
                    Collections.rotate(enigmaMachine.usedRotors.get(j).forwardEntry, -1);
                    Collections.rotate(enigmaMachine.usedRotors.get(j).backwardEntry, -1);
                }
                //if(result=='a'||result=='A'){
                //  count++;
                //}
                //if(count==3){
                //  //System.out.println("yes!");
                //count=0;
                //}

            }


            StringBuilder thePositions=new StringBuilder();
            for(int k=0;k<enigmaMachine.getUsedRotorsCount();k++){
                thePositions.append(enigmaMachine.usedRotors.get(k).forwardEntry.get(0));
            }
            String strResult=enigmaMachine.encryption(encryptedString.toUpperCase());
            // System.out.println(strResult);
            //System.out.println(strResult);
            boolean res=checkIfExists(strResult);
            if(res==true){
                //System.out.println(strResult);
                //System.out.println(thePositions.toString());
                //System.out.println(this.enigmaMachine.usedReflector.getReflectorId());
                String config=createConfiguration(thePositions.toString());
                counterCandidates.increment();
                // System.out.println(config);
                //System.out.println(Thread.currentThread().getId()-10);
                try {
                    String[]str=Thread.currentThread().getName().split("-");
                    String resToSendNumberThread=str[str.length-1];
                    dataFromAgentToDmBlockingQueue.put(new DataFromAgentToDm(strResult,config,indexOfSize));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
        //indexOfSize+=1;
       // toSendCounter.accept(indexOfSize/totalTasks);
        timeFinish=System.nanoTime();
       // toSendTimeOfAll.accept(timeFinish-timeStart);


            taskInQueueNum--;
          //  synchronized (this) {
            //    tasksInQueue.accept(1);
            //}
            counter.increment();
            counterCandidatesPreformed.increment();




        if(taskInQueueNum==0){
          //  isLast.accept(0);
        }
    }
    public boolean checkIfExists(String strResult){

        String regexComma = " ";
        String[] str = strResult.split(regexComma);

        for(int i=0;i<str.length;i++){
            if(!dictionary.contains(str[i].toLowerCase())&&!dictionary.contains(str[i].toUpperCase())){
                return false;
            }
        }

        return true;
    }
    public String createConfiguration(String positions){

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("<");
        ArrayList<Integer>rotors=new ArrayList<>();
        for(int i=this.enigmaMachine.getUsedRotorsCount()-1;i>=0;i--){
            //rotors.add(this.enigmaMachine.usedRotors.get(0).getRotorId());
            stringBuilder.append(this.enigmaMachine.usedRotors.get(i).getRotorId());
            if(i!=0){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(">");
        stringBuilder.append("<");

        for( int i=this.enigmaMachine.getUsedRotorsCount()-1;i>=0;i--){
            //rotors.add(this.enigmaMachine.usedRotors.get(0).getRotorId());
            stringBuilder.append(positions.charAt(i));
            //if(this.enigmaMachine.usedRotors.get(i).getNotchPosition()>=this.enigmaMachine.usedRotors.get(i).getRotorPosition()){
            //stringBuilder.append("(" + (this.enigmaMachine.usedRotors.get(i).getNotchPosition()-this.enigmaMachine.usedRotors.get(i).getRotorPosition()) + ")");
            //}else{
            //stringBuilder.append("(" + (alphabet.length()-positionsNumeric[i]) + ")");
            // stringBuilder.append("(" + ((this.enigmaMachine.getAlphabet().length()-this.enigmaMachine.usedRotors.get(i).getRotorPosition())+this.enigmaMachine.usedRotors.get(i).getNotchPosition()) + ")");
        }
        stringBuilder.append(">");
        stringBuilder.append("<");
        stringBuilder.append(changeFromIntToRome(this.enigmaMachine.usedReflector.getReflectorId()));
        stringBuilder.append(">");

        return stringBuilder.toString();


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
}
