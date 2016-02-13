package com.kutztown.projectmanagement.exception;

/**
 * @author Steven Gantz
 * @date 2/13/2016
 * This class allows for easier error handling
 * when the server is not up and running.
 */
public class ServerNotRunningException extends Exception {

    /**
     * Generic exception that can be error handled in code
     */
    public ServerNotRunningException(){ super(); }

    /**
     * Secondary exception that allows addition of a message
     * @param message - specific hardcoded error message
     */
    public ServerNotRunningException(String message){ super(message); }
}
