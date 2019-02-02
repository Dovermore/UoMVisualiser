package Util;

import org.json.JSONArray;
import org.jsoup.nodes.Document;

import java.io.File;
import java.time.Year;
import java.util.ArrayList;

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

    public static <T> ArrayList<T> jsonArrayToArrayList(JSONArray jsonArray) {
        ArrayList<T> arrayList = new ArrayList<>();
        try {
            for (Object object : jsonArray) {
                T element = (T) object;
                arrayList.add(element);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * Check if a given object is of NULL value
     * @param object The object to be checked
     * @return true if the object resembles a null value
     */
    public static boolean isNull(Object object) {
        if (object instanceof String) {
            return stringIsNull((String) object);
        } else if (object instanceof Document) {
            return htmlIsNull((Document) object);
        } else if (object instanceof Year) {
            return yearIsNull((Year) object);
        } else if (object instanceof Float) {

        }
        // If it's not supported type, then simply null
        return true;
    }

    private static boolean stringIsNull(String string) {
        return string.equalsIgnoreCase(Constants.NULL_STRING);
    }

    private static boolean htmlIsNull(Document html) {
        return html.html().equalsIgnoreCase(Constants.NULL_HTML.html()) || html.text().isEmpty();
    }

    private static boolean yearIsNull(Year year) {
        return year.compareTo(Constants.NULL_YEAR) == 0;
    }

    private static boolean floatIsNull(Float number) {
        return Math.abs(number - Constants.NULL_FLOAT) < 1e-6;
    }
}
