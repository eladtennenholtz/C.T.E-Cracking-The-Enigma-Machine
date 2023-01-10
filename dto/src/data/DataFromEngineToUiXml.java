package data;

public class DataFromEngineToUiXml {

    private boolean xmlFileLoaded=false;

    public DataFromEngineToUiXml(){
        this.xmlFileLoaded=true;

    }

    public String getXmlFileLoaded(){

        if(this.xmlFileLoaded==true) {
            return "Great! The File was loaded successfully! ";
        }else{
            return "Too bad.. The File was not loaded successfully. ";
        }

    }

}
