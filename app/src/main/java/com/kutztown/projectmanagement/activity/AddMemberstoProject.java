package com.kutztown.projectmanagement.activity;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.UserTableEntry;
import com.kutztown.projectmanagement.exception.ServerNotRunningException;
import com.kutztown.projectmanagement.network.HTTPHandler;

import java.util.ArrayList;

public class AddMemberstoProject extends Activity implements AppCompatCallback {
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
        super.onCreate(savedInstanceState);
        ApplicationData.delegate.installViewFactory();
        ApplicationData.delegate.onCreate(savedInstanceState);
        ApplicationData.delegate.setContentView(R.layout.activity_add_membersto_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ApplicationData.delegate.setSupportActionBar(toolbar);

        ApplicationData.delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ApplicationData.delegate.getSupportActionBar().setDisplayShowHomeEnabled(false);
        ApplicationData.delegate.getSupportActionBar().setTitle(null);

        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });


        // Retrieve list of all members
        ArrayList<String> memberList = null;
        ListView projectView = null;

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }
        // Retrieve all app members
        memberList = getAllMembers();

        if(memberList == null) {
            memberList = new ArrayList<>();
        }

        // Retrieve listview and add projects
        projectView = (ListView) findViewById(R.id.addMemberListView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, memberList);
        projectView.setAdapter(listAdapter);

        final ImageButton myButton = (ImageButton) findViewById(R.id.plus_buttom1);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu, ApplicationData.amvMenu.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return ApplicationData.contextMenu(this, item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }
    }

    /**
     * Retrieve the projects from the logged in user and parse them into an arraylist
     * @return null if no projects, else arraylist of projects
     */
    protected ArrayList<String> getAllMembers() {
        HTTPHandler httpHandler = new HTTPHandler();
        try {
            return httpHandler.getAll();
        } catch (ServerNotRunningException e) {
            e.printStackTrace();
        }

        return null;
    }
}
