package components.main.model;

import com.google.gson.JsonArray;

import java.util.ArrayList;


public class AllEncryptedStringAndStartContestData {

    private String encryptedString;
    private String contestStart;


    public void setAllData(JsonArray jsonArray) {

            String[] res = jsonArray.toString().split(",");
            encryptedString=res[0].toString().substring(2,res[0].length()-1);
            contestStart=res[1].substring(1,res[1].length()-2);
    }
    public void setAllDataNew(String []dataNew){
        encryptedString=dataNew[0];
        contestStart=dataNew[1];
    }

    public String getEncryptedString() {
        return encryptedString;
    }

    public String getContestStart() {
        return contestStart;
    }
}
