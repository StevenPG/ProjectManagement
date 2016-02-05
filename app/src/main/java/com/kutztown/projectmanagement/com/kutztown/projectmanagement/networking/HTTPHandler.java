package com.kutztown.projectmanagement.com.kutztown.projectmanagement.networking;

import android.util.Log;

import com.kutztown.projectmanagement.data.ApplicationData;

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
                    //e.printStackTrace();
                    return;
                }
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    //e.printStackTrace();
                    return;
                }

                InputStream in = null;
                try {
                    in = new BufferedInputStream(urlConnection.getInputStream());
                } catch (IOException e) {
                    //e.printStackTrace();
                    return;
                }

                //convert stream to string
                StringWriter writer = new StringWriter();
                try {
                    IOUtils.copy(in,writer,"UTF-8");
                } catch (IOException e) {
                    //e.printStackTrace();
                    return;
                }
                String theString = writer.toString();

                urlConnection.disconnect();

                Log.d("debug",theString);
            }
        });
        t.start();
    }

    /**
     * The method allows the caller to quickly check whether
     * a server is available for access or not.
     * @param serverIP - the ip address to attempt to read
     * @return boolean value representing the server being up or not
     */
    public boolean pingServer(String serverIP) {

        // Flag variable for ping success
        boolean success = false;

        try {
            PingHttpThread pinger = new PingHttpThread();
            Thread httpThread = new Thread(pinger);

            // Send the http ping request
            httpThread.start();

            // Wait for thread to finish
            httpThread.join();

            // Get whether the value was a success or not
            success = pinger.getSuccess();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return success;
    }

    /**
     *
     * @param url - The IP address of the URL
     * @param port - The port of the web service
     * @param path - The app route for an application
     * @param useHttps - Whether or not to use Https in the URL
     * @return a URL object that is created using the parameters
     */
    public static URL buildURL(String url, String port, String path, boolean useHttps) throws MalformedURLException {
        StringBuilder builder = new StringBuilder();

        // Add the protocol
        if(useHttps)
            builder.append("https://");
        else
            builder.append("http://");

        // Add the URL
        builder.append(url);

        // Add the port
        builder.append(":");
        builder.append(port);

        // Add the app route
        builder.append("/");
        builder.append(path);

        return new URL(builder.toString());
    }
}
