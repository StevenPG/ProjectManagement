package com.kutztown.projectmanagement.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;

public class InboxActivity extends AppCompatActivity {

    /**
     * List of projects
     */
    ArrayList<String> memberList = null;
    ListView ListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(null);
        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu09);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if (!loggedIn) {
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }

        // Retrieve projects of current user
        // TODO: getMembersThatHaveMessagedYou()
        this.memberList = getMembersFromProject();

        if (this.memberList == null) {
            this.memberList = new ArrayList<>();
        }

        // Retrieve listview and add projects
        ListView = (ListView) findViewById(R.id.inboxListView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, this.memberList);

        ListView.setAdapter(listAdapter);

        final Context thisContext = this;

        // On a regular click, show the user's task progress
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user = (String) ListView.getItemAtPosition(position);
                Log.d("debug", "Clicked on " + user);
                ApplicationData.currentViewedMember = user;
                startActivity(ActivityController.openActivityMessage(getApplicationContext()));
            }
        });
    }

    /**
     * Retrieve the projects from the logged in user and parse them into an arraylist
     *
     * @return null if no projects, else arraylist of projects
     */
    protected ArrayList<String> getMembersFromProject() {

        String projectList = ApplicationData.currentProject.getMemberList();
        // refresh project
        if (projectList == null) {
            projectList = "";
        }

        ArrayList memberArray = new ArrayList();

        TextView header = (TextView) findViewById(R.id.header);
        Log.d("debug", "Member List: " + projectList);
        if ("None".equals(projectList) || "u''".equals(projectList)) {
            header.setText("There are no members in this project");
            return null;
        } else {
            header.setText("Members");
            String[] members = projectList.split("--");

            // Wipe python leftover from splitting list object
            // Remove first 2 characters from very first item
            members[0] = members[0].substring(2, members[0].length());
            // Remove very last char from last item
            members[members.length - 1] = members[members.length - 1].substring(0, members[members.length - 1].length() - 1);

            for (String member : members) {
                member = member.replace("\\s+", "");
                if(!"".equals(member)) {
                    memberArray.add(member);
                }
            }
        }

        return memberArray;
    }
}
