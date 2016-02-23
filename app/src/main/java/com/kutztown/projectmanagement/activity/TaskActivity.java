package com.kutztown.projectmanagement.activity;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.ProjectTableEntry;
import com.kutztown.projectmanagement.data.TaskTableEntry;
import com.kutztown.projectmanagement.data.UserTableEntry;
import com.kutztown.projectmanagement.exception.ServerNotRunningException;
import com.kutztown.projectmanagement.network.HTTPHandler;

public class TaskActivity extends ListActivity implements AppCompatCallback{

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
        ApplicationData.delegate.setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ApplicationData.delegate.setSupportActionBar(toolbar);
        ApplicationData.delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ApplicationData.delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);
        ApplicationData.delegate.getSupportActionBar().setDisplayShowTitleEnabled(false);


        CursorLoader loader = new CursorLoader(this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,null);
        Cursor Contacts = loader.loadInBackground();
        // geting the data from the contact list for now. it will be replace with a call to the database
        ListAdapter task_adapter = new SimpleCursorAdapter(this, R.layout.task, Contacts, new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                new int[]{ R.id.task},0);

        try {
            HTTPHandler httpHandler = new HTTPHandler();
            //ApplicationData.ProjectName = (ProjectTableEntry) httpHandler.select(this.mEmail
                    //, "email", new UserTableEntry(), "UserTable");

            //ApplicationData.currentProject = (ProjectTableEntry) httpHandler.select(String searchValue,
            //String searchRecord, TableEntry entry, String table);

            TaskTableEntry currentTask = (TaskTableEntry) httpHandler.select(String user, String project, String taskName, String taskDesc,
                    String taskPriority, String taskDueDate, String taskDep);

            Log.d("debug", ApplicationData.currentUser.writeAsGet());
        } catch (ServerNotRunningException e) {
            e.printStackTrace();
            Log.d("debug", "Server isn't running");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("debug", "User wasn't found");
        }

        //HelloWorld

        setListAdapter(task_adapter);
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

}
