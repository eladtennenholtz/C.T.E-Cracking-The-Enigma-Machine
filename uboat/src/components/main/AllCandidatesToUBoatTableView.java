package components.main;

import javafx.beans.property.SimpleStringProperty;

public class AllCandidatesToUBoatTableView {

    private SimpleStringProperty colDecrypt;
    private SimpleStringProperty colTeam;
    private SimpleStringProperty colCode;


    public AllCandidatesToUBoatTableView(String codDecrypt,String colTeam,String colCode){
        this.colDecrypt=new SimpleStringProperty(codDecrypt);
        this.colTeam=new SimpleStringProperty(colTeam);
        this.colCode=new SimpleStringProperty(colCode);
    }

    public String getColDecrypt() {
        return colDecrypt.get();
    }

    public void setColDecrypt(String colDecrypt) {
        this.colDecrypt.set(colDecrypt);
    }

    public String getColTeam() {
        return colTeam.get();
    }

    public void setColTeam(String colTeam) {
        this.colTeam.set(colTeam);
    }

    public String getColCode() {
        return colCode.get();
    }

    public void setColCode(String colCode) {
        this.colCode.set(colCode);
    }
}
