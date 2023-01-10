package exceptionFromUserInput;

public class InputStringToEncryptException extends RuntimeException{

    private String answer;

    public InputStringToEncryptException(String answer){
        this.answer=answer;
    }

    public String getAnswer() {
        return answer;
    }
}
