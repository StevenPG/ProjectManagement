package com.kutztown.projectmanagement.com.kutztown.projectmanagement.networking;

import android.util.Log;

import com.kutztown.projectmanagement.data.ApplicationData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Steven Gantz on 2/5/2016.
 * @date 2/5/2016
 * @file This file is a custom thread that allows passing in
 * of multiple types of variables to constructors.
 */
public class PingHttpThread implements Runnable {

    /**
     * Flag whether or not an operation was successful
     */
    public volatile boolean success;

    /**
     * General constructor
     * Creates a boolean set to true. This boolean is changed
     * if there is a failure to ping an external server.
     */
    public PingHttpThread(){
        this.success = true;
    }

    public void run(){
        try {
            // Build URL for ping
            URL url = HTTPHandler.buildURL(ApplicationData.SERVER_IP, ApplicationData.SERVER_PORT, "", false);

            // Generate the connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Exception will be thrown just before this line. Otherwise close the connection.
            urlConnection.disconnect();

        } catch (MalformedURLException m) {
            Log.v("Error", "Some exception was thrown while building the URL");
            this.success = false;
        } catch (IOException e) {
            Log.v("Error", "The server is not up and running, attempting to make a connection has failed.");
            this.success = false;
        }
    }

    public boolean getSuccess(){
        return this.success;
    }
}