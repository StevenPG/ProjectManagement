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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

    // Retrieve list of all members
    ArrayList<String> memberList = null;
    ListView projectView = null;

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

        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu20);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        checkTheme();

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }
        // Retrieve all app members
        memberList = getAllMembers();

        ArrayList<String> memberListNoUnicodeChars = new ArrayList<>();

        if(memberList == null) {
            memberList = new ArrayList<>();
        } else {
            for(String member : memberList){
                memberListNoUnicodeChars.add(member.substring(3, member.length()-1));
            }
        }

        // Retrieve listview and add projects
        projectView = (ListView) findViewById(R.id.addMemberListView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.mainactivityrow, memberListNoUnicodeChars);
        projectView.setAdapter(listAdapter);

        final ImageButton myButton = (ImageButton) findViewById(R.id.plus_buttom1);
        projectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedEmail = (String) projectView.getItemAtPosition(position);

                // Build paramString
                StringBuilder paramBuilder = new StringBuilder();
                paramBuilder.append("?");
                paramBuilder.append("projectid=");
                paramBuilder.append(ApplicationData.currentProject.getProjectId());
                paramBuilder.append("&");
                paramBuilder.append("addeduser=");
                paramBuilder.append(selectedEmail);

                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("message/rfc822");
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{selectedEmail});
                email.putExtra(Intent.EXTRA_SUBJECT, "Please join my group");
                email.putExtra(Intent.EXTRA_TEXT, ApplicationData.FULL_URL + "/accept" + paramBuilder.toString());
                try {
                    startActivity(Intent.createChooser(email, "Please select an email client to send invitation..."));
                }catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AddMemberstoProject.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
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
            return httpHandler.getAll("");
        } catch (ServerNotRunningException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void checkTheme()
    {
        //bigImg = (ImageView) findViewById(R.id.big_logo);
        ImageView miniLogo = (ImageView) findViewById(R.id.mini_logo);
        LinearLayout colorBar = (LinearLayout) findViewById(R.id.color_bar);

        if(!ApplicationData.theme.equals("default"))
        {
            switch(ApplicationData.theme)
            {
                case "Banana":
                        colorBar.setBackgroundResource(R.color.color_banana);
                        miniLogo.setImageResource(R.drawable.banner_logo_banana);
                        //bigImg.setImageResource(R.drawable.banner_logo_banana);
                        ApplicationData.theme = "Banana";
                        break;
                    case "Peach":
                        colorBar.setBackgroundResource(R.color.color_peach);
                        miniLogo.setImageResource(R.drawable.banner_logo_peach);
                        //bigImg.setImageResource(R.drawable.banner_logo_peach);
                        ApplicationData.theme = "Peach";
                        break;
                    case "Strawberry":
                        colorBar.setBackgroundResource(R.color.color_strawberry);
                        miniLogo.setImageResource(R.drawable.banner_logo_strawberry);
                        //bigImg.setImageResource(R.drawable.banner_logo_strawberry);
                        ApplicationData.theme = "Strawberry";
                        break;
                    case "Mellon":
                        colorBar.setBackgroundResource(R.color.color_mellon);
                        miniLogo.setImageResource(R.drawable.banner_logo_mellon);
                        //bigImg.setImageResource(R.drawable.banner_logo_mellon);
                        ApplicationData.theme = "Mellon";
                        break;
                    default:
                        colorBar.setBackgroundResource(R.color.color_default);
                        miniLogo.setImageResource(R.drawable.banner_logo);
                        //bigImg.setImageResource(R.drawable.banner_logo);
                        ApplicationData.theme = "Default";
                        break;
            }
        }
    }
}
