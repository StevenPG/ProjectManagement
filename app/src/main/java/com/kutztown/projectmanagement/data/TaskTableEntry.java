package com.kutztown.projectmanagement.data;

import java.util.ArrayList;

/**
 * @author Steven Gantz
 * @date 2/11/2016
 *
 * This object will contain everything required to have a full
 * entry in the task table. Each resource can be accessed, and
 * it can be transformed into a get string to be passed to
 * the web service.
 */
public class TaskTableEntry {

    // List of all attributes within the database
    // These attributes are private and don't appear in the javadoc
    private int taskID;
    private String user;
    private String project;
    private String taskName;
    private String taskDesc;
    private String taskProgress;
    private String taskStatus;
    private String taskPriority;
    private String taskDueDate;
    private String taskDep;

    // Getters and setters for each method for ease of use and manipulation
    public int getTaskID() { return taskID;}
    public void setTaskID(int taskID) { this.taskID = taskID;}
    public String getUser() { return user;}
    public void setUser(String user) { this.user = user;}
    public String getProject() { return project;}
    public void setProject(String project) { this.project = project;}
    public String getTaskName() { return taskName;}
    public void setTaskName(String taskName) { this.taskName = taskName;}
    public String getTaskDesc() { return taskDesc;}
    public void setTaskDesc(String taskDesc) { this.taskDesc = taskDesc;}
    public String getTaskProgress() { return taskProgress;}
    public void setTaskProgress(String taskProgress) { this.taskProgress = taskProgress;}
    public String getTaskStatus() { return taskStatus;}
    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus;}
    public String getTaskPriority() { return taskPriority;}
    public void setTaskPriority(String taskPriority) { this.taskPriority = taskPriority;}
    public String getTaskDueDate() { return taskDueDate;}
    public void setTaskDueDate(String taskDueDate) { this.taskDueDate = taskDueDate;}
    public String getTaskDep() { return taskDep;}
    public void setTaskDep(String taskDep) { this.taskDep = taskDep;}

    /**
     * General constructor, requires
     * only entries that cannot be null.
     * All others are set to empty.
     *
     * @param taskId - Task id
     * @param user - user assigned to task
     * @param project - project task is part of
     * @param taskName - name of task
     * @param taskDesc - task description
     * @param taskDueDate - due date of task
     * @param taskPriority - priority status of task
     */
    public TaskTableEntry(int taskId, String user, String project, String taskName, String taskDesc,
                          String taskPriority, String taskDueDate, String taskDep){
        this.taskID = taskId;
        this.user = user;
        this.project = project;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskPriority = taskPriority;
        this.taskDueDate = taskDueDate;
        this.taskProgress = "";
        this.taskStatus = "";
        this.taskDep = "";
    }

    /**
     * Secondary constructor, initialize every value in the object
     *
     * @param taskId - Task id
     * @param user - user assigned to task
     * @param project - project task is part of
     * @param taskName - name of task
     * @param taskDesc - task description
     * @param taskProgress - total task progress
     * @param taskStatus - current task status
     * @param taskDueDate - due date of task
     * @param taskPriority - priority status of task
     */
    public TaskTableEntry(int taskId, String user, String project,
                          String taskName, String taskDesc,
                          String taskProgress, String taskStatus,
                          String taskPriority, String taskDueDate, String taskDep ){
        this.taskID = taskId;
        this.user = user;
        this.project = project;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskProgress = taskProgress;
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.taskDueDate = taskDueDate;
        this.taskProgress = taskProgress;
        this.taskStatus = taskStatus;
        this.taskDep = taskDep;
    }

    /**
     * Tertiary constructor, takes an arraylist of each element in an entry and builds the object.
     * @param taskTableEntry - the arraylist of entries
     **/
    public TaskTableEntry(ArrayList<String> taskTableEntry){

        // Raise an exception if the array does not contain all of the required values
        if(taskTableEntry.size() != 12){
            throw new RuntimeException();
        }

        // The arraylist should have all of the entries in the order that they are in the table.
        this.taskID = Integer.parseInt(taskTableEntry.get(0));
        this.user = taskTableEntry.get(1);
        this.project = taskTableEntry.get(2);
        this.taskName = taskTableEntry.get(3);
        this.taskDesc = taskTableEntry.get(4);
        this.taskProgress = taskTableEntry.get(5);
        this.taskStatus = taskTableEntry.get(6);
        this.taskPriority = taskTableEntry.get(7);
        this.taskDueDate = taskTableEntry.get(8);
        this.taskProgress = taskTableEntry.get(9);
        this.taskStatus = taskTableEntry.get(10);
        this.taskDep = taskTableEntry.get(11);
    }


    /**
     * returns a string that can be added to a URL for a get request.
     * Note. TaskID is never sent to the database as it is generated by db.
     *
     * @return completed string that can be used for a get request
     */
    public String writeAsGet() {
        return "user=" + this.user +
                "project=" + this.project +
                "taskName=" + this.taskName +
                "taskDesc=" + this.taskDesc +
                "taskProgress=" + this.taskProgress +
                "taskStatus=" + this.taskStatus +
                "taskPriority=" + this.taskPriority +
                "taskDueDate=" + this.taskDueDate +
                "taskDep=" + this.taskDep;

    }
}

