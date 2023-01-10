package data;

public class DataFromEngineToUiMachineSettings {

    private int allRotors;
    private int usedRotors;
    private int reflectors;
    private int encodeCount;



    DataFromEngineToUiMachineSettings(int allRotors, int usedRotors, int reflectors, int encodeCount){
        this.allRotors=allRotors;
        this.usedRotors=usedRotors;
        this.reflectors=reflectors;
        this.encodeCount=encodeCount;
    }

    public int getAllRotors(){
        return this.allRotors;
    }
    public int getUsedRotors(){
        return this.usedRotors;
    }
    public int getReflectors(){
        return this.reflectors;
    }



}
