package com.kutztown.projectmanagement.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.data.ApplicationData;

import java.util.Calendar;

public class CreateTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
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

        final Spinner spin01 = (Spinner) findViewById(R.id.spinner01);
        final TextView text_sel01 = (TextView)spin01.getSelectedView();

        final Spinner spin02 = (Spinner) findViewById(R.id.spinner02);
        final TextView text_sel02 = (TextView)spin02.getSelectedView();


        final Spinner spin = (Spinner) findViewById(R.id.spinner03);
        TextView text_sel = (TextView)spin.getSelectedView();
        String pickedDependency = text_sel.getText().toString();


        TextView taskName = (TextView) findViewById(R.id.name_task);

        // this textview will be set whith the project name
        TextView projectName = (TextView) findViewById(R.id.name_project);
        projectName.setText("");

        // this text view will ve set with the description of the project
        TextView projectDesc = (TextView) findViewById(R.id.project_description);
        projectDesc.setText("");

        Button createTask = (Button) findViewById(R.id.create_task);
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String pickedMember = text_sel01.getText().toString()
                //String pickedPriority = text_sel02.getText().toString();
                //String pickedDependency = text_sel.getText().toString();

                //Log.d("debug", pickedMember);
                //Log.d("debug", pickedPriority);
                //Log.d("debug", pickedDependency);
            }
        });

        final DatePicker taskDate = (DatePicker)findViewById(R.id.datepicker01);
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        taskDate.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,monthOfYear, dayOfMonth);
                // here will go code to get the date due of the task and passed that info to the
                // database
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

}
