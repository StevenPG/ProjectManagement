package com.kutztown.projectmanagement.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

public class MemberList extends Activity implements AppCompatCallback {

    /**
     * List of projects
     */
    ArrayList<String> memberList = null;
    ListView projectView = null;

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationData.delegate = AppCompatDelegate.create(this, this);
        //the installViewFactory method replaces the default widgets
        //with the AppCompat-tinted versions
        ApplicationData.delegate.installViewFactory();
        super.onCreate(savedInstanceState);
        ApplicationData.delegate.onCreate(savedInstanceState);
        ApplicationData.delegate.setContentView(R.layout.activity_member_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ApplicationData.delegate.setSupportActionBar(toolbar);
        ApplicationData.delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ApplicationData.delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);
        ApplicationData.delegate.getSupportActionBar().setDisplayShowTitleEnabled(false);

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }
        // Retrieve projects of current user
        this.memberList = getMembersFromProject();

        if(this.memberList == null){
            this.memberList = new ArrayList<>();
        }

        // Retrieve listview and add projects
        projectView = (ListView) findViewById(R.id.memberListView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, this.memberList);
        /**projectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // this is not in the requirements: TODO Don't do anything when a member is clicked
                return;


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
        });**/

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
    protected ArrayList<String> getMembersFromProject(){

        String projectList = ApplicationData.currentProject.getMemberList();
        if(projectList == null){
            projectList = "";
        }

        ArrayList memberArray = new ArrayList();

        TextView header = (TextView) findViewById(R.id.header);
        Log.d("debug", "Member List: " + projectList);
        if("None".equals(projectList)){
            header.setText("There are no members in this project");
            return null;
        }
        else{
            header.setText("Members");
            String[] members = projectList.split("--");

            for(String member : members){
                // Wipe leftover python structures
                member = member.replaceAll("u'", "");
                member = member.replaceAll("'", "");
                memberArray.add(member);
            }
        }

        return memberArray;
    }

}
