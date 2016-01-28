package com.kutztown.projectmanagement.activity;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.kutztown.project.projectmanagement.R;

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
}
