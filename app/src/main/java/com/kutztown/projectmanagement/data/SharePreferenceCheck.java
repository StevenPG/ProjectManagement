
    package com.kutztown.projectmanagement.data;

    import android.content.SharedPreferences;
    import android.preference.PreferenceActivity;
    import android.widget.Toast;
    import com.kutztown.projectmanagement.data.ApplicationData;
    /**
     * Created by yunior on 2/3/2016.
     * this class assist in determining if it is the first time an user has login to
     * the app. it checks the share preference database to see is an User file exist. if true
     * the user will be redirected to the main screen without having to login.
     */
    public class SharePreferenceCheck extends PreferenceActivity {
        public void preference()
        {
            SharedPreferences setting = getSharedPreferences(ApplicationData.PREFERENCE_FILENAME, MODE_PRIVATE);
            if (setting.contains(ApplicationData.PREFERENCE_FILENAME))
            {
                // Intent myIntent = new Intent(SplashActivity.this, SplashActivity.class);
                // startActivity(myIntent);

                String m = setting.getString(ApplicationData.PREFERENCE_FILENAME,"");
                Toast.makeText(getBaseContext(), m, Toast.LENGTH_LONG).show();


            }
            else
            {
                Toast.makeText(getBaseContext(),"hello word",Toast.LENGTH_LONG).show();
            }
        }

    }
