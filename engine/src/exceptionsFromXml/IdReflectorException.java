package exceptionsFromXml;


public class IdReflectorException extends RuntimeException {

    private String answer;

    public IdReflectorException(String answer){
        this.answer=answer;
    }

    public String getAnswer() {
        return answer;
    }


    //There are reflectors with the same ID
}
