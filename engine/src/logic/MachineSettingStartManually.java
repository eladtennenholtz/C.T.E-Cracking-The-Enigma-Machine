package logic;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class MachineSettingStartManually {

    private ArrayList<String> data;
    String alphabet;
    public String resultString;
    private int[]notch;
    private String before;
    private String after;
    private long time;
    public ArrayList<String> allOfDataHisAndStat;
    public int[]positionsNumeric;


    public MachineSettingStartManually(ArrayList<String> results ,int[]positionsNumeric,String alphabet,int[]notch){
        this.data=results;
        this.alphabet=alphabet;
        this.notch=notch;
        this.allOfDataHisAndStat=new ArrayList<>();
        this.positionsNumeric=positionsNumeric;
    }
    public void makeStringToUser(){

        String rotors=this.data.get(0);
        String regexComma = ",";
        String[] strRotors = rotors.split(regexComma);
        String positionsChar=this.data.get(1);
        String reflector=this.data.get(2);
        String plugBoard=this.data.get(3);
        int[]positionsNumeric=this.positionsNumeric;
        //String positions=this.data.get(1);
        //String reflector=this.data.get(2);
        //String plugBoard=this.data.get(3);
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("<");
        //int n =strRotors.length-1;
        int n=0;

        for(int i=0;i<strRotors.length;i++){
            stringBuilder.append(strRotors[i]);
            //if(notch[n]>=positionsNumeric[i]){
                //stringBuilder.append("(" + (notch[n] - positionsNumeric[i]) + ")");
            //}else{
                //stringBuilder.append("(" + (alphabet.length()-positionsNumeric[i]) + ")");
                //stringBuilder.append("(" + ((alphabet.length()-positionsNumeric[i])+notch[n]) + ")");

            //}

            //if(notch[n]>=changeToPositionNumeric(positions.charAt(i))) {
                //stringBuilder.append("(" + (notch[n] - changeToPositionNumeric(positions.charAt(i))) + ")");
            //}else{
                //stringBuilder.append("(" + (alphabet.length()-changeToPositionNumeric(positions.charAt(i))) + ")");
            //}
            if(i!=strRotors.length-1){
                stringBuilder.append(",");
            }


        }
        stringBuilder.append(">");
        stringBuilder.append("<");

        for(int j=0;j<strRotors.length;j++){
            //stringBuilder.append(positions.charAt(j));
            stringBuilder.append(positionsChar.charAt(j));
            if(notch[n]>=positionsNumeric[j]){
                stringBuilder.append("(" + (notch[n] - positionsNumeric[j]) + ")");
            }else{
                //stringBuilder.append("(" + (alphabet.length()-positionsNumeric[i]) + ")");
                stringBuilder.append("(" + ((alphabet.length()-positionsNumeric[j])+notch[n]) + ")");

            }
            if(j!=strRotors.length-1){
                stringBuilder.append(",");
            }
            n++;
        }
        stringBuilder.append(">");
        stringBuilder.append("<");
        stringBuilder.append(changeToRome(reflector));
        stringBuilder.append(">");
        stringBuilder.append("<");
        for(int k=0;k<plugBoard.length();k=k+2){
            stringBuilder.append(plugBoard.charAt(k));
            stringBuilder.append("|");
            stringBuilder.append(plugBoard.charAt(k+1));
            if(k!=plugBoard.length()-2){
                stringBuilder.append(",");
            }

        }
        stringBuilder.append(">");
        this.resultString=stringBuilder.toString();
    }
    public int changeToPositionNumeric(char letter){

        for(int i=0;i<this.alphabet.length();i++){
            if(letter==alphabet.charAt(i)){
                return i;
            }
        }
        return -1;
    }
    public String changeToRome(String num){

        if(num.equals("1")){
            return "I";
        }if(num.equals("2")){
            return "II";
        }if(num.equals("3")){
            return "III";
        }if(num.equals("4")){
            return "IV";
        }if (num.equals("5")){
            return "V";
        }

        return "";
    }

    public void setAllTheData(String first,String second,long num){

        StringBuilder theString=new StringBuilder();

        theString.append(this.allOfDataHisAndStat.size()+1 +". <"+first+"> -> <"+second+"> ("+num+")");
        this.allOfDataHisAndStat.add(theString.toString());
    }

    public ArrayList<String> getAllOfDataHisAndStat() {
        return allOfDataHisAndStat;
    }


}
