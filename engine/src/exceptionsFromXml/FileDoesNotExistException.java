package exceptionsFromXml;

public class FileDoesNotExistException extends RuntimeException {

    public void printException(){
        System.out.println("File does not exist.");
    }
}
