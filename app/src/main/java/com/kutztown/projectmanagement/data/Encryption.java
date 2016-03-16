package com.kutztown.projectmanagement.data;

/**
 * Created by Hector on 3/14/16.
 */
public final class Encryption {
    char character = ' ';
    String str;

    public String encrypt(String password) {

        StringBuilder sb = new StringBuilder();
        // loop through each character in the password
        // assigns it to the stringbuilder in ascii form
        // and save the whole number in a integer
        for (int j = 0; j < password.length(); j++)
            sb.append((int) password.charAt(j));
        return sb.toString();
    }

    public String descrypt(String password) {
        StringBuilder strB = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            character = password.charAt(i);
            strB.append((char) character);
        }
        String descryptedP = strB.toString();
        return descryptedP;

    }

}
