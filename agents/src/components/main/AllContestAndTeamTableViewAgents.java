package components.main;

import javafx.beans.property.SimpleStringProperty;

public class AllContestAndTeamTableViewAgents {
    private SimpleStringProperty colBattle;
    private SimpleStringProperty colUBoat;
    private SimpleStringProperty colStatus;
    private SimpleStringProperty colLevel;
    private SimpleStringProperty colAlliesMax;
    private SimpleStringProperty colAlliesLogged;
    private SimpleStringProperty colTeam;
    private SimpleStringProperty colAgents;
    private SimpleStringProperty colTaskSize;

    public AllContestAndTeamTableViewAgents(String colBattle,String colUBoat,String colStatus,String colLevel,String colAlliesMax,String colAlliesLogged,String colTeam,String colAgents,String colTaskSize){


        this.colBattle=new SimpleStringProperty(colBattle);
        this.colUBoat=new SimpleStringProperty(colUBoat);
        this.colStatus=new SimpleStringProperty(colStatus);
        this.colLevel=new SimpleStringProperty(colLevel);
        this.colAlliesMax=new SimpleStringProperty(colAlliesMax);
        this.colAlliesLogged=new SimpleStringProperty(colAlliesLogged);
        this.colTeam=new SimpleStringProperty(colTeam);
        this.colAgents=new SimpleStringProperty(colAgents);
        this.colTaskSize=new SimpleStringProperty(colTaskSize);
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

    public String getColTeam() {
        return colTeam.get();
    }

    public void setColTeam(String colTeam) {
        this.colTeam.set(colTeam);
    }

    public String getColAgents() {
        return colAgents.get();
    }

    public void setColAgents(String colAgents) {
        this.colAgents.set(colAgents);
    }

    public String getColTaskSize() {
        return colTaskSize.get();
    }

    public void setColTaskSize(String colTaskSize) {
        this.colTaskSize.set(colTaskSize);
    }


}


