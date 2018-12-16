package Crawler;

import Util.Constants;

import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Util.Constants.LinkConstant.PREFIX;
import static Util.Constants.LinkConstant.SEARCH_POSTFIX_RE;
import static Util.Constants.LinkConstant.SUBJECT_POSTFIX_RE;

// TODO ideally should also use strategy design pattern

/**
 * This class determines if a page belongs to subject page or a search page.
 */
public class LinkProcessor {
    private Year year = Constants.DEFAULT_YEAR;
    // root url
    private String root = String.format(PREFIX, year.toString());
    // Pattern to match search page against
    private Pattern searchPattern;
    // Pattern to match subject page against
    private Pattern subjectPattern;


    private static LinkProcessor ourInstance = new LinkProcessor();

    public static LinkProcessor getInstance() {
        return ourInstance;
    }

    public static void setYear(Year year) {
        ourInstance.year = year;
        ourInstance.root = String.format(PREFIX, year.toString());
        String pattern = ourInstance.root.replace("/", "\\/").replace(".", "\\.");
        ourInstance.searchPattern = Pattern.compile("^" + pattern + SEARCH_POSTFIX_RE);
        ourInstance.subjectPattern = Pattern.compile("^" + pattern + SUBJECT_POSTFIX_RE);
    }

    private LinkProcessor() {
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
