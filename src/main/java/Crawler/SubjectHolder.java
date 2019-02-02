package Crawler;

import Util.CSVObject;
import Util.CSVable;
import Util.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Hold the info crawled from search page
 */
public class SubjectHolder implements CSVable {
    // All subjects listed
    private static ArrayList<String> allSubjects = new ArrayList<>();

    // Basic subject information
    private String code;
    private String facultyCode;
    private String yearCode;

    // Links of all fields
    private String overviewLink = Constants.NULL_STRING;
    private String eligibilityLink = Constants.NULL_STRING;
    private String assessmentLink = Constants.NULL_STRING;
    private String datesTimesLink = Constants.NULL_STRING;
    private String furtherInfoLink = Constants.NULL_STRING;
    private String printLink = Constants.NULL_STRING;


    public SubjectHolder(String code) {
        this.code = code;
        allSubjects.add(code);
        this.facultyCode = code.substring(0, 4);
        this.yearCode = code.substring(4, 9);
    }

    public void addLink(String link) {
        if (link.contains(code + Constants.LinkConstant.OVERVIEW_POSTFIX)) {
            overviewLink = link;

            try {
                // Load HTML
                Document document = Jsoup.connect(link).get();

                // read links on the HTML
                Elements allPages = document.select("a[href]");

                // check and add all links relative to the subject
                for (Element page : allPages) {
                    String pageString = page.attr("abs:href");

                    if (pageString.contains(code + Constants.LinkConstant.ELIGIBILITY_POSTFIX)) {
                        eligibilityLink = pageString;
                    } else if (pageString.contains(code + Constants.LinkConstant.ASSESSMENT_POSTFIX)) {
                        assessmentLink = pageString;
                    } else if (pageString.contains(code + Constants.LinkConstant.DATES_TIMES_POSTFIX)) {
                        datesTimesLink = pageString;
                    } else if (pageString.contains(code + Constants.LinkConstant.FURTHER_INFO_POSTFIX)) {
                        furtherInfoLink = pageString;
                    } else if (pageString.contains(code + Constants.LinkConstant.PRINT_POSTFIX)) {
                        printLink = pageString;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.err.format("%s: invalid link: %s", code, link);
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SubjectHolder) {
            return code.equals(((SubjectHolder) obj).code);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s\n, %b, %b, %b, %b, %b, %b\n",
                code,
                overviewLink != null,
                eligibilityLink != null,
                assessmentLink != null,
                datesTimesLink != null,
                furtherInfoLink != null,
                printLink != null
        );
    }

    public CSVObject toCSVObject() {
        CSVObject csvObject = new CSVObject();
        csvObject.add(code);
        csvObject.add(overviewLink);
        csvObject.add(eligibilityLink);
        csvObject.add(assessmentLink);
        csvObject.add(datesTimesLink);
        csvObject.add(furtherInfoLink);
        csvObject.add(printLink);
        return csvObject;
    }

    public String getCode() {
        return code;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public String getYearCode() {
        return yearCode;
    }

    public String getOverviewLink() {
        return overviewLink;
    }

    public String getEligibilityLink() {
        return eligibilityLink;
    }

    public String getAssessmentLink() {
        return assessmentLink;
    }

    public String getDatesTimesLink() {
        return datesTimesLink;
    }

    public String getFurtherInfoLink() {
        return furtherInfoLink;
    }

    public String getPrintLink() {
        return printLink;
    }
}
