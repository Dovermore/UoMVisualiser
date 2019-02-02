package Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.time.Year;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class Constants {
    /**
     * Default String if result not present
     */
    public final static String NULL_STRING = "null";

    /**
     * Empty document
     */
    public final static Document NULL_HTML = Jsoup.parse(NULL_STRING);

    /**
     * Default String if result not present
     */
    public final static Year NULL_YEAR = Year.parse("0000");

    /**
     * Empty document
     */
    public final static float NULL_FLOAT = 0.0f;

    /**
     * Default String if result not present
     */
    public final static Year DEFAULT_YEAR = Year.parse("2019");

    /**
     * Overview section String
     */
    public final static String OVERVIEW = "overview";
    /**
     * Eligibility and requirements String
     */
    public final static String ELIGIBILITY = "eligibility";
    public final static String ASSESSMENT = "assessment";
    public final static String DATES_TIMES = "datesTimes";
    public final static String FURTHER_INFO = "furtherInfo";
    public final static String PRINT = "print";
    public final static ArrayList<String> ALL_ENTRIES = new ArrayList<>();
    static {
        ALL_ENTRIES.add(OVERVIEW);
        ALL_ENTRIES.add(ELIGIBILITY);
        ALL_ENTRIES.add(ASSESSMENT);
        ALL_ENTRIES.add(DATES_TIMES);
        ALL_ENTRIES.add(FURTHER_INFO);
        ALL_ENTRIES.add(PRINT);
    }

    /**
     * LinkConstant matching related Constants
     */
    public final static class LinkConstant {
        /**
         * Sets the PREFIX for all pages to prevent navigate to unwanted pages.
         */
        public final static String PREFIX = "https://handbook.unimelb.edu.au/%s/subjects";

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
         * Data code key
         */
        public final static String CODE = "code";

        /**
         * SubjectData parsedData key
         */
        public final static String PARSED_DATA = "parsedData";

        /**
         * SubjectData rawData key
         */
        public final static String RAW_DATA = "rawData";

        /**
         * RawData linkDataGroup key
         */
        public final static String LINK_DATA_GROUP = "linkDataGroup";

        /**
         * LinkData link key
         */
        public final static String LINK = "link";

        /**
         * LinkData html key
         */
        public final static String HTML = "htmlString";

        /**
         * ParsedData name key
         */
        public final static String NAME = "name";

        /**
         * ParsedData overview key (Different from Constants.OVERVIEW, this is summarised overview)
         */
        public final static String OVERVIEW = "overview";

        /**
         * ParsedData name key
         */
        public final static String YEAR = "year";

        /**
         * ParsedData level key
         */
        public final static String LEVEL = "level";

        /**
         * ParsedData credit key
         */
        public final static String CREDIT = "credit";

        /**
         * ParsedData campus key
         */
        public final static String CAMPUS = "campus";

        /**
         * ParsedData availability key
         */
        public final static String AVAILABILITY = "availability";

        /**
         * ParsedData prerequisites key
         */
        public final static String PREREQUISITES = "prerequisites";

        /**
         * ParsedData prerequisites key
         */
        public final static String SUB_PREREQUISITES = "subPrerequisites";

        /**
         * ParsedData corequisites key
         */
        public final static String COREQUISITES = "corequisites";

        /**
         * ParsedData prohibitions key
         */
        public final static String PROHIBITIONS = "prohibitions";
    }

    /**
     * SubjectData parsing related Strings, All constants are small case letter!
     */
    public final static class HtmlParsingConstant {
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

        public final static class OverviewConstant {
            /**
             * year of offer for overview box
             */
            public final static String YEAR = "year of offer";
            /**
             * Overview box class name
             */
            public final static String CLASS_OVERVIEW_BOX = "course__overview-box";
            /**
             * SubjectData level for overview box
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
            public final static String COURSE_HEADER_SUBJECT_NAME_RE = "(.+?)\\s?\\(\\w{4}\\d{5}\\)";
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
        }

        public final static class AvailabilityConstant {

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
            public final static String ONE_OF = "one of";
            /**
             * SubjectData code regex
             */
            public final static String CODE_RE = "(\\w{4}\\d{5})";
            /**
             * SubjectData code regex
             */
            public final static Pattern CODE_PATTERN = Pattern.compile(CODE_RE, Pattern.CASE_INSENSITIVE);
            /**
             * Prerequisite regex
             */
            public final static String PREREQUISITE_RE = "((\\&)|(or)|(one of)|(and)|(\\w{4}\\d{5}))";
            /**
             * Prerequisite regex
             */
            public final static Pattern PREREQUISITE_PATTERN = Pattern.compile(PREREQUISITE_RE, Pattern.CASE_INSENSITIVE);
        }

    }

}
