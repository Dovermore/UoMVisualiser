package Util;

import java.io.File;

public class HelperMethods {
    public static boolean containsIgnoreCase(String str, String searchStr)     {
        if(str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }

    public static File createFile(String fName) {
        File file = new File(fName);
        File parent = file.getParentFile();

        if (parent == null || parent.exists() || parent.mkdirs()) {
            return file;
        } else {
            System.err.format("Failed to create file: %s", fName);
            return null;
        }
    }
}
