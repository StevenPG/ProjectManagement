package com.kutztown.projectmanagement.activity;

import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.ProjectTableEntry;
import com.kutztown.projectmanagement.network.HTTPHandler;

public class CreateProject extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }

        //NOTE: here puh completed project into the database

        // Retrieve data from fields
        final EditText projectNameText = (EditText) findViewById(R.id.project_name);
        final EditText projectDescText = (EditText) findViewById(R.id.projectDescField);


        // Add onclick to button
        Button projectB = (Button) findViewById(R.id.createProject);
        projectB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieve values
                String projectName = projectNameText.getText().toString();
                String projectDesc = projectDescText.getText().toString();

                // print values for debugging
                Log.d("debug", "ProjectName: " + projectName);
                Log.d("debug", "Project Desc:" + projectDesc);

                // Enter project into database
                HTTPHandler handler = new HTTPHandler();
                ProjectTableEntry entry = new ProjectTableEntry(
                        ApplicationData.currentUser.getEmail(),
                        projectName,
                        projectDesc);

                // TODO NEED TO ADD PROJECT TO CURRENT USER'S PROJECTLIST

                // Assign entry as the current project
                ApplicationData.currentProject = entry;

                try {
                    handler.insert(entry, "ProjectTable");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("debug", "Error inserting new project into current user's table");
                }

                startActivity(ActivityController.openLeaderViewActivity(getApplicationContext()));
            }
        });


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return ApplicationData.contextMenu(this, item);
}

}
