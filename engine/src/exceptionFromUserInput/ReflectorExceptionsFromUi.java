package exceptionFromUserInput;

public class ReflectorExceptionsFromUi extends RuntimeException{
    private String answer;

    public ReflectorExceptionsFromUi(String answer){
        this.answer=answer;
    }

    public String getAnswer() {
        return answer;
    }
}
