package components.dashBoardPage;

import javafx.beans.property.SimpleStringProperty;

public class AllTeamsAgentsDataForTableView {

    private SimpleStringProperty colName;
    private SimpleStringProperty colThreads;
    private SimpleStringProperty colTaskSize;

    public AllTeamsAgentsDataForTableView(String colName,String colThreads,String colTaskSize){
        this.colName=new SimpleStringProperty(colName);
        this.colThreads=new SimpleStringProperty(colThreads);
        this.colTaskSize=new SimpleStringProperty(colTaskSize);
    }

    public String getColName() {
        return colName.get();
    }

    public void setColName(String colName) {
        this.colName.set(colName);
    }

    public String getColThreads() {
        return colThreads.get();
    }

    public void setColThreads(String colThreads) {
        this.colThreads.set(colThreads);
    }

    public String getColTaskSize() {
        return colTaskSize.get();
    }

    public void setColTaskSize(String colTaskSize) {
        this.colTaskSize.set(colTaskSize);
    }
}
