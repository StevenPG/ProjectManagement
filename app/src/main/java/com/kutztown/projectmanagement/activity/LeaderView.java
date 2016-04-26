package com.kutztown.projectmanagement.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;

import org.w3c.dom.Text;

public class LeaderView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(null);

        ApplicationData.amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu03);
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

        // Set the correct name on project
        TextView header = (TextView) findViewById(R.id.leader_prj_label);
        String projectName = ApplicationData.currentProject.getProjectName();
        projectName = projectName.substring(2, ApplicationData.currentProject.getProjectName().length()-1);
        projectName = projectName.replaceAll("_"," ");
        header.setText(projectName);

        final Button progressB = (Button)findViewById(R.id.progress_ld_button);
        final Button taskB = (Button)findViewById(R.id.task_ld_button);
        final Button calendarB = (Button) findViewById(R.id.calendar_id_button);
        final Button membersB = (Button) findViewById(R.id.members_ld_button);
        final Button messageB = (Button) findViewById(R.id.message_ld_button);
        final Button descriptionB = (Button) findViewById(R.id.description_ld_button);

        progressB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityController.openProgressActivity(getApplicationContext()));
            }
        });

        taskB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityController.openTaskActivity(getApplicationContext()));
            }
        });

        calendarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityController.openCalendarActivity(getApplicationContext()));
            }
        });

        membersB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityController.openMemberList(getApplicationContext()));
            }
        });

        messageB.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(ActivityController.openActivityInbox(getApplicationContext()));
           }
        });

        descriptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityController.openProjectDescriptionActivity(getApplicationContext()));
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
    protected void onResume(){
        super.onResume();
        boolean loggedIn = ApplicationData.checkIfLoggedIn(getApplicationContext());
        if(!loggedIn){
            startActivity(ActivityController.openLoginActivity(getApplicationContext()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return ApplicationData.contextMenu(this, item);
    }

    public void checkTheme()
    {
        //bigImg = (ImageView) findViewById(R.id.big_logo);
        ImageView miniLogo = (ImageView) findViewById(R.id.mini_logo);
        LinearLayout colorBar = (LinearLayout) findViewById(R.id.color_bar);
        Button progressB = (Button)findViewById(R.id.progress_ld_button);
        Button taskB = (Button)findViewById(R.id.task_ld_button);
        Button calendarB = (Button) findViewById(R.id.calendar_id_button);
        Button membersB = (Button) findViewById(R.id.members_ld_button);
        Button messageB = (Button) findViewById(R.id.message_ld_button);
        Button descriptionB = (Button) findViewById(R.id.description_ld_button);

        if(!ApplicationData.theme.equals("default"))
        {
            switch(ApplicationData.theme)
            {
                case "Banana":
                        colorBar.setBackgroundResource(R.color.color_banana);
                        miniLogo.setImageResource(R.drawable.banner_logo_banana);
                        progressB.setBackgroundResource(R.color.color_banana);
                        taskB.setBackgroundResource(R.color.color_banana);
                        calendarB.setBackgroundResource(R.color.color_banana);
                        membersB.setBackgroundResource(R.color.color_banana);
                        messageB.setBackgroundResource(R.color.color_banana);
                        descriptionB.setBackgroundResource(R.color.color_banana);
                        //bigImg.setImageResource(R.drawable.banner_logo_banana);
                        ApplicationData.theme = "Banana";
                        break;
                    case "Peach":
                        colorBar.setBackgroundResource(R.color.color_peach);
                        miniLogo.setImageResource(R.drawable.banner_logo_peach);
                        progressB.setBackgroundResource(R.color.color_peach);
                        taskB.setBackgroundResource(R.color.color_peach);
                        calendarB.setBackgroundResource(R.color.color_peach);
                        membersB.setBackgroundResource(R.color.color_peach);
                        messageB.setBackgroundResource(R.color.color_peach);
                        descriptionB.setBackgroundResource(R.color.color_peach);
                        //bigImg.setImageResource(R.drawable.banner_logo_peach);
                        ApplicationData.theme = "Peach";
                        break;
                    case "Strawberry":
                        colorBar.setBackgroundResource(R.color.color_strawberry);
                        miniLogo.setImageResource(R.drawable.banner_logo_strawberry);
                        progressB.setBackgroundResource(R.color.color_strawberry);
                        taskB.setBackgroundResource(R.color.color_strawberry);
                        calendarB.setBackgroundResource(R.color.color_strawberry);
                        membersB.setBackgroundResource(R.color.color_strawberry);
                        messageB.setBackgroundResource(R.color.color_strawberry);
                        descriptionB.setBackgroundResource(R.color.color_strawberry);
                        //bigImg.setImageResource(R.drawable.banner_logo_strawberry);
                        ApplicationData.theme = "Strawberry";
                        break;
                    case "Mellon":
                        colorBar.setBackgroundResource(R.color.color_mellon);
                        miniLogo.setImageResource(R.drawable.banner_logo_mellon);
                        progressB.setBackgroundResource(R.color.color_mellon);
                        taskB.setBackgroundResource(R.color.color_mellon);
                        calendarB.setBackgroundResource(R.color.color_mellon);
                        membersB.setBackgroundResource(R.color.color_mellon);
                        messageB.setBackgroundResource(R.color.color_mellon);
                        descriptionB.setBackgroundResource(R.color.color_mellon);
                        //bigImg.setImageResource(R.drawable.banner_logo_mellon);
                        ApplicationData.theme = "Mellon";
                        break;
                    default:
                        colorBar.setBackgroundResource(R.color.color_default);
                        miniLogo.setImageResource(R.drawable.banner_logo);
                        progressB.setBackgroundResource(R.color.color_default);
                        taskB.setBackgroundResource(R.color.color_default);
                        calendarB.setBackgroundResource(R.color.color_default);
                        membersB.setBackgroundResource(R.color.color_default);
                        messageB.setBackgroundResource(R.color.color_default);
                        descriptionB.setBackgroundResource(R.color.color_default);
                        //bigImg.setImageResource(R.drawable.banner_logo);
                        ApplicationData.theme = "Default";
                        break;
            }
        }
    }
}
