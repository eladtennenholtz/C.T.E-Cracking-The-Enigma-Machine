package components.contestPage;

import javafx.beans.property.SimpleStringProperty;

public class AllCandidatesForAlliesTableView {

        private SimpleStringProperty colDecrypt;
        private SimpleStringProperty colAgentCandidate;
        private SimpleStringProperty colCode;


        public AllCandidatesForAlliesTableView(String codDecrypt,String colAgentCandidate,String colCode){
            this.colDecrypt=new SimpleStringProperty(codDecrypt);
            this.colAgentCandidate=new SimpleStringProperty(colAgentCandidate);
            this.colCode=new SimpleStringProperty(colCode);
        }

        public String getColDecrypt() {
            return colDecrypt.get();
        }

        public void setColDecrypt(String colDecrypt) {
            this.colDecrypt.set(colDecrypt);
        }

    public String getColAgentCandidate() {
        return colAgentCandidate.get();
    }

    public void setColAgentCandidate(String colAgentCandidate) {
        this.colAgentCandidate.set(colAgentCandidate);
    }

    public String getColCode() {
            return colCode.get();
        }

        public void setColCode(String colCode) {
            this.colCode.set(colCode);
        }
    }


