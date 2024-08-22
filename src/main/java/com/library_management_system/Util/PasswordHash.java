package com.library_management_system.Util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}