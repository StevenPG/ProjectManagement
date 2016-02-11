package com.kutztown.projectmanagement.data;
/**
 * Created by Steven Gantz on 1/28/2016.
 *
 * This class contains all data data used
 * by the application that can be altered
 * between compilations.
 *
 * Note: All values are final, const, and data, that is
 *  they cannot be changed or updated and are constant.
 *  All attributes should be in ALL_CAPS to denote their
 *  immutable status
 *
 * Usage:
 *
 * ApplicationData.<attribute>
 *
 */
public final class ApplicationData {

    /**
     * IP address of web service
     */
    final static public String SERVER_IP = "104.238.131.94";

    /**
     * Web service port
     */
    final static public String SERVER_PORT = "5000";

    /**
     * How long the splash screen will display
     * 3000ms seems to be the best length of time
     * debug: 500ms
     */
    final static public int SPLASH_TIME_OUT = 3000;
    final static public String PREFERENCE_FILENAME = "UserName";
    final static public SharePreferenceCheck myPreference = new SharePreferenceCheck();
    static public boolean answer = false;
}
