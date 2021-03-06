package com.kutztown.projectmanagement.data;

import android.util.Log;

import java.util.ArrayList;

/**
 * @author Steven Gantz
 * @date 2/11/2016
 * <p/>
 * This object will contain everything required to have a full
 * entry in the user table. Each resource can be accessed, and
 * it can be transformed into a get string to be passed to
 * the web service.
 */
public class UserTableEntry implements TableEntry {

    // List of all attributes within the database
    // These attributes are private and don't appear in the javadoc
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String bio;
    private String projectList;
    private String picture;

    /**
     * General constructor, requires
     * only entries that cannot be null.
     * All others are set to empty.
     *
     * @param email    - User's email address
     * @param password - User's password
     */
    public UserTableEntry(String email, String password) {
        this.userId = -1;
        this.firstName = "";
        this.lastName = "";
        this.email = email;
        this.password = password;
        this.bio = "";
        this.projectList = "";
        this.picture = "";
    }

    /**
     * Secondary constructor, initialize every value in the object
     *
     * @param firstName   - User's first name
     * @param lastName    - User's last name
     * @param email       - User's email address
     * @param password    - User's password
     * @param bio         - User's biography
     * @param projectList - list of projects the user is in dash separated
     * @param picture     - text link to the user's picture
     */
    public UserTableEntry(String firstName, String lastName,
                          String email, String password, String bio, String projectList,
                          String picture) {
        this.userId = -1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.projectList = projectList;
        this.picture = picture;
    }

    /**
     * Tertiary constructor, takes an arraylist of each element in an entry and builds the object.
     *
     * @param userTableEntry - the arraylist of entries
     **/
    public UserTableEntry(ArrayList<String> userTableEntry) {

        // Raise an exception if the array does not contain all of the required values
        if (userTableEntry.size() != 8) {
            //Log.d("debug", String.valueOf(userTableEntry.size()));
            throw new RuntimeException();
        }
        Log.d("debug", Integer.toString(userTableEntry.size()));

        // The arraylist should have all of the entries in the order that they are in the table.
        try{
            this.userId = Integer.parseInt(userTableEntry.get(0));
        } catch (NumberFormatException e){
            this.userId = -1;
        }
        this.firstName = userTableEntry.get(1);
        this.lastName = userTableEntry.get(2);
        this.email = userTableEntry.get(3);
        this.password = userTableEntry.get(4);
        this.bio = userTableEntry.get(5);
        this.projectList = userTableEntry.get(6);
        this.picture = userTableEntry.get(7);
    }

    /**
     * Empty constructor for construction purposes
     */
    public UserTableEntry(){

    }


    /**
     * returns a string that can be added to a URL for a get request.
     * Note. userId is never sent to the database as it is generated by db.
     *
     * @return completed string that can be used for a get request
     */
    @Override
    public String writeAsGet() {
        return "firstName=" + this.firstName +
                "&lastName=" + this.lastName +
                "&email=" + this.email +
                "&password=" + this.password +
                "&bio=" + this.bio +
                "&projectList=" + this.projectList +
                "&picture=" + this.picture;
    }

    @Override
    public String writeAsValueString() {
        return "\"" + this.firstName + "\",\"" +
                this.lastName + "\",\"" +
                this.email + "\",\"" +
                this.password + "\",\"" +
                this.bio + "\",\"" +
                this.projectList + "\",\"" +
                this.picture + "\"";
    }

    @Override
    public String getColumnString() {
        return "firstname,lastname,email,password,bio,projectlist,picture";
    }

    // Getters and setters for each method for ease of use and manipulation
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProjectList() {
        if(projectList == null){
            return "";
        } else {
            return projectList;
        }
    }

    public void setProjectList(String projectList) {
        this.projectList = projectList;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
