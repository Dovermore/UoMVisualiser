package Util;

import java.util.regex.Pattern;

public final class Constants {
    /**
     * Default String if result not present
     */
    public final static String NULL = "null";


    /**
     * LinkConstant matching related Constants
     */
    public final static class LinkConstant {
        /**
         * Sets the PREFIX for all pages to prevent navigate to unwanted pages.
         */
        public final static String PREFIX = "https://handbook.unimelb.edu.au/%d/subjects";

        /**
         * Postfix regex String for search page
         */
        public final static String SEARCH_POSTFIX_RE = "(\\?page=\\d+)?$";

        /**
         * Postfix regex String for subject page
         */
        public final static String SUBJECT_POSTFIX_RE = "\\/(\\w{4}\\d{5})(\\/.+)?$";

        /**
         * Postfix for overview page
         */
        public final static String OVERVIEW_POSTFIX = "";

        /**
         * Postfix for eligibility page
         */
        public final static String ELIGIBILITY_POSTFIX = "/eligibility-and-requirements";

        /**
         * Postfix for assessment page
         */
        public final static String ASSESSMENT_POSTFIX = "/assessment";

        /**
         * Postfix for dates and times page
         */
        public final static String DATES_TIMES_POSTFIX = "/dates-times";

        /**
         * Postfix for further information page
         */
        public final static String FURTHER_INFO_POSTFIX = "/further-information";

        /**
         * Postfix for one-page display page
         */
        public final static String PRINT_POSTFIX = "/print";
    }

    /**
     * File related Constants
     */
    public final static class FileConstant {

        /**
         * Path to results files
         */
        public final static String F_PATH = "./result/";

        /**
         * Name of the csv of subject links write to
         */
        public final static String SUBJECT_LINK_CSV = "subjectLink.csv";

        /**
         * JSON format of subject information file name write to
         */
        public final static String SUBJECT_INFO_JSON = "subjectInfo.json";
    }

    /**
     * Constants used in writing/reading JSON files
     */
    public final static class JSONKey {
        /**
         * Subject code key
         */
        public final static String CODE = "code";

        /**
         * Subject info key (first level)
         */
        public final static String INFO = "info";

        /**
         * Subject link info within info key
         */
        public final static String LINK_INFO = "linkInfo";

        /**
         * Subject link/document type (overview/assessment/etc..) key
         */
        public final static String LINK_TYPE = "type";

        /**
         * Subject url key
         */
        public final static String URL = "url";

        /**
         * Subject html key
         */
        public final static String HTML = "document";

        /**
         * Subject parsed information key (within info)
         */
        public final static String PARSED_INFO = "parsedInfo";

        // TODO parsed subject constants to be added
    }

    /**
     * Subject parsing related Strings, All constants are small case letter!
     */
    public final static class ParsingConstant {
        /**
         * Overview section String
         */
        public final static String OVERVIEW = "overview";

        /**
         * Eligibility and requirements String
         */
        public final static String ELIGIBILITY = "eligibility";

        /**
         * Overview box class name
         */
        public final static String CLASS_OVERVIEW_BOX = "course__overview-box";

        /**
         * year of offer for overview box
         */
        public final static String YEAR = "year of offer";

        /**
         * Subject level for overview box
         */
        public final static String SUBJECT_LEVEL = "subject level";

        /**
         * campus for overview box
         */
        public final static String CAMPUS = "campus";

        /**
         * Availability for overview box
         */
        public final static String AVAILABILITY = "availability";

        /**
         * String used in detecting availability
         */
        public final static String NOT_AVAILABLE = "not available";

        /**
         * Fees for overview box
         */
        public final static String fees = "fees";

        /**
         * Outer course header inner class name
         */
        public final static String COURSE_HEADER_INNER = "header--course-and-subject__inner";

        /**
         * Course header main class name
         */
        public final static String COURSE_HEADER_MAIN = "header--course-and-subject__main";

        /**
         * Course header detail class name
         */
        public final static String COURSE_HEADER_DETAIL = "header--course-and-subject__details";

        /**
         * Course header main regex for getting name
         */
        public final static String COURSE_HEADER_SUBJECT_NAME_RE = "(.+)\\(\\w{4}\\d{5}\\)";
        /**
         * Compiled pattern of Course header main regex for getting name
         */
        public final static Pattern COURSE_HEADER_SUBJECT_NAME_PATTERN = Pattern.compile(COURSE_HEADER_SUBJECT_NAME_RE);

        /**
         * Course header detail regex for getting credit value
         */
        public final static String COURSE_HEADER_CREDIT_RE = "Points: (\\d+\\.?\\d*)";

        /**
         * Compiled pattern of Course header detail regex for getting credit value
         */
        public final static Pattern COURSE_HEADER_CREDIT_PATTERN = Pattern.compile(COURSE_HEADER_CREDIT_RE);

        /**
         * Prerequisite id tag
         */
        public final static String PREREQUISITES = "prerequisites";

        /**
         * Deciding the mode of adding new requisites - type 1 "and" relation, creating new rows
         */
        public final static String AND_1 = "and";

        /**
         * Deciding the mode of adding new requisites - type 2 "and" relation, creating new rows
         */
        public final static String AND_2 = "&";

        /**
         * Deciding the mode of adding new requisites - type 1 "or" relation, using the same row
         */
        public final static String OR_1 = "or";

        /**
         * Deciding the mode of adding new requisites - type 2 "or" relation, using the same row
         */
        public final static String OR_2 = "one of";

        /**
         * Subject code regex
         */
        public final static String CODE_RE = "(\\w{4}\\d{5})";

        /**
         * Prerequisite regex
         */
        public final static String PREREQUISITE_RE = "((\\&)|(or)|(one of)|(and)|(\\w{4}\\d{5}))";
        /**
         * Prerequisite regex
         */
        public final static Pattern PREREQUISITE_PATTERN = Pattern.compile(PREREQUISITE_RE, Pattern.CASE_INSENSITIVE);
    }

    /**
     * HTML constants of tags, selectors, etc...
     */
    public final static class HTMLConstant {
        /**
         * Table body tag
         */
        public final static String TBODY = "tbody";

        /**
         * Table row tag
         */
        public final static String TR = "tr";

        /**
         * Table cell tag
         */
        public final static String TD = "td";

    }
}
