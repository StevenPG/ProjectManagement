package com.kutztown.projectmanagement.activity;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import com.kutztown.project.projectmanagement.R;

public class MemberList extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CursorLoader loader = new CursorLoader(this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,null);
              Cursor Contacts = loader.loadInBackground();
        // geting the data from the contact list for now. it will be replace with a call to the database
        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.name, Contacts, new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                new int[]{ R.id.nameD},0);
        setListAdapter(adapter);


    }

}
