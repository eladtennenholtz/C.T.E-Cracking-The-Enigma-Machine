package exceptionsFromXml;



public class RotorsCountException extends RuntimeException{

    private String answer;
    public RotorsCountException(String answer){
        this.answer=answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void printException(){
        System.out.println("The amount of rotors that are used by the machine is not accurate");
    }
}
