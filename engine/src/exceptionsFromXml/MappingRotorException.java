package exceptionsFromXml;

public class MappingRotorException extends RuntimeException{

    public void printException(){
        System.out.println("Rotor mapped wrong");
    }
}
