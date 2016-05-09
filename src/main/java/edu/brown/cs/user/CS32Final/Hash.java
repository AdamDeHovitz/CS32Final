package edu.brown.cs.user.CS32Final;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lc50 on 8/05/16.
 */
public class Hash {

    public static String getHashedPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            String generatedPassword = sb.toString();
            return generatedPassword;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
