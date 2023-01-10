package exceptionsFromXml;

public class MappingReflectorException extends RuntimeException{

    public void printException(){
        System.out.println("Reflector mapped to the same place");
    }
}
