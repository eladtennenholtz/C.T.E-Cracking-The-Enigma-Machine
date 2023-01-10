package components.dashBoardPage;

import javafx.beans.property.SimpleStringProperty;

public class AllContestDataForTableView {

   // private String colBattle;
    //private String colUBoat;
   //private Boolean colStatus;
   //private Integer colAlliesMax;
   //private Integer colAlliesLogged;
    private SimpleStringProperty colBattle;
    private SimpleStringProperty colUBoat;
    private SimpleStringProperty colStatus;
    private SimpleStringProperty colLevel;
    private SimpleStringProperty colAlliesMax;
    private SimpleStringProperty colAlliesLogged;


    public AllContestDataForTableView(String colBattle,String colUBoat,String colStatus,String colLevel,String colAlliesMax,String colAlliesLogged){
        //this.colBattle=colBattle;
        //this.colUBoat=colUBoat;
        this.colBattle=new SimpleStringProperty(colBattle);
        this.colUBoat=new SimpleStringProperty(colUBoat);
        this.colStatus=new SimpleStringProperty(colStatus);
        this.colLevel=new SimpleStringProperty(colLevel);
        this.colAlliesMax=new SimpleStringProperty(colAlliesMax);
        this.colAlliesLogged=new SimpleStringProperty(colAlliesLogged);
    }

    public String getColBattle() {
        return colBattle.get();
    }

    public void setColBattle(String colBattle) {
        this.colBattle.set(colBattle);
    }

    public String getColUBoat() {
        return colUBoat.get();
    }

    public void setColUBoat(String colUBoat) {
        this.colUBoat.set(colUBoat);
    }

    public String getColStatus() {
        return colStatus.get();
    }

    public void setColStatus(String colStatus) {
        this.colStatus.set(colStatus);
    }

    public String getColLevel() {
        return colLevel.get();
    }

    public void setColLevel(String colLevel) {
        this.colLevel.set(colLevel);
    }

    public String getColAlliesMax() {
        return colAlliesMax.get();
    }

    public void setColAlliesMax(String colAlliesMax) {
        this.colAlliesMax.set(colAlliesMax);
    }

    public String getColAlliesLogged() {
        return colAlliesLogged.get();
    }

    public void setColAlliesLogged(String colAlliesLogged) {
        this.colAlliesLogged.set(colAlliesLogged);
    }
}
