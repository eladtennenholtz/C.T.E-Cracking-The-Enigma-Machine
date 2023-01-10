package components.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AllCandidatesDataTableView {
    private StringProperty colDecrypt;
    private StringProperty colTaskProduced;
    private StringProperty colConfiguration;


    public AllCandidatesDataTableView(String colDecrypt,String colTaskProduced,String colConfiguration){
        this.colDecrypt=new SimpleStringProperty(colDecrypt);
        this.colConfiguration=new SimpleStringProperty(colConfiguration);
        this.colTaskProduced=new SimpleStringProperty(colTaskProduced);
    }

    public String getColDecrypt() {
        return colDecrypt.get();
    }

    public void setColDecrypt(String colDecrypt) {
        this.colDecrypt.set(colDecrypt);
    }

    public String getColConfiguration() {
        return colConfiguration.get();
    }

    public void setColConfiguration(String colConfiguration) {
        this.colConfiguration.set(colConfiguration);
    }

    public String getColTaskProduced() {
        return colTaskProduced.get();
    }

    public void setColTaskProduced(String colTaskProduced) {
        this.colTaskProduced.set(colTaskProduced);
    }
}
