package WebPage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Util.Constants.LinkConstant.PREFIX;
import static Util.Constants.LinkConstant.SEARCH_POSTFIX_RE;
import static Util.Constants.LinkConstant.SUBJECT_POSTFIX_RE;

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
        ourInstance.root = String.format(PREFIX, year);
        String pattern = ourInstance.root.replace("/", "\\/").replace(".", "\\.");
        ourInstance.searchPattern = Pattern.compile("^" + pattern + SEARCH_POSTFIX_RE);
        ourInstance.subjectPattern = Pattern.compile("^" + pattern + SUBJECT_POSTFIX_RE);
    }

    private LinkProcessor() {
        year = YEAR;
        root = String.format(PREFIX, year);
        String pattern = root.replace("/", "\\/").replace(".", "\\.");
        searchPattern = Pattern.compile("^" + pattern + SEARCH_POSTFIX_RE);
        subjectPattern = Pattern.compile("^" + pattern + SUBJECT_POSTFIX_RE);
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
