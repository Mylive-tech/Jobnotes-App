package com.jobnotes.Utility;


import com.jobnotes.Splash;

public final class WebUrl {
    public static final String LOGIN = "json_webservice2.3.5.php?action=user_authentication";

    //public static String BASE_DOMAION_NAME=SPUser.getString(Splash.splash,SPDomain.DOMAIN_NAME);
    //public static final String LOCATION_PROPERTIES = SPUser.getString(Splash.splash, SPDomain.DOMAIN_NAME) + "json_webservice2.3.5.php?action=proplisting";
    public static final String IMAGEPATH = "upload/";
    public static final String SAVE_REPORT = "json_webservice2.3.5.php?action=save_report";
    public static final String UPDATE_STATUS = "json_webservice2.3.5.php?action=updatejob";
    public static final String UPLOAD_PHOTOS = "json_webservice2.3.5.php?action=file_upload";
    public static final String ADD_NOTES = "json_webservice2.3.5.php?action=add_note";
    public static final String DISPLAY_NOTES = "json_webservice2.3.5.php?action=display_notes";
    //public static final String BASE_URL = "http:/snownotes.jobnotes.net/";
    public static String BASE_URL = "";//"http://dev.jobnotes.net/";
public static String current_frag="";
    public static void ShowLog(String message) {
        System.out.println(message);
    }

}
