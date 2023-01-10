package exceptionsFromXml;

public class ExceptionAll extends RuntimeException {

    String result;

    public ExceptionAll(String result){
        this.result=result;
    }

    public String getResult() {
        return result;
    }
}
