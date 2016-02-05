package com.kutztown.projectmanagement.com.kutztown.projectmanagement.networking;

import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Steven Gantz on 2/4/2016.
 * @date 2/4/2016
 * This class abstracts away the annoying
 * aspects of making the HTTP calls.
 */
public class HTTPHandler {

    //TODO - THIS IS A DEBUG FOR NOW WHILE THIS COMMENT EXISTS
    public HTTPHandler(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("http://104.238.131.94:5000/databasetest");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                InputStream in = null;
                try {
                    in = new BufferedInputStream(urlConnection.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //convert stream to string
                StringWriter writer = new StringWriter();
                try {
                    IOUtils.copy(in,writer,"UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String theString = writer.toString();

                urlConnection.disconnect();

                Log.d("debug",theString);
            }
        });
        t.start();
    }
}
