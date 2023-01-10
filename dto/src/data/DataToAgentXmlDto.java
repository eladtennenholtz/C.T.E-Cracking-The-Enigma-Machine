package data;

public class DataToAgentXmlDto {
    private String xml;
    private String encryptedString;
    private int totalTasks;

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getXml() {
        return xml;
    }

    public void setEncryptedString(String encryptedString) {
        this.encryptedString = encryptedString;
    }

    public String getEncryptedString() {
        return encryptedString;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getTotalTasks() {
        return totalTasks;
    }



}
