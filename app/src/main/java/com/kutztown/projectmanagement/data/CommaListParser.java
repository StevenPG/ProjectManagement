package com.kutztown.projectmanagement.data;

import java.util.ArrayList;

/**
 * @author Steven Gantz
 * @date 2/9/2016
 * @file CommaListParser.java
 *
 * This class has a single public static method that
 * takes in a string that contains a comma separated list
 * and places each element of this list in a java.util.ArrayList;
 */
public class CommaListParser {

    /**
     * This method allows for a comma separated list within a
     * string to be easily accessed from an ArrayList.
     *
     * @param list - string representing a comma separated list
     * @return - an arraylist containing each element of parameter list
     */
    public static ArrayList<String> parseString(String list){
        ArrayList<String> newList = new ArrayList<>();
        for(String string : list.split(",")){
            newList.add(string.replaceAll("\\s", ""));
        }
        return newList;
    }
}
