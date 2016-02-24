package com.kutztown.projectmanagement.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.ProjectTableEntry;
import com.kutztown.projectmanagement.network.HTTPHandler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * List of projects
     */
    ArrayList<String> projectList = null;
    ListView projectView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }

        // Retrieve projects of current user
        this.projectList = getProjectsFromUser();

        if(this.projectList == null){
            this.projectList = new ArrayList<>();
        }

        // Retrieve listview and add projects
        projectView = (ListView) findViewById(R.id.ProjectListView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, this.projectList);
        projectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Clicked Text contains the project name
                String clickedText = (String) parent.getItemAtPosition(position);
                // TODO - Set the ApplicationData.currentProject value
                // Retrieve the project from the DB and store it globally
                HTTPHandler handler = new HTTPHandler();
                try {
                    ApplicationData.currentProject = (ProjectTableEntry) handler.
                            select(clickedText, "ProjectName", new ProjectTableEntry(), "ProjectTable");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("debug", ApplicationData.currentProject.writeAsGet());

                // Get info and decide where to send the user if leader or not
                String projectLeader = ApplicationData.currentProject.getLeaderList();
                if(projectLeader.equals(ApplicationData.currentUser.getEmail())){
                    startActivity(ActivityController.openLeaderViewActivity(getApplicationContext()));
                } else {
                    startActivity(ActivityController.openMemberViewActivity(getApplicationContext()));
                }
            }
        });

        projectView.setAdapter(listAdapter);
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
        int id = item.getItemId();
        onBackPressed();
        return true;
    }

    /**
     * Retrieve the projects from the logged in user and parse them into an arraylist
     * @return null if no projects, else arraylist of projects
     */
    protected ArrayList<String> getProjectsFromUser(){

        String projectList = ApplicationData.currentUser.getProjectList();
        if(projectList == null){
            projectList = "";
        }

        ArrayList projectArray = new ArrayList();

        TextView header = (TextView) findViewById(R.id.Header);
        Log.d("debug", "Project List: " + projectList);
        if("None".equals(projectList)){
            header.setText("You are not a part of any projects.");
            return null;
        }
        else{
            header.setText("Projects");
            String[] projects = projectList.split("--");

            for(String project : projects){
                // Wipe leftover python structures
                project = project.replaceAll("u'", "");
                project = project.replaceAll("'", "");
                projectArray.add(project);
            }
        }

        return projectArray;
    }

}
