package com.kutztown.projectmanagement.controller;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * Created by Hector on 4/15/16.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kutztown.project.projectmanagement.R;
import com.kutztown.projectmanagement.activity.CalendarActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class ListViewAdapter extends BaseAdapter{

       private CalendarActivity context;
       private ArrayList<String> dates1 = null;
        private ArrayList<String> tasks1 = null;


        public ListViewAdapter(CalendarActivity context,ArrayList<String> dates1, ArrayList<String> tasks1) {
            super();
            this.dates1 = new ArrayList<>();
            this.tasks1 = new ArrayList<>();
            this.context = context;
            this.dates1 = dates1;
            this.tasks1 = tasks1;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return tasks1.size();
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

        public View getView(int position, View convertView, ViewGroup parent)
        {
            // TODO Auto-generated method stub
            ViewHolder holder;
            LayoutInflater inflater =  context.getLayoutInflater();

            if (convertView == null)
            {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.calendar,null);
                holder.dates = (TextView) convertView.findViewById(R.id.dates1);
                holder.tasks = (TextView) convertView.findViewById(R.id.tasks);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.tasks.setText(tasks1.get(position));
            holder.dates.setText(dates1.get(position));

            return convertView;
        }



}
