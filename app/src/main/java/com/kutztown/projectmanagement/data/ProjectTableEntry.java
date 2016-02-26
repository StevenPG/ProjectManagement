package com.kutztown.projectmanagement.data;

import java.util.ArrayList;

/**
 * @author Steven Gantz
 * @date 2/11/2016
 * <p/>
 * This object will contain everything required to have a full
 * entry in the project table. Each resource can be accessed, and
 * it can be transformed into a get string to be passed to
 * the web service.
 */
public class ProjectTableEntry implements TableEntry {

    // List of all attributes within the database
    // These attributes are private and don't appear in the javadoc
    private int projectId;
    private String leaderList;
    private String memberList;
    private String taskList;
    private String projectName;
    private String projectDesc;
    private String projectProgress;

    /**
     * General constructor, requires
     * only entries that cannot be null.
     * All others are set to empty.
     *
     * @param projectId   Id of the project (This does not get passed into the database EVER
     * @param leaderList  List of leaders (comma separated)
     * @param projectName Project name
     * @param projectDesc Description of project
     */
    public ProjectTableEntry(String leaderList, String projectName, String projectDesc) {
        this.projectId = -1;
        this.leaderList = leaderList;
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.memberList = "";
        this.taskList = "";
        this.projectProgress = "";
    }

    /**
     * Secondary constructor, initialize every value in the object
     *
     * @param projectId       - Project ID assigned from database
     * @param leaderList      - List of leaders (comma separated)
     * @param projectName     - Name of project
     * @param projectDesc     - Description of project
     * @param memberList      - List of members (-- separated)
     * @param taskList        - List of tasks (-- separated)
     * @param projectProgress - Total project progress
     */
    public ProjectTableEntry(String leaderList, String projectName, String projectDesc,
                             String memberList, String taskList, String projectProgress) {
        this.projectId = -1;
        this.leaderList = leaderList;
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.memberList = memberList;
        this.taskList = taskList;
        this.projectProgress = projectProgress;
    }

    /**
     * Tertiary constructor, takes an arraylist of each element in an entry and builds the object.
     *
     * @param projectTableEntry - the arraylist of entries
     **/
    public ProjectTableEntry(ArrayList<String> projectTableEntry) {

        // Raise an exception if the array does not contain all of the required values
        if (projectTableEntry.size() != 7) {
            throw new RuntimeException();
        }

        // The arraylist should have all of the entries in the order that they are in the table.
        this.projectId = Integer.parseInt(projectTableEntry.get(0));
        this.leaderList = projectTableEntry.get(1);
        this.memberList = projectTableEntry.get(2);
        this.taskList = projectTableEntry.get(3);
        this.projectName = projectTableEntry.get(4);
        this.projectDesc = projectTableEntry.get(5);
        this.projectProgress = projectTableEntry.get(6);
    }

    /**
     * Empty constructor for construction purposes
     */
    public ProjectTableEntry(){

    }

    /**
     * returns a string that can be added to a URL for a get request.
     * Note. ProjectID is never sent to the database as it is generated by db.
     *
     * @return completed string that can be used for a get request
     */
    @Override
    public String writeAsGet() {
        return "leaderlist=" + this.leaderList +
                "&memberlist=" + this.memberList +
                "&tasklist=" + this.taskList +
                "&projectname=" + this.projectName +
                "&projectdesc=" + this.projectDesc +
                "&projectprogress" + this.projectProgress;
    }

    @Override
    public String writeAsValueString() {
        return "\"" + this.leaderList + "\",\"" +
                this.memberList + "\",\"" +
                this.taskList + "\",\"" +
                this.projectName + "\",\"" +
                this.projectDesc + "\",\"" +
                this.projectProgress + "\"";
    }

    @Override
    public String getColumnString() {
        return "leaderlist,memberlist,tasklist,projectname,projectdescription,projectprogress";
    }

    @Override
    public String getUpdateString() {
        return null;
    }

    // Getters and setters for each method for ease of use and manipulation
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getLeaderList() {
        return leaderList;
    }

    public void setLeaderList(String leaderList) {
        this.leaderList = leaderList;
    }

    public String getMemberList() {
        return memberList;
    }

    public void setMemberList(String memberList) {
        this.memberList = memberList;
    }

    public String getTaskList() {
        return taskList;
    }

    public void setTaskList(String taskList) {
        this.taskList = taskList;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public String getProjectProgress() {
        return projectProgress;
    }

    public void setProjectProgress(String projectProgress) {
        this.projectProgress = projectProgress;
    }
}
