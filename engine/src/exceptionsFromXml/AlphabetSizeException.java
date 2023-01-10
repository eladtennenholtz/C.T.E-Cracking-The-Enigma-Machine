package exceptionsFromXml;


public class AlphabetSizeException extends RuntimeException{

    private String answer;

    public AlphabetSizeException(String answer){
        this.answer=answer;
    }
    public void printException(){
        System.out.println("Alphabet size is not an even number");
    }

    public String getAnswer() {
        return answer;
    }
}
