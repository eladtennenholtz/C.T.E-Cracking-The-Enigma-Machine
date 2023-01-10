package utils;

public class ConstantsAllies {

    public final static int REFRESH_RATE = 2000;
    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/components/main/alliesMain.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/components/login/loginAllies.fxml";
    //public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/chat/client/component/chatroom/chat-room-main.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    public final static String CONTEXT_PATH = "/enigmaWeb_war";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginUBoat";
    public final static String CONTESTS_DATA_PAGE = FULL_SERVER_PATH + "/contestsData";
    public final static String READY_FOR_REG_PAGE = FULL_SERVER_PATH + "/readyForRegContest";
    public final static String TEAMS_AGENTS_DATA_PAGE=FULL_SERVER_PATH+"/teamsAgentsData";
    public final static String READY_FOR_CONTEST_ALLY_PAGE = FULL_SERVER_PATH + "/readyForContestAlly";
    public final static String ALL_TEAMS_DATA_FOR_CONTEST_PAGE=FULL_SERVER_PATH+"/allTeamsDataForContest";
    public final static String ENCRYPTED_STRING_AND_START_CONTEST_PAGE=FULL_SERVER_PATH+"/encryptedStringAndStartContestData";
    public final static String CANDIDATES_FOR_ALLIES_PAGE=FULL_SERVER_PATH+"/candidatesForAllies";
    public final static String PROGRESS_OF_AGENTS_ALLIES_PAGE=FULL_SERVER_PATH+"/progressOfAgents";
    public final static String TOTAL_PROGRESS_DATA_OF_DM_PAGE=FULL_SERVER_PATH+"/totalProgressOfDm";
    public final static String CONTEST_FINISHED_ALLIES_PAGE=FULL_SERVER_PATH+"/contestFinishedAllies";
    public final static String FINISHED_CONTEST_BUTTON_PRESSED=FULL_SERVER_PATH+"/finishedContestButtonPressedPage";
}
