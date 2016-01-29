package com.kutztown.projectmanagement.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The splash screen will stay in place for a few
 * seconds before moving on to the rest of the
 * application.
 *
 * This activity is removed from the activity stack
 * as soon as it leaves, allowing the user to back
 * out of the next activity directly to the previous
 * application or instance.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Set parameters to be fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create content view
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart(){
        super.onStart();

        // TODO - Maybe add a progressbar down the line

        // Set the current date
        TextView date = (TextView) findViewById(R.id.date);

        // Code Source:
        //  http://stackoverflow.com/questions/8654990/how-can-i-get-current-date-in-android

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MMM/dd/yyyy");
        String formattedDate = df.format(c.getTime());

        // - end

        date.setText(formattedDate);

        // Once activity starts, stop the UIThread for loading purposes
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            // Just move into next activity if waiting fails
        }

        // Start next activity
    }
}
