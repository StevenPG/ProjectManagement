package com.kutztown.projectmanagement.data;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.Encryption;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Steven Gantz on 1/28/2016.
 * <p/>
 * This class contains all data data used
 * by the application that can be altered
 * between compilations.
 * <p/>
 * Note: All values are final, const, and data, that is
 * they cannot be changed or updated and are constant.
 * All attributes should be in ALL_CAPS to denote their
 * immutable status
 * <p/>
 * Usage:
 * <p/>
 * ApplicationData.<attribute>
 */
public final class ApplicationData {

    /**
     * IP address of web service
     */
    final static public String SERVER_IP = "104.238.131.94";

    static public ActionMenuView amvMenu;

    final static public String FULL_URL = "http://104.238.131.94:5000";

    /**
     * Web service port
     */
    final static public String SERVER_PORT = "5000";

    /**
     * How long the splash screen will display
     * 3000ms seems to be the best length of time
     * debug: 500ms
     */
    final static public int SPLASH_TIME_OUT = 2000;

    /**
     * Globally hold the current viewed member
     */
    static public String currentViewedMember;

    // Share preference check attributes
    final static public String PREFERENCE_FILENAME = "UserPosition";
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
    static public MessageTableEntry currentMessage;

    /**
     * This flag keeps track of whether or not the user
     * is logged in. This value should be checked at the start of,
     * and during every activity to make sure that the user has
     * valid access to each activity and the generated content.
     */
    static public boolean isLoggedIn = false;
    static public String theme = "Default";

    /**
     * Run this to logout before sending user back to loginActivity
     */
    static public void logoutUser() {
        ApplicationData.isLoggedIn = false;
        ApplicationData.currentProject = null;
        ApplicationData.currentUser = null;
        ApplicationData.currentTask = null;
        ApplicationData.isLoggedIn = false;
    }

    static public ProjectTableEntry currentProject;

    // represent the key that is passed with the project name
    // when adding user to a particular project
    final public static String ProjectName = "ProjectName";

    static public boolean checkIfLoggedIn(Context appContext) {
        if (ApplicationData.isLoggedIn) {
            return true;
        } else {
            return false;
        }
    }

    /* instance of a variable to be used for the class encrytion to handle methods called.
     * it is used to enscrypt the user password before being saved in the database
     */
    static public final Encryption myEncrytion = new Encryption();
    public static int day = 0;
    public static int year = 0;
    public static int month = 0;

    /**
     * Universal method that creates a toast whenever a user tries to create
     * something that contains '--'. This is used internally as a concatenater,
     * and therefore cannot be entered in 'create' field.
     */
    static public boolean checkIfContainsDoubleStar(Context context, String testString, boolean displayToast) {
        if (testString.contains("**")) {
            if (displayToast) {
                Toast.makeText(context, "Field cannot contain ** !", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Place all of the elements of the string into a list without their
     * separators. Then remove any duplicates, before re-creating into a string.
     */
    static public String removeDuplicates(String duplicateString) {
        ArrayList<String> itemList = new ArrayList<>();

        if (duplicateString.charAt(0) == 'u') {
            duplicateString = duplicateString.substring(2, duplicateString.length() - 1);
        }

        // Split the string up into parts
        String[] itemArray = duplicateString.split("--");

        // Add the string into a list
        for (String item : itemArray) {
            itemList.add(item);
        }

        // Remove any duplicate pairings inside the list

        // Add everything to a set which will not allow duplicates, then get back
        // Using a linkedHashSet will preserve order which we want.
        Set<String> noDupes = new LinkedHashSet<>();
        noDupes.addAll(itemList);

        // Clear the list and add items from set
        itemList.clear();
        itemList.addAll(noDupes);

        // TODO - Turn the list into a string separated by the delimiter
        StringBuilder builder = new StringBuilder();
        for (String item : itemList) {
            builder.append(item);
            builder.append("--");
        }

        Log.d("dupe", builder.toString());

        String removedDoubles = builder.toString().replace("---", "--");

        return removedDoubles;
    }

    /**
     * This will get called every time the context menu is opened and
     * will pass the relevant stuff to do other stuff.
     *
     * @param context
     * @param item
     */
    static public boolean contextMenu(Context context, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Log.d("debug", "Selected Logout");
                ApplicationData.logoutUser();
                context.startActivity(ActivityController.openLoginActivity(context));
                return true;
            case R.id.profile:
                Log.d("debug", "Selected Profile");
                context.startActivity(ActivityController.openProfileActivity(context));
                return true;
            case R.id.mainmenuview:
                Log.d("debug", "Selected Main Menu view");
                context.startActivity(ActivityController.openMainActivity(context));
                return true;
            case R.id.theme:
                Log.d("debug", "Selected Theme");
                context.startActivity(ActivityController.openThemeActivity(context));
                return true;
        }
        return true;
    }
}
