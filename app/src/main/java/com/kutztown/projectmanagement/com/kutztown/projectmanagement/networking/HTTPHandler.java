package com.kutztown.projectmanagement.com.kutztown.projectmanagement.networking;

import android.util.Log;

import com.kutztown.projectmanagement.data.ApplicationData;
import com.kutztown.projectmanagement.data.CommaListParser;
import com.kutztown.projectmanagement.data.UserTableEntry;
import com.kutztown.projectmanagement.exception.ServerNotRunningException;
import com.kutztown.projectmanagement.exception.UserNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
     *
     * @param searchValue - value to search for
     * @param searchRecord - what field in usertable to check in
     * @return - the table entry of the user requested
     * @throws ServerNotRunningException - if the server is not running
     * @throws UserNotFoundException - if the user was not found
     */
    public UserTableEntry selectUser(String searchValue, String searchRecord)
            throws ServerNotRunningException, UserNotFoundException{

        if(!this.pingServer(ApplicationData.SERVER_IP)){
            throw new ServerNotRunningException();
        }

        // Create the object that will get populated
        UserTableEntry userTableEntry = null;

        try {
            String parameterString = "search=" + searchValue
                    + "&record=" + searchRecord;

            URL url = buildURL(ApplicationData.SERVER_IP,
                    ApplicationData.SERVER_PORT,
                    "selectuser", false, parameterString);

            SelectUserTask task = new SelectUserTask(url);
            task.execute((Void) null);

            while(!task.grabString){
                // Busy wait until the connection is done
                Log.d("debug", "Busy waiting until connection is done");
                Log.d("debug", Boolean.toString(task.grabString));
            }
            String userEntry = task.userString;

            // If the user wasn't found, a 0 will be returned
            if(userEntry.charAt(0) == '0'){
                throw new UserNotFoundException();
            }

            // If there was an issue, a 2 will be returned
            if(userEntry.charAt(0) == '2'){
                throw new UserNotFoundException("Search query wasn't filled out");
            }

            // Parse the string into an arraylist
            ArrayList<String> parsedList = CommaListParser.parseString(userEntry);

            // build the object using the arraylist as a parameter
            userTableEntry = new UserTableEntry(parsedList);

            return userTableEntry;

        } catch (MalformedURLException e) {
            throw new ServerNotRunningException("Some error occurred building URL");
        } catch (IOException e) {
            throw new ServerNotRunningException();
        }
    }



    /**
     * This method reaches out to the webservice at
     * the /version route and requests the webservice version.
     * @return - The web service version as a string.
     */
    public String getWSVersion(){
        String version = null;
        if(!this.pingServer(ApplicationData.SERVER_IP)){
            return "X";
        }

        try {
            URL url = buildURL(ApplicationData.SERVER_IP,
                    ApplicationData.SERVER_PORT,"version",
                    false, "");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            version = readFromURLConnection(httpURLConnection);
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            return "URL Error";
        } catch (IOException e) {
            //e.printStackTrace();
            return "Can't reach page";
        }

        return version;
    }

    /**
     *
     * @param parameterString - formatted parameter string
     * @return 0 - if the user is not found
     * @return 1 - if the user is found
     * @return 2 - if the server is down
     * @return 3 - if another error occurred
     */
    public String login(String parameterString){

        String data;
        if(!this.pingServer(ApplicationData.SERVER_IP)){
            return "2";
        }

        try {
            URL url = buildURL(ApplicationData.SERVER_IP, ApplicationData.SERVER_PORT, "login", false, parameterString);

            // Generate the connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            data = readFromURLConnection(urlConnection);

        } catch (MalformedURLException e) {
            Log.v("error", "Error forming URL");
            return "3";
        } catch (IOException e) {
            Log.v("error", "Error accessing url");
            return "3";
        }

        return String.valueOf(data.charAt(0));
    }

    /**
     * Attempt to create an account on the server
     *
     * @param parameterString - formatted parameter string
     * @return 0 - if create account was successful
     * @return 1 - if account already exists
     * @return 2 - if an error occurred
     * @return 3 - if the server is down
     */
    public String createAccount(String parameterString){

        String data;
        if(!this.pingServer(ApplicationData.SERVER_IP)){
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
    public static String readFromURLConnection(HttpURLConnection url) throws IOException {
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
