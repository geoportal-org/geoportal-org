package com.eversis.esa.geoss.curated.resources.util;

import java.sql.Timestamp;

/**
 * The type Code generator util.
 */
public class CodeGeneratorUtil {

    /**
     * Generate code string.
     *
     * @param userId the user id
     * @return the string
     */
    public static String generateCode(String title, long userId) {
        String titleTransformation = title.toLowerCase().replace(' ','_');
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return titleTransformation + "_" + userId + "_" + timestamp.getTime();
    }

}
