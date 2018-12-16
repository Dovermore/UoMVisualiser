package Subject;

import Util.Constants;

import Util.HelperMethods;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.stream.Collectors;


// TODO VERY important, the getXXXXX method will be used by library to determine the parsed json object,
// TODO Making new class of info and link info will automatically convert current json structure to the desired structure!
// TODO This file has TOO MANY LINES, need to delegate functions to component class. Think of a way of using delegation to write further refactor
public class Subject {

    // TODO make object for most of the attributes, so it's easier to link and filter semesters
    //      Links make a specific link object, and fields make classes based on it's feature

    // TODO Clean up the default value of these fields. Lots fields don't have elegant ways of handling NULL
    // Parsed contents

    private String code = Constants.NULL;

    private String name = Constants.NULL;
    private String subjectLevel;
    private Year year = Year.parse("0000");
    private float credit;
    private String campus = Constants.NULL;
    private ArrayList<String> availability = new ArrayList<>();
    // TODO a proper subject overview is need to be extracted
    private String subjectOverview;

    // The prerequisites can have 'or' condition. Use a 2D structure instead
    private ArrayList<HashSet<String>> prerequisites = new ArrayList<>();
    private HashSet<String> corequisites = new HashSet<>();
    private HashSet<String> prohibitions = new HashSet<>();

    // Links, by default is "null"
    private String overviewLink = Constants.NULL;
    private String eligibilityLink = Constants.NULL;


    // HTML documents
    private Document overviewDocument;
    private Document eligibilityDocument;



    public Subject(String overviewLink, String code) {
        this.code = code;
        this.overviewLink = overviewLink;
    }

    public Subject(@org.jetbrains.annotations.NotNull CSVRecord record) {
        this.code = record.get(0);
        this.overviewLink = record.get(1);
        this.eligibilityLink = record.get(2);

        try {
            if (!overviewLink.equals(Constants.NULL)) {
                overviewDocument = Jsoup.connect(overviewLink).get();
            }
            if (!eligibilityLink.equals(Constants.NULL)) {
                eligibilityDocument = Jsoup.connect(eligibilityLink).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Subject(JSONObject jsonObject) {
        try {
            code = jsonObject.getString(Constants.JSONKey.CODE);
            JSONObject info = jsonObject.getJSONObject(Constants.JSONKey.INFO);
            JSONObject linkInfo = info.getJSONObject(Constants.JSONKey.LINK_INFO);

            // Note here: The overview document, eligibility document if not existed will be seen as a html containing
            // Only null as body!
            {
                overviewLink = linkInfo.getJSONObject(Constants.ParsingConstant.OVERVIEW).getString(Constants.JSONKey.URL);
                overviewDocument = Jsoup.parse(linkInfo.getJSONObject(Constants.ParsingConstant.OVERVIEW)
                        .getString(Constants.JSONKey.HTML));

                eligibilityLink = linkInfo.getJSONObject(Constants.ParsingConstant.ELIGIBILITY).getString(Constants.JSONKey.URL);
                eligibilityDocument = Jsoup.parse(linkInfo.getJSONObject(Constants.ParsingConstant.ELIGIBILITY)
                        .getString(Constants.JSONKey.HTML));
            }

        } catch (Exception e) {
            System.err.println("Error reading subject");
            e.printStackTrace();
        }
    }


    public boolean sameCode(String code) {
        return this.code.equals(code);
    }

    /**
     * Accessing all the document from web takes long time, store it for better processing speed
     * @return Parsed Json object
     */
    public JSONObject toJSONObject() {
        // TODO FIXME ************THE JSONObject.Write will automatically parse all the fields to proper field name!!!
        // The base JSON object of the subject stored:
        //          {"code" : code, "info" : info(JSONObject)}
        JSONObject base = new JSONObject();
        base.put(Constants.JSONKey.CODE, code);

        // Detailed information of subject:
        //          {"linkInfo" : linkInfo, "parsedInfo" : parsedInfo}
        JSONObject info = new JSONObject();
        JSONObject linkInfo = linkInfoJson();
        JSONObject parsedInfo = parsedInfoJson();

        info.put(Constants.JSONKey.PARSED_INFO, parsedInfo);
        info.put(Constants.JSONKey.LINK_INFO, linkInfo);

        base.put(Constants.JSONKey.INFO, info);

        return base;
    }

    /**
     * Parse the link part (raw data) of the subject
     * @return parsed JSON object of link properties
     */
    private JSONObject linkInfoJson() {
        // Link info, and stored string of html of links
        //          {linkType("overview", "prerequisites") : linkJSONObject}
        JSONObject linkInfo = new JSONObject();
        JSONObject overviewJson = linkJson(Constants.ParsingConstant.OVERVIEW, overviewLink, overviewDocument);
        JSONObject eligibilityJson = linkJson(Constants.ParsingConstant.ELIGIBILITY,
                eligibilityLink, eligibilityDocument);

        linkInfo.put(Constants.ParsingConstant.OVERVIEW, overviewJson);
        linkInfo.put(Constants.ParsingConstant.ELIGIBILITY, eligibilityJson);
        return linkInfo;
    }

    /**
     * Parse the parsed/processed part (raw data) of the subject
     * @return parsed JSON object of processed properties
     */
    private JSONObject parsedInfoJson() {
        JSONObject parsedInfo = new JSONObject();

        parsedInfo.put(Constants.JSONKey.CODE, code);
        parsedInfo.put(Constants.JSONKey.NAME, name);
        parsedInfo.put(Constants.JSONKey.CREDIT, credit);
        parsedInfo.put(Constants.JSONKey.YEAR, year.toString());
        parsedInfo.put(Constants.JSONKey.LEVEL, subjectLevel);
        // TODO make parsedInfo richer

        return parsedInfo;
    }


    /**
     * Create a JSONObject based on the type, url, document provided
     * @param type Link type (overview, eligibility, etc...)
     * @param url  Link Content
     * @param document HTML document of the link
     * @return Formatted JSON object of the link
     */
    private static JSONObject linkJson(String type, String url, Document document) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(Constants.JSONKey.LINK_TYPE, type);
        jsonObject.put(Constants.JSONKey.URL, url);
        if (document != null) {
            jsonObject.put(Constants.JSONKey.HTML, document.toString());
        } else {
            jsonObject.put(Constants.JSONKey.HTML, Constants.NULL);
        }

        return jsonObject;
    }

    /**
     * Process the overview document, if overviewLink exist and overview document is not "null".
     * Then store it to various variables
     */
    public void processOverview() {
        // TODO refactor this part semi-duplicated codes, add condition of link to the condition field elegantly
        if (overviewDocument == null) {
            try {
                overviewDocument = Jsoup.connect(overviewLink).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (overviewDocument != null && !overviewDocument.text().equals(Constants.NULL)) {
            // Get the overview document if not fetched yet.

            processOverviewHeader();
            processOverviewBox();

        }
    }

    /**
     * Internal method used by processOverview to process the header to get subject name and subject credit
     */
    private void processOverviewHeader() {
        // TODO future make the overview extraction match approximate string
        try {
            Element overviewHeader = overviewDocument.getElementsByClass(Constants
                    .ParsingConstant.COURSE_HEADER_INNER).get(0);

            // Get the Html segment that only contains name and code
            String nameHtml = overviewHeader.getElementsByClass(Constants
                    .ParsingConstant.COURSE_HEADER_MAIN).get(0).html();
            Matcher matcher = Constants.ParsingConstant.COURSE_HEADER_SUBJECT_NAME_PATTERN.matcher(nameHtml);
            // If matcher found matches, output it
            if (matcher.matches()) {
                name = matcher.group(1);
                // Updated to non-greedy regex, the trailing space removing condition won't be needed
            } else {
                System.err.format("---------\n" +
                        "Regex found no matching name group\n" +
                        "    code: %s\n" +
                        "    name html:\n %s\n", code, nameHtml);
            }

            // Get the Html segment that contains credits points
            String pointHtml = overviewHeader.getElementsByClass(Constants
                    .ParsingConstant.COURSE_HEADER_DETAIL).html();
            matcher = Constants.ParsingConstant.COURSE_HEADER_CREDIT_PATTERN.matcher(pointHtml);
            // If matcher found matches, output it
            if (matcher.find()) {
                credit = Float.parseFloat(matcher.group(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processOverviewBox() {
        try {
            // Get year/campus/availability/etc...
            Element overviewBox = overviewDocument.getElementsByClass(Constants
                    .ParsingConstant.CLASS_OVERVIEW_BOX).get(0);
            // The information is all contained in table entries
            Elements rows = overviewBox.getElementsByTag(Constants.HTMLConstant.TR);
            for (Element row : rows) {
                Element rowHeader = row.child(0);
                Element rowCell = rowHeader.nextElementSibling();
                if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                        Constants.ParsingConstant.YEAR)) {
                    if (!HelperMethods.containsIgnoreCase(rowCell.text(),
                            Constants.ParsingConstant.NOT_AVAILABLE)) {
                        year = Year.parse(rowCell.text());
                    }
                } else if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                        Constants.ParsingConstant.SUBJECT_LEVEL)) {
                    subjectLevel = rowCell.text();
                } else if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                        Constants.ParsingConstant.CAMPUS)) {
                    campus = rowCell.text();
                } else if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                        Constants.ParsingConstant.AVAILABILITY)) {
                    // Availability are texts captured by div tags
                    // TODO deal with [Month - Online] type of availability, (month type and online)
                    for (Element timeElement : rowCell.children()) {
                        availability.add(timeElement.text());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Process the overview document, if eligibilityLink exist and eligibility document is not "null".
     * Then store it to various variables
     */
    public void processEligibility() {
        // TODO refactor this part semi-duplicated codes, add condition of link to the condition field elegantly
        if (eligibilityDocument == null) {
            try {
                eligibilityDocument = Jsoup.connect(eligibilityLink).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (eligibilityDocument != null && !eligibilityDocument.body().text().equals(Constants.NULL)) {
            processPrerequisite();
            processCorequisite();
            processProhibition();
        }
    }

    /**
     * Internal method used by process Eligibility to get prerequisite information
     */
    private void processPrerequisite() {
        // get the element, convert to just plain text
        Element prerequisitesElement = eligibilityDocument.getElementById(Constants.ParsingConstant.PREREQUISITES);
        try {
            String requisiteText = prerequisitesElement.text();
            // Match against matcher
            Matcher matcher = Constants.ParsingConstant.PREREQUISITE_PATTERN.matcher(requisiteText);

            // TODO deal with 3-D requirement, but not just buckets of requirements
            // TODO Current: (A) and (B or C) and (D) --> need to deal with --> (A and B) or (B and C)
            // All subject that already appeared, remove duplicated ones
            ArrayList<String> expressions = new ArrayList<>();
            // Collect all found matches
            while (matcher.find()) {
                expressions.add(matcher.group().toLowerCase());
            }

            // Prerequisite adding mode
            int AND_MODE = 0;
            int ONEOF_MODE = 1;
            int mode = AND_MODE;

            HashSet<String> appeared = new HashSet<>();
            HashSet<String> currentRequisites = new HashSet<>();
            // FIXME I have no idea if this works or not,
            // TODO And also this is far too basic for parsing the HTML which has extremely complicated rules
            // Explanation:
            // First strip the html off tags, leaving only plain text, since subject codes will always
            // show in plain text, then removing duplicated subject code. For some subjects includes additional
            // description about some prerequisite (which ideally should also be parsed.)
            // Then, the parsing assumes subject are by default separated by and relation. From then onwards, when token
            // "one of" is seen, this switches to "one of mode".
            // AND mode: All subjects will be stored in different HashSet to indicate all choices are needed
            // ONEOF mode: Subsequent subjects will be stored in the same HashSet to indicate, choosing either one will
            //             satisfy the requirement
            // (Side note: this assumes the subject description will always be in 'sum of product' form,
            //  which is "not always true")
            // Example prerequisite: [[A],[B],[C,D,E]] --> A,B,C | A,B,D | A,B,E all satisfies the prerequisite
            for (int i = 0; i < expressions.size(); i++) {
                String expression = expressions.get(i);
                switch (expression) {
                    case Constants.ParsingConstant.AND_1:
                    case Constants.ParsingConstant.AND_2:
                        mode = AND_MODE;
                        break;
                    case Constants.ParsingConstant.OR_1:
                        break;
                    case Constants.ParsingConstant.ONE_OF:
                        if (i > 0 && expressions.get(i - 1).equals(Constants.ParsingConstant.AND_1)) {
                            prerequisites.add(currentRequisites);
                            currentRequisites = new HashSet<>();
                        }
                        mode = ONEOF_MODE;
                        break;
                    default:
                        // prevent duplication
                        if (appeared.contains(expression)) {
                            expressions.remove(i);
                            i--;
                            break;
                        }
                        appeared.add(expression);

                        if (currentRequisites.size() < 1) {
                            currentRequisites.add(expression);
                        } else if ((mode == ONEOF_MODE) ||
                                (expressions.get(i - 1).equals(Constants.ParsingConstant.OR_1))) {
                            currentRequisites.add(expression);
                        } else {
                            prerequisites.add(currentRequisites);
                            currentRequisites = new HashSet<>();
                            currentRequisites.add(expression);
                        }
                }
            }
            if (currentRequisites.size() > 0) {
                prerequisites.add(currentRequisites);
            }
            System.out.println(code);
            System.out.println(name);
            System.out.println(prerequisites.toString());
        } catch (Exception e) {
            System.out.println(code);
            System.out.println(eligibilityDocument.toString());
            e.printStackTrace();
        }
    }

    /**
     * Internal method used by process Eligibility to get corequisite information
     */
    private void processCorequisite() {

    }

    /**
     * Internal method used by process Eligibility to get not allowed subject information
     */
    private void processProhibition() {

    }

    /**
     * Read the subject information JSON file and return a ArrayList containing all read information about the file
     * @param fName path and name of the file to be read
     * @return ArrayList created by parsing the JSON file
     */
    public static ArrayList<Subject> readSubjectsJSON(String fName, int num) {
        num = num != -1 ? num : Integer.MAX_VALUE;

        ArrayList<Subject> out = new ArrayList<>();
        try (BufferedReader buffer = new BufferedReader(new FileReader(fName))) {
            JSONObject jsonObject = new JSONObject(buffer.lines()
                    .collect(Collectors.joining()));

            int count = 0;
            for (String key : jsonObject.keySet()) {
                // System.out.println(key);
                out.add(new Subject(jsonObject.getJSONObject(key)));

                // Break the loop if count incremented to m.
                count++;
                if (count >= num) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    public String getOverviewLink() {
        return overviewLink;
    }

    public ArrayList<HashSet<String>> getPrerequisites() {
        return prerequisites;
    }

    public HashSet<String> getCorequisites() {
        return corequisites;
    }

    public HashSet<String> getProhibitions() {
        return prohibitions;
    }

    public String getCode() {
        return code;
    }


    /**
     * We use code to identify a subject
     */
    @Override
    public int hashCode() {
        return code.hashCode();
    }

    /**
     * We use code to identify a subject
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Subject) {
            return ((Subject) obj).code.equals(code);
        }
        return false;
    }

    @Override
    public String toString() {
        // Null value
        return String.format("code: %s, name: %s, year: %s", code, name, year.toString());
    }
}
