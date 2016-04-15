package com.kutztown.projectmanagement.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.UserTableEntry;
import com.kutztown.projectmanagement.network.HTTPHandler;

public class ProfileActivity extends AppCompatActivity {
     private  String biography = "";
     private String SName = "";
     private   String email = "";
     private String SPhone = "";
     private   String SPosition = "";
     private String lastN ="";
     private String passw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activitty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(null);
        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu07);
        ApplicationData.amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);

            }
        });

        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }
        final ImageView myImage = (ImageView) findViewById(R.id.pic);
        final EditText name = (EditText) findViewById(R.id.profile_name);
        final TextView user_id= (TextView) findViewById(R.id.user_email);
        final EditText user_position = (EditText) findViewById(R.id.user_position);
        final EditText user_bio = (EditText) findViewById(R.id.biography);
        final Button clearButton = (Button) findViewById(R.id.clear);
        final Button updateP = (Button)findViewById(R.id.update_profile);
        final EditText lastName = (EditText) findViewById(R.id.profile_nameL);
        UserTableEntry selectedUser = new UserTableEntry();

       /* name.setText(ApplicationData.currentUser.getFirstName().substring(2, ApplicationData.currentUser.getFirstName().length() - 1));
        lastName.setText(ApplicationData.currentUser.getLastName().substring(2, ApplicationData.currentUser.getLastName().length() - 1));
        user_id.setText(ApplicationData.currentUser.getEmail().substring(2, ApplicationData.currentUser.getEmail().length() - 1));
       // user_position.setText(ApplicationData.currentUser.get);
        user_bio.setText(ApplicationData.currentUser.getBio().substring(2, ApplicationData.currentUser.getLastName().length() - 1));
        passw = ApplicationData.currentUser.getPassword().substring(2, ApplicationData.currentUser.getPassword().length() - 1);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                lastName.setText("");
                user_position.setText("");
                user_bio.setText("");
            }
        });

        updateP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                biography = user_bio.getText().toString();
                SName = name.getText().toString();
                email = user_id.getText().toString();
                SPosition = user_position.getText().toString();
                lastN = lastName.getText().toString();
                if (biography.equals("") || SName.equals("") || SPosition.equals("") || lastN.equals("")) {
                    MissingProfileInfo();
                } else {


                    HTTPHandler handler = new HTTPHandler();
                    try {
                        //return "firstname,lastname,email,password,bio,projectlist,picture";
                      //  handler.update("firstname=\"John\"%20where%20email=\"emailaddress@.kutztown.edu\"", "usertable");

                       handler.update("firstname=\"" + SName + "\"lastname=\"" +lastN+
                                "\"email=\""+ email + "\"password=\"" + passw + "\"bio=\"" + biography+
                                "\"projectList=\""+ "one" + "\"picture=\"" + "two" + "\"%20where%20email=\""+ email +"\"", "usertable");
                        Toast.makeText(getApplicationContext(), "User profile Update", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("debug", "Error Updating user profile ");
                        Toast.makeText(getApplicationContext(), "Error updating user profile", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }); */

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
     * Does not let the user progress if information is missing from fields. Generates a dialog
     * to alert the user that this issue has occurred.
     */
    private void MissingProfileInfo() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Missing update profile information");

        // Setting Dialog Message
        alertDialog.setMessage("Please fill all fields before updating your profile!");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.icon);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


}
