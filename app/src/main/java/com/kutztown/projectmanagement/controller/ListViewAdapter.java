package com.kutztown.projectmanagement.controller;

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

public class ListViewAdapter extends BaseAdapter{

        Activity context;
        String dates1[];
        String tasks1[];

        public ListViewAdapter(Activity context, String[]dates1, String[] tasks1) {
            super();
            this.context = context;
            this.dates1 = dates1;
            this.tasks1 = tasks1;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return dates1.length;
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
                convertView = inflater.inflate(R.layout.calendar,null);
                holder = new ViewHolder();
                holder.dates = (TextView) convertView.findViewById(R.id.date);
                holder.tasks = (TextView) convertView.findViewById(R.id.task);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.dates.setText(dates1[position]);
            holder.tasks.setText(tasks1[position]);

            return convertView;
        }


}
