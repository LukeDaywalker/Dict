package com.util;

/**
 * Created by olivia on 2016/12/20.
 */
public class TextUtils {

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static String connectStr(String oldStr, String newStr) {
        if (isEmpty(oldStr)) {
            return newStr;
        }
        return oldStr + "," + newStr;
    }
}
