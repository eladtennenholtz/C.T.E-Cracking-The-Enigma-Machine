package exceptionsFromXml;



public class XmlException extends RuntimeException{

    public void printException(){
        System.out.println("File is not a XML.");
    }

}
