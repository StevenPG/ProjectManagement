package com.kutztown.projectmanagement.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import android.widget.TextView;
import android.content.Context;


import com.kutztown.project.projectmanagement.R;

import java.util.ArrayList;
import java.util.Calendar;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.controller.ActivityController;
import com.kutztown.projectmanagement.data.ApplicationData;


public class Reminder extends AppCompatActivity {
   private ArrayList<String> dueTask = null;
    private ArrayList<String> datesTask = null;
    private ListViewAdapter1 myNewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
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

        Intent d = getIntent();
        datesTask = new ArrayList<>();
        dueTask = new ArrayList<>();
        dueTask = d.getStringArrayListExtra("Dates");
        datesTask = d.getStringArrayListExtra("Tasks");
        ListView first = (ListView) findViewById(R.id.list_date);


      //  ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.right_message, this.dueTask);
       // ArrayAdapter<String> listAdapter1 = new ArrayAdapter<String>(this, R.layout.right_message, this.datesTask);

      //  first.setAdapter(listAdapter);
       // second.setAdapter(listAdapter1);
       myNewAdapter = new ListViewAdapter1(this,datesTask,dueTask);
        first.setAdapter(myNewAdapter);


    }


   private class ListViewAdapter1 extends BaseAdapter {

        private Activity context;
        private ArrayList<String> dates12 = null;
        private ArrayList<String> tasks12 = null;
       
        private ListViewAdapter1(Activity context, ArrayList<String> dates1, ArrayList<String> tasks1) {
            super();
            this.dates12 = new ArrayList<>();
            this.tasks12 = new ArrayList<>();
            this.context = context;
            this.dates12 = dates1;
            this.tasks12 = tasks1;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return tasks12.size();
        }


        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        private class ViewHolder {
            TextView dates;
            TextView tasks;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            LayoutInflater inflater = context.getLayoutInflater();

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.reminder, null);
                holder.dates = (TextView) convertView.findViewById(R.id.reminder1);
                holder.tasks = (TextView) convertView.findViewById(R.id.reminder2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.tasks.setText(tasks12.get(position));
            holder.dates.setText(dates12.get(position));

            return convertView;
        }

    }

    public void checkTheme()
    {
        //bigImg = (ImageView) findViewById(R.id.big_logo);
        ImageView miniLogo = (ImageView) findViewById(R.id.mini_logo);
        LinearLayout colorBar = (LinearLayout) findViewById(R.id.color_bar);
        TextView labelOne = (TextView) findViewById(R.id.label_1);
        TextView labelTwo = (TextView) findViewById(R.id.label_2);

        if(!ApplicationData.theme.equals("default"))
        {
            switch(ApplicationData.theme)
            {
                case "Banana":
                        colorBar.setBackgroundResource(R.color.color_banana);
                        miniLogo.setImageResource(R.drawable.banner_logo_banana);
                        //bigImg.setImageResource(R.drawable.banner_logo_banana);
                        labelOne.setTextColor(ContextCompat.getColor(this, R.color.color_banana));
                        labelTwo.setTextColor(ContextCompat.getColor(this,R.color.color_banana));
                        ApplicationData.theme = "Banana";
                        break;
                    case "Peach":
                        colorBar.setBackgroundResource(R.color.color_peach);
                        miniLogo.setImageResource(R.drawable.banner_logo_peach);
                        //bigImg.setImageResource(R.drawable.banner_logo_peach);
                        labelOne.setTextColor(ContextCompat.getColor(this, R.color.color_peach));
                        labelTwo.setTextColor(ContextCompat.getColor(this, R.color.color_peach));
                        ApplicationData.theme = "Peach";
                        break;
                    case "Strawberry":
                        colorBar.setBackgroundResource(R.color.color_strawberry);
                        miniLogo.setImageResource(R.drawable.banner_logo_strawberry);
                        //bigImg.setImageResource(R.drawable.banner_logo_strawberry);
                        labelOne.setTextColor(ContextCompat.getColor(this, R.color.color_strawberry));
                        labelTwo.setTextColor(ContextCompat.getColor(this, R.color.color_strawberry));
                        ApplicationData.theme = "Strawberry";
                        break;
                    case "Mellon":
                        colorBar.setBackgroundResource(R.color.color_mellon);
                        miniLogo.setImageResource(R.drawable.banner_logo_mellon);
                        //bigImg.setImageResource(R.drawable.banner_logo_mellon);
                        labelOne.setTextColor(ContextCompat.getColor(this, R.color.color_mellon));
                        labelTwo.setTextColor(ContextCompat.getColor(this, R.color.color_mellon));
                        ApplicationData.theme = "Mellon";
                        break;
                    default:
                        colorBar.setBackgroundResource(R.color.color_default);
                        miniLogo.setImageResource(R.drawable.banner_logo);
                        //bigImg.setImageResource(R.drawable.banner_logo);
                        labelOne.setTextColor(ContextCompat.getColor(this, R.color.color_default));
                        labelTwo.setTextColor(ContextCompat.getColor(this, R.color.color_default));
                        ApplicationData.theme = "Default";
                        break;
            }
        }
    }
}
