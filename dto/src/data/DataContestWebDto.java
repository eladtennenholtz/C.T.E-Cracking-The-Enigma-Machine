package data;

public class DataContestWebDto {
    private String battleFieldName;
    private String uBoatName;
    private boolean status;
    private String level;
    private int alliesMax;
    private int alliesLogged;

    public DataContestWebDto(){

    }

    public void setBattleFieldName(String battleFieldName) {
        this.battleFieldName = battleFieldName;
    }

    public void setuBoatName(String uBoatName) {
        this.uBoatName = uBoatName;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setAlliesMax(int alliesMax) {
        this.alliesMax = alliesMax;
    }

    public void setAlliesLogged(int alliesLogged) {
        this.alliesLogged = alliesLogged;
    }

    public String getBattleFieldName() {
        return battleFieldName;
    }

    public String getuBoatName() {
        return uBoatName;
    }

    public boolean getStatus(){
        return status;
    }

    public String getLevel() {
        return level;
    }

    public int getAlliesMax() {
        return alliesMax;
    }

    public int getAlliesLogged() {
        return alliesLogged;
    }


}
