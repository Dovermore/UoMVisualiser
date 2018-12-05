package WebPage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Util.Constants.prefix;
import static Util.Constants.searchPostfix;
import static Util.Constants.subjectPostfix;

public class LinkProcessor {
    private static int YEAR = 2019;

    private int year;
    // root url
    private String root;
    // Pattern to match search page against
    private Pattern searchPattern;
    // Pattern to match subject page against
    private Pattern subjectPattern;


    private static LinkProcessor ourInstance = new LinkProcessor();

    public static LinkProcessor getInstance() {
        return ourInstance;
    }

    public static void setYear(int year) {
        ourInstance.year = year;
        ourInstance.root = String.format(prefix, year);
        String pattern = ourInstance.root.replace("/", "\\/").replace(".", "\\.");
        ourInstance.searchPattern = Pattern.compile("^" + pattern + searchPostfix);
        ourInstance.subjectPattern = Pattern.compile("^" + pattern + subjectPostfix);
    }

    private LinkProcessor() {
        year = YEAR;
        root = String.format(prefix, year);
        String pattern = root.replace("/", "\\/").replace(".", "\\.");
        searchPattern = Pattern.compile("^" + pattern + searchPostfix);
        subjectPattern = Pattern.compile("^" + pattern + subjectPostfix);
    }

    public boolean isSubjectPage(String link) {
        Matcher subjectMatcher = subjectPattern.matcher(link);
        return subjectMatcher.matches();
    }

    public boolean isSearchPage(String link) {
        Matcher searchMatcher = searchPattern.matcher(link);
        return searchMatcher.matches();
    }

    public String getSubjectCode(String link) {
        if (isSubjectPage(link)) {
            Matcher subjectMatcher = subjectPattern.matcher(link);
            subjectMatcher.matches();

            return subjectMatcher.group(1);
        }
        return null;
    }

    public String getRoot() {
        return root;
    }


}
