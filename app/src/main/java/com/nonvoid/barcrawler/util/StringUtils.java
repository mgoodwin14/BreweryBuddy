package com.nonvoid.barcrawler.util;

/**
 * Created by Matt on 5/12/2017.
 */

public class StringUtils {

    public static boolean isNullOrEmpty(String string){
        return string == null || string.isEmpty();
    }

    public static boolean isNotNullOrEmpty(String string){
        return !isNullOrEmpty(string);
    }
}
