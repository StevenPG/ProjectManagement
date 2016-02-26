package com.kutztown.projectmanagement.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;

import org.w3c.dom.Text;

public class LeaderView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }

        TextView header = (TextView) findViewById(R.id.leader_prj_label);
        header.setText(ApplicationData.currentProject.getProjectName());

        final Button progressB = (Button)findViewById(R.id.progress_ld_button);
        final Button taskB = (Button)findViewById(R.id.task_ld_button);
        final Button calendarB = (Button) findViewById(R.id.calendar_id_button);
        final Button membersB = (Button) findViewById(R.id.members_ld_button);

        progressB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityController.openProgressActivity(getApplicationContext()));
            }
        });

        taskB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityController.openTaskActivity(getApplicationContext()));
            }
        });

        calendarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityController.openCalendarActivity(getApplicationContext()));
            }
        });

        membersB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityController.openMemberList(getApplicationContext()));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.tutorial:
                Log.d("debug", "Selected Tutorial");
                return true;
            case R.id.logout:
                Log.d("debug", "Selected Logout");
                ApplicationData.logoutUser();
                startActivity(ActivityController.
                        openLoginActivity(getApplicationContext()));
                return true;
            case R.id.profile:
                Log.d("debug", "Selected Profile");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
