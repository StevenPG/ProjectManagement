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

    /**
     * This method returns the object in the form of an insert that
     * can be inserted into the database.
     * @return
     */
    String writeAsValueString();

    /**
     * This method returns a preset column string for use in
     * polymorphic inserting into the database.
     */
    String getColumnString();
}
