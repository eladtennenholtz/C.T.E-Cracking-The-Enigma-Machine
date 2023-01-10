package reflector;

import java.util.HashMap;

public class Reflector {

    private final int reflectorId;
    private HashMap<Integer,Integer>reflectorMapping;

    public Reflector(int reflectorId,HashMap<Integer,Integer>reflectorMapping){
        this.reflectorId=reflectorId;
        this.reflectorMapping=reflectorMapping;
    }

    //This method receives the row when coming forward and returns the coming backward.
    public int mappingOfReflector(int row){
        return this.reflectorMapping.get(row);
    }

    public int getReflectorId() {
        return reflectorId;
    }

    public HashMap<Integer, Integer> getReflectorMapping() {
        return reflectorMapping;
    }
}
