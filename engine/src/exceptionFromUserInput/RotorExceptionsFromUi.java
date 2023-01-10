package exceptionFromUserInput;

public class RotorExceptionsFromUi extends RuntimeException {

    private String answer;

    public RotorExceptionsFromUi(String result){
        this.answer=result;
    }

    public String getAnswer() {
        return answer;
    }


}
