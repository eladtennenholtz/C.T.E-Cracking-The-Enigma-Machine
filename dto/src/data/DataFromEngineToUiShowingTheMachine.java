package data;

public class DataFromEngineToUiShowingTheMachine {
    private String currentStage;
    private String firstStage;
    private int totalReflectors;
    private int howManyEncrypt;
    private int howManyUsedRotors;
    private int howManyRotorsTotal;

    public DataFromEngineToUiShowingTheMachine(int howManyRotorsTotal,int howManyUsedRotors,int totalReflectors,int howManyEncrypt,String firstStage,String currentStage){
        this.howManyRotorsTotal=howManyRotorsTotal;
        this.howManyUsedRotors=howManyUsedRotors;
        this.totalReflectors=totalReflectors;
        this.howManyEncrypt=howManyEncrypt;
        this.firstStage=firstStage;
        this.currentStage=currentStage;
    }

    public String getCurrentStage() {
        return currentStage;
    }

    public String getFirstStage() {
        return firstStage;
    }

    public int getHowManyEncrypt() {
        return howManyEncrypt;
    }

    public int getHowManyRotorsTotal() {
        return howManyRotorsTotal;
    }

    public int getHowManyUsedRotors() {
        return howManyUsedRotors;
    }

    public int getTotalReflectors() {
        return totalReflectors;
    }





}

