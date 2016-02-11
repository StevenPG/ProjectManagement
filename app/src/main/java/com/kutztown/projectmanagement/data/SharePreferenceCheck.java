
    package com.kutztown.projectmanagement.data;

    import android.app.Application;
    import android.content.Context;
    import android.content.SharedPreferences;
    import android.preference.PreferenceActivity;
    import android.view.View;
    import android.widget.Toast;
    import com.kutztown.projectmanagement.data.ApplicationData;
    /**
     * Created by yunior on 2/3/2016.
     * this class assist in determining if it is the first time an user has login to
     * the app. it checks the share preference database to see is an User file exist. if true
     * the user will be redirected to the main screen without having to login.
     */
    public class SharePreferenceCheck extends Application {
        public void preference(Context context )
        {

            SharedPreferences setting = context.getSharedPreferences(ApplicationData.PREFERENCE_FILENAME,MODE_PRIVATE);

            if(setting.contains(ApplicationData.PREFERENCE_FILENAME))
            {

                String m = setting.getString(ApplicationData.PREFERENCE_FILENAME,"");
                Toast.makeText(context,m,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"hello World",Toast.LENGTH_LONG).show();
            }
        }
        // this method will be used in the create account screen. its purpose is to
        // save a key, value of the user's name in the sharepreference database. it will be
        // use to allow the user to go to the main screen of the app without having to login.
        public void WriteToSharePreference(View myView)
        {
            SharedPreferences setting = getSharedPreferences(ApplicationData.PREFERENCE_FILENAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString(ApplicationData.PREFERENCE_FILENAME, myView.toString());
            editor.apply();

        }
    }