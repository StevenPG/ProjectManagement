package com.kutztown.projectmanagement.data;

/**
 * @author Steven Gantz
 * @date 2/13/2016
 *
 * This file is a contract for each tableEntry object, requiring them
 * to implement its methods to make sure they are all easily usable within
 * the context of the application.
 */
public interface TableEntry {
    /**
     * returns a string that can be added to a URL for a get request.
     * @return completed string that can be used for a get request
     */
    String writeAsGet();
}
