package com.kutztown.projectmanagement.activity;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;

public class AddMemberstoProject extends ListActivity implements AppCompatCallback {
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
        ApplicationData.delegate.getSupportActionBar().setDisplayShowTitleEnabled(false);

        CursorLoader loader = new CursorLoader(this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,null);
        Cursor Contacts = loader.loadInBackground();
        // geting the data from the contact list for now. it will be replace with a call to the database
        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.members_to_add, Contacts,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                new int[]{ R.id.member_name},0);
        setListAdapter(adapter);

        Intent theInten = getIntent();
        String message = theInten.getStringExtra(ApplicationData.ProjectName);
        TextView theProjectName = (TextView) findViewById(R.id.project_display);
         theProjectName.setText(message);
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
        getMenuInflater().inflate(R.menu.plus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


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
}
