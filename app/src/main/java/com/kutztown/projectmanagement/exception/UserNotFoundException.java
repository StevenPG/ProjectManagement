package com.kutztown.projectmanagement.exception;

/**
 * @author Steven Gantz
 * @date 2/13/2016
 * This exception is thrown whenever the database replies
 * to the application that a user selected was not found
 * in the database.
 */
public class UserNotFoundException extends Exception {

    /**
     * Generic exception that can be error handled in code
     */
    public UserNotFoundException() { super(); }

    /**
     * Secondary exception that allows addition of a message
     * @param message - specific hardcoded error message
     */
    public UserNotFoundException(String message){ super(message); }

}
