package com.kutztown.projectmanagement.com.kutztown.projectmanagement.networking;

import android.util.Log;

import com.kutztown.projectmanagement.data.ApplicationData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Steven Gantz on 2/4/2016.
 * @date 2/4/2016
 * This class abstracts away the annoying
 * aspects of making the HTTP calls.
 */
public class HTTPHandler {

    /**
     * General empty constructor
     */
    public HTTPHandler(){

    }

    /**
     * Attempt to create an account on the server
     *
     * @return 0 - if create account was successful
     * @return 1 - if account already exists
     * @return 2 - if an error occurred
     * @return 3 - if the server is down
     */
    public String createAccount(String parameterString){

        String data;
        boolean isServerUp = this.pingServer(ApplicationData.SERVER_IP);
        if(!isServerUp){
            return "3";
        }

        try {
            URL url = buildURL(ApplicationData.SERVER_IP, ApplicationData.SERVER_PORT, "createaccount", false, parameterString);

            // Generate the connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            data = readFromURLConnection(urlConnection);

        } catch (MalformedURLException e) {
            Log.v("error", "Error forming URL");
            return "2";
        } catch (IOException e) {
            Log.v("error", "Error accessing url");
            return "2";
        }

        return String.valueOf(data.charAt(0));
    }

    /**
     *
     * @param url - url to read from
     * @return - String returned from a web service
     */
    private String readFromURLConnection(HttpURLConnection url) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                        url.getInputStream()
                ));

        // Build String
        String inputLine;
        StringBuilder builder = new StringBuilder();
        while ((inputLine = reader.readLine()) != null){
            builder.append(inputLine);
        }

        // Close reader and return built string
        reader.close();
        return builder.toString();


    }

    /**
     * The method allows the caller to quickly check whether
     * a server is available for access or not.
     * @param serverIp - the ip address to attempt to read
     * @return boolean value representing the server being up or not
     */
    public boolean pingServer(String serverIp) {

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
     * @param getParameters - null string if no params, formatted parameter string if valid
     * @return a URL object that is created using the parameters
     */
    public static URL buildURL(String url, String port, String path, boolean useHttps, String getParameters) throws MalformedURLException {
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

        // Add the parameter list if getParameters is not null
        if(getParameters != null){
            builder.append("?");
            builder.append(getParameters);
        }

        return new URL(builder.toString());
    }
}
