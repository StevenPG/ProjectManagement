
    package com.kutztown.projectmanagement.data;

    import android.app.Application;
    import android.content.Context;
    import android.content.SharedPreferences;
    import android.widget.Toast;

    import com.kutztown.projectmanagement.activity.SplashActivity;
    import com.kutztown.projectmanagement.controller.ActivityController;
    import com.kutztown.projectmanagement.data.ApplicationData;
    /**
     * Created by yunior on 2/3/2016.
     * this class assist in determining if it is the first time an user has login to
     * the app. it checks the share preference database to see is an User file exist. if true
     * the user will be redirected to the main screen without having to login.
     */
    public class SharePreferenceCheck extends Application {
        public boolean preference(Context context ) {

            SharedPreferences setting = context.getSharedPreferences(ApplicationData.PREFERENCE_FILENAME, MODE_PRIVATE);

            if (setting.contains(ApplicationData.PREFERENCE_FILENAME)) {
                return true;
            } else {
                return false;
            }
        }
        // this method will be used in the create account screen. its purpose is to
        // save a key, value of the user's name in the sharepreference database. it will be
        // use to allow the user to go to the main screen of the app without having to login.
        public void WriteToSharePrefference(Context context, String UserName)
        {
            SharedPreferences setting = context.getSharedPreferences(ApplicationData.PREFERENCE_FILENAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString(ApplicationData.PREFERENCE_FILENAME, UserName);
            editor.apply();

        }

    }