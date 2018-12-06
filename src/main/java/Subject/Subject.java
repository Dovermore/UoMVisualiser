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

public class Subject {

    // TODO make object for most of the attributes, so it's easier to link and filter semesters
    // Parsed contents
    private String code;
    private String name;
    private String subjectLevel;
    private Year year;
    private float credit;
    private String campus;
    private ArrayList<String> availability = new ArrayList<>();
    private String subjectOverview;

    // The prerequisites can have 'or' condition. Use a 2D structure instead
    private ArrayList<HashSet<String>> prerequisites = new ArrayList<>();
    private HashSet<String> corequisites = new HashSet<>();
    private HashSet<String> prohibitions = new HashSet<>();

    // Prerequisite adding mode
    private static int AND_MODE = 0;
    private static int OR_MODE = 1;

    // Links
    private String overviewLink;
    private String eligibilityLink;


    // HTML documents
    private Document overviewDocument;
    private Document eligibilityDocument;



    public Subject(String overviewLink, String code) {
        this.code = code;
        this.overviewLink = overviewLink;
    }

    public Subject(CSVRecord record) {
        this.code = record.get(0);
        this.overviewLink = record.get(1);
        this.eligibilityLink = record.get(2);

        System.out.println(code);
        System.out.println(overviewLink);
        System.out.println(eligibilityLink);

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
        JSONObject base = new JSONObject();
        base.put(Constants.JSONKey.CODE, code);

        JSONObject info = new JSONObject();

        JSONObject linkInfo = new JSONObject();
        // TODO make parsed info richer

        JSONObject parsedInfo = new JSONObject();
        parsedInfo.put(Constants.JSONKey.CODE, code);

        JSONObject overviewJson = linkJson(Constants.ParsingConstant.OVERVIEW, overviewLink, overviewDocument);
        JSONObject eligibilityJson = linkJson(Constants.ParsingConstant.ELIGIBILITY,
                eligibilityLink, eligibilityDocument);

        linkInfo.put(Constants.ParsingConstant.OVERVIEW, overviewJson);
        linkInfo.put(Constants.ParsingConstant.ELIGIBILITY, eligibilityJson);

        info.put(Constants.JSONKey.PARSED_INFO, parsedInfo);
        info.put(Constants.JSONKey.LINK_INFO, linkInfo);

        base.put(Constants.JSONKey.INFO, info);

        return base;
    }


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

    public void processOverview() {
        if (overviewDocument != null && !overviewDocument.text().equals(Constants.NULL)) {
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
                    if (name.charAt(name.length()-1) == ' ') {
                        name = name.substring(0, name.length()-1);
                    }
                    System.out.println(name);
                } else {
                    System.out.println("No match :" + nameHtml);
                }

                // Get the Html segment that contains credits points
                String pointHtml = overviewHeader.getElementsByClass(Constants
                        .ParsingConstant.COURSE_HEADER_DETAIL).html();
                matcher = Constants.ParsingConstant.COURSE_HEADER_CREDIT_PATTERN.matcher(pointHtml);
                // If matcher found matches, output it
                if (matcher.find()) {
                    credit = Float.parseFloat(matcher.group(1));
                    System.out.println(credit);
                }

                // Get year/campus/availability/etc...
                Element overviewBox = overviewDocument.getElementsByClass(Constants
                        .ParsingConstant.CLASS_OVERVIEW_BOX).get(0);
                Elements rows = overviewBox.getElementsByTag(Constants.HTMLConstant.TR);
                for (Element row : rows) {
                    Element rowHeader = row.child(0);
                    Element rowCell = rowHeader.nextElementSibling();
                    if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                            Constants.ParsingConstant.YEAR)) {
                        if (!HelperMethods.containsIgnoreCase(rowCell.text(),
                                Constants.ParsingConstant.NOT_AVAILABLE)) {
                            year = Year.parse(rowCell.text());
                            System.out.println(year.toString());
                        }
                    } else if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                            Constants.ParsingConstant.SUBJECT_LEVEL)) {
                            subjectLevel = rowCell.text();
                            System.out.println(subjectLevel);
                    } else if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                            Constants.ParsingConstant.CAMPUS)) {
                            campus = rowCell.text();
                            System.out.println(campus);
                    } else if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                            Constants.ParsingConstant.AVAILABILITY)) {
                        // Availability are texts captured by div tags
                        // TODO deal with [Month - Online] type of availability, (month and online)
                        for (Element timeElement : rowCell.children()) {
                            availability.add(timeElement.text());
                        }
                        System.out.println(availability.toString());
                    }
                }
            } catch (Exception e) {
                System.out.println(overviewDocument.getElementsByClass(Constants
                        .ParsingConstant.CLASS_OVERVIEW_BOX).get(0));

                e.printStackTrace();
            }
        }
    }

    public void processEligibility() {
        if (eligibilityDocument == null) {
            try {
                eligibilityDocument = Jsoup.connect(eligibilityLink).get();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        if (eligibilityDocument != null && !eligibilityDocument.body().text().equals(Constants.NULL)) {
            processPrerequisite();
            processCorequisite();
            processProhibition();
        }
    }

    public static ArrayList<Subject> readSubjectsFile(String fName) {
        ArrayList<Subject> out = new ArrayList<>();
        try (BufferedReader buffer = new BufferedReader(new FileReader(fName))) {
            JSONObject jsonObject = new JSONObject(buffer.lines()
                    .collect(Collectors.joining()));

            for (String key : jsonObject.keySet()) {
                // System.out.println(key);
                out.add(new Subject(jsonObject.getJSONObject(key)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    private void processPrerequisite() {
        // get the element, convert to just plain text
        Element prerequisitesElement = eligibilityDocument.getElementById(Constants.ParsingConstant.PREREQUISITES);
        try {
            String requisiteText = prerequisitesElement.text();
            // Match against matcher
            Matcher matcher = Constants.ParsingConstant.PREREQUISITE_PATTERN.matcher(requisiteText);

            int mode = AND_MODE;
            HashSet<String> currentRequisites = new HashSet<>();

            // TODO deal with 3-D requirement, but not just buckets of requirements
            // TODO Current: (A) and (B or C) and (D) --> need to deal with --> (A and B) or (B and C)
            while (matcher.find()) {
                // TODO THIS PART need serious improvement!!!!!!!!!!!!!!
                // TODO THIS PART need serious ERROR correction!!!!!!!!!!!!!!
//                switch (matcher.group().toLowerCase()) {
//                    // And Case
//                    case Constants.ParsingConstant.AND_1:
//                    case Constants.ParsingConstant.AND_2:
//                        mode = AND_MODE;
//                        prerequisites.add(currentRequisites);
//                        currentRequisites = new HashSet<>();
//                        break;
//                    // Or case
//                    case Constants.ParsingConstant.OR_1:
//                    case Constants.ParsingConstant.OR_2:
//                        mode = OR_MODE;
//                        break;
//                    // Subject code case
//                    default:
//                        currentRequisites.add(matcher.group());
//                        System.out.println(matcher.group());
//                        if (mode == AND_MODE) {
//                            prerequisites.add(currentRequisites);
//                            currentRequisites = new HashSet<>();
//                        } else if (mode == OR_MODE) {
//                            // do nothing
//                        }
//                }
            }
            // If it's "AND" mode, the bucket is already added
            if (mode == OR_MODE) {
                prerequisites.add(currentRequisites);
            }
            System.out.println(prerequisites.toString());
        } catch (Exception e) {
            System.out.println(code);
            System.out.println(eligibilityDocument.toString());
            e.printStackTrace();
        }
    }

    private void processCorequisite() {

    }

    private void processProhibition() {

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

    @Override
    public int hashCode() {
        return overviewLink.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Subject) {
            return ((Subject) obj).overviewLink.equals(overviewLink);
        }
        return false;
    }
}
