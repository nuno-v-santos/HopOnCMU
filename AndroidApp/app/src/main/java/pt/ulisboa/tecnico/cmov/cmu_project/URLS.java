package pt.ulisboa.tecnico.cmov.cmu_project;

/**
 * Created by espada on 11-04-2018.
 */

public class URLS {

    //private static final String ROOT_URL = "http://194.210.159.185:1234/";
    public static final String SERVER_IP = "10.0.2.2";
    private static final String ROOT_URL = "https://" + SERVER_IP + ":1234/";

    public static final String URL_REGISTER = ROOT_URL + "register";
    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_LOGOUT = ROOT_URL + "logout";
    public static final String URL_GET_INFO = ROOT_URL + "android/userInfo/";
    public static final String URL_GET_MONUMENTS = ROOT_URL + "android/monuments/";
    public static final String URL_GET_QUESTIONS = ROOT_URL + "android/monumentQuestions/";
    public static final String URL_POST_USER_ANSWERS = ROOT_URL + "android/userAnswers/";
    public static final String URL_CHECK_VERSION = ROOT_URL + "android/version/";
    public static final String URL_SYNC = ROOT_URL + "android/sync/";
    public static final String URL_POST_EVENTS_POOL = ROOT_URL + "android/sync/events/";
    public static final String URL_POST_ANSWERS_POOL = ROOT_URL + "android/sync/answers/";
    public static final String URL_POST_SERVER_POOL = ROOT_URL + "android/sync/server/";

}
