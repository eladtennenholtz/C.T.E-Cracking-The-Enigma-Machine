package exceptionFromUserInput;

public class RotorPositionsExceptionFromUi extends RuntimeException{
    private String answer;

    public RotorPositionsExceptionFromUi(String result){
        this.answer=result;
    }

    public String getAnswer() {
        return answer;
    }
}
