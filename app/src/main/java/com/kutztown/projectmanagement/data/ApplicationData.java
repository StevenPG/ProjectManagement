package com.kutztown.projectmanagement.data;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kutztown.projectmanagement.controller.ActivityController;

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
    final static public int SPLASH_TIME_OUT = 100;

    // Share preference check attributes
    final static public String PREFERENCE_FILENAME = "UserName";
    final static public SharePreferenceCheck myPreference = new SharePreferenceCheck();
    static public boolean answer = false;
    static public AppCompatDelegate delegate;

    // Mutable fields below:
    /**
     * Global element that effectively "caches" the current user's data. Should it
     * be used, it must be updated whenever the user's account is updated.
     */
    static public UserTableEntry currentUser;
    static public TaskTableEntry currentTask;

    /**
     * This flag keeps track of whether or not the user
     * is logged in. This value should be checked at the start of,
     * and during every activity to make sure that the user has
     * valid access to each activity and the generated content.
     */
    static public boolean isLoggedIn = false;

    /**
     * Run this to logout before sending user back to loginActivity
     */
    static public void logoutUser(){
        ApplicationData.currentProject = null;
        ApplicationData.currentUser = null;
        ApplicationData.currentTask = null;
        ApplicationData.isLoggedIn = false;
    }

    static public ProjectTableEntry currentProject;

    // represent the key that is passed with the project name
    // when adding user to a particular project
    final public static String ProjectName = "ProjectName";

    static public boolean checkIfLoggedIn(Context appContext){
        if(this.isLoggedIn){
            return true;
        } else {
            return false;
        }
    }

}
