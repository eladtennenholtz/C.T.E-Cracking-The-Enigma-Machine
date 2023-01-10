package data;

public class DataFromEngineToUiEncryptedString {

    private String encryptedString;
    private boolean isError;

    public DataFromEngineToUiEncryptedString(String encryptedString){
        this.encryptedString=encryptedString;
        this.isError=false;
    }

    public String getEncryptedString() {
        return encryptedString;
    }

    public void setEncryptedString(String encryptedString){

        this.encryptedString=encryptedString;
        this.isError=true;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean getIsError(){
        return this.isError;
    }
}
