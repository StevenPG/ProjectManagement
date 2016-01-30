package com.kutztown.projectmanagement.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.graphing.GraphingTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    protected void onStart() {
        super.onStart();

        // Set the current date
        TextView date = (TextView) findViewById(R.id.date);

        // Code Source:
        //  http://stackoverflow.com/questions/8654990/how-can-i-get-current-date-in-android

        Calendar c = Calendar.getInstance();
        // This just removes an error since we are using the device's timezone (supposed to specify)
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("MMM/dd/yyyy");
        String formattedDate = df.format(c.getTime());

        // - end

        date.setText(formattedDate);

        // Once activity starts, stop the UIThread for loading purposes
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent intent = new Intent(SplashActivity.this,
                //        GraphingTest.class);
                //startActivity(intent);

                startActivity(ActivityController.openGraphingTest(getApplicationContext()));

                // Close activity
                finish();
            }
        }, ApplicationData.SPLASH_TIME_OUT);
    }
}
