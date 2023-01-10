package utils;

public class Constants {


    public final static int REFRESH_RATE = 2000;

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/components/main/uBoatMain.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/components/login/login.fxml";
    //public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/chat/client/component/chatroom/chat-room-main.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    public final static String CONTEXT_PATH = "/enigmaWeb_war";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginUBoat";
    public final static String FILE_UPLOAD_PAGE=FULL_SERVER_PATH+"/upload-file";
    public final static String MACHINE_DETAILS_PAGE=FULL_SERVER_PATH+"/machineDetails";
    public final static String MACHINE_SETTING_DETAILS_PAGE=FULL_SERVER_PATH+"/machineSettingDetails";
    public final static String SET_ROTORS_PAGE=FULL_SERVER_PATH+"/setRotorsMachine";
    public final static String SET_POSITIONS_PAGE=FULL_SERVER_PATH+"/setPositionsMachine";
    public final static String SET_REFLECTOR_PAGE=FULL_SERVER_PATH+"/setReflectorMachine";
    public final static String SET_PLUGS_PAGE=FULL_SERVER_PATH+"/setPlugsMachine";
    public final static String SET_ALL_MANUALLY_PAGE=FULL_SERVER_PATH+"/setAllManuallyMachine";
    public final static String DISPLAY_MACHINE_SETTINGS=FULL_SERVER_PATH+"/displayMachineSettings";
    public final static String SET_MACHINE_RANDOM_PAGE=FULL_SERVER_PATH+"/setMachineRandom";
    public final static String ENCRYPT_STRING=FULL_SERVER_PATH+"/encrypt";
    public final static String RESET_SETTINGS=FULL_SERVER_PATH+"/reset";
    public final static String READY_TO_START_CONTEST_UBOAT=FULL_SERVER_PATH+"/readyToStartContestUBoat";
    public final static String CONTEST_STARTED_UBOAT_PAGE=FULL_SERVER_PATH+"/contestStartedUBoat";
    public final static String RECEIVE_CANDIDATES_UBOAT_SERVLET=FULL_SERVER_PATH+"/receiveCandidatesUBoatServlet";
    public final static String FOUND_THE_WINNER_UBOAT=FULL_SERVER_PATH+"/foundTheWinnerUBoat";
    public final static String LOGOUT_UBOAT_PAGE=FULL_SERVER_PATH+"/logoutUBoat";

    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

}
