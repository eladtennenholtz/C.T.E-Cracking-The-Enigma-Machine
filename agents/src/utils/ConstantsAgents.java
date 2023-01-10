package utils;

public class ConstantsAgents {

    public final static int REFRESH_RATE = 2000;
    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/components/main/agentsMain.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/components/login/loginAgents.fxml";
    //public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/chat/client/component/chatroom/chat-room-main.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    public final static String CONTEXT_PATH = "/enigmaWeb_war";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginUBoat";
    public final static String AGENTS_TEAMS = FULL_SERVER_PATH + "/agentsTeams";
    public final static String AGENTS_CONTEST_AND_TEAM_DATA = FULL_SERVER_PATH + "/agentsContestAndTeamData";
    public final static String TASKS_FROM_DM_TO_AGENT_PAGE = FULL_SERVER_PATH + "/tasksFromDmToAgent";
    public final static String XML_DATA_TO_AGENT = FULL_SERVER_PATH + "/xmlDataToAgent";
    public final static String CANDIDATES_FROM_AGENTS_TO_UBOAT = FULL_SERVER_PATH + "/candidatesFromAgentsToUBoat";
    public final static String SEND_AGENTS_PROGRESS_TO_SERVER = FULL_SERVER_PATH + "/sendAgentsProgressToServer";
    public final static String CONTEST_FINISHED_AGENT = FULL_SERVER_PATH + "/contestFinishedAgent";
    public final static String CHECK_ALLY_PRESSED_FINISHED = FULL_SERVER_PATH + "/checkIfAllyPressedFinished";
    public final static String IS_CONTEST_STARTED_AGENT_PAGE = FULL_SERVER_PATH + "/isContestStartedAgent";

}
