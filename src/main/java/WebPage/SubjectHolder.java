package WebPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Hold the info crawled from search page
 */
public class SubjectHolder {
    // All subjects listed
    private static ArrayList<String> allSubjects = new ArrayList<>();

    // Field postfix
    private static String overviewPostfix = "";
    private static String eligibilityPostfix = "/eligibility-and-requirements";
    private static String assessmentPostfix = "/assessment";
    private static String datesTimesPostfix = "/dates-times";
    private static String furtherInfoPostfix = "/further-information";
    private static String printPostfix = "/print";

    // Basic subject information
    private String code;
    private String facultyCode;
    private String yearCode;

    // Links of all fields
    private String overviewLink = "null";
    private String eligibilityLink = "null";
    private String assessmentLink = "null";
    private String datesTimesLink = "null";
    private String furtherInfoLink = "null";
    private String printLink = "null";


    public SubjectHolder(String code) {
        this.code = code;
        allSubjects.add(code);
        this.facultyCode = code.substring(0, 4);
        this.yearCode = code.substring(4, 9);
    }

    public void addLink(String link) {
        if (link.contains(code + overviewPostfix)) {
            overviewLink = link;

            try {
                // Load HTML
                Document document = Jsoup.connect(link).get();

                // read links on the HTML
                Elements allPages = document.select("a[href]");

                // check and add all links relative to the subject
                for (Element page : allPages) {
                    String pageString = page.attr("abs:href");

                    if (pageString.contains(code + eligibilityPostfix)) {
                        eligibilityLink = pageString;
                    } else if (pageString.contains(code + assessmentPostfix)) {
                        assessmentLink = pageString;
                    } else if (pageString.contains(code + datesTimesPostfix)) {
                        datesTimesLink = pageString;
                    } else if (pageString.contains(code + furtherInfoPostfix)) {
                        furtherInfoLink = pageString;
                    } else if (pageString.contains(code + printPostfix)) {
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

    public ArrayList<String> toList() {
        ArrayList<String> list = new ArrayList<>();
        list.add(code);
        list.add(overviewLink);
        list.add(eligibilityLink);
        list.add(assessmentLink);
        list.add(datesTimesLink);
        list.add(furtherInfoLink);
        list.add(printLink);
        return list;
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
