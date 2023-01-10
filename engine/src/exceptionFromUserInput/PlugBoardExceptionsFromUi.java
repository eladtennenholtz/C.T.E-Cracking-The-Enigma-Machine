package exceptionFromUserInput;

public class PlugBoardExceptionsFromUi extends RuntimeException{

    private String answer;

    public PlugBoardExceptionsFromUi(String answer){
        this.answer=answer;
    }

    public String getAnswer() {
        return answer;
    }
}
