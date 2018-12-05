package Util;

public class Constants {
    // Sets the prefix for all pages to prevent navigate to unwanted pages.
    public final static String prefix = "https://handbook.unimelb.edu.au/%d/subjects";
    // Postfix for search page
    public final static String searchPostfix = "(\\?page=\\d+)?$";
    // Postfix for subject page
    public final static String subjectPostfix = "\\/([a-z]{4}\\d{5})(\\/.+)?$";
    // Path to the file
    public final static String fPath = "./result/";
    // Name of the csv write to
    public final static String linkFName = "subjectLink.csv";

    // JSON format of subject information
    public final static String subjectFName = "subjectInfo.json";
}
