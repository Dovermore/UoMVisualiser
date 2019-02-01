package Subject.HtmlHandler;

import Subject.ParsedData;
import Util.Constants;
import Util.HelperMethods;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.Year;
import java.util.HashSet;
import java.util.regex.Matcher;

public class OverviewHandler implements BaseHandler {
    private String name;
//    private String code;
    private float credit;
    private Year year;
    private String level;
    private String campus;
    private HashSet<String> availability;


    @Override
    public void parse(Document html, ParsedData parsedData) {
        if (html == null || html.text().equals(Constants.NULL)) {
            return;
        }

        // Clean the internal variables
        name = Constants.NULL;
        year = Constants.DEFAULT_YEAR;
        level = Constants.NULL;
        campus = Constants.NULL;
        availability = new HashSet<>();

        processHeader(html);
        processBox(html);


        parsedData.setName(name);
        parsedData.setCampus(campus);
        parsedData.setCredit(credit);
        parsedData.setYear(year);
        parsedData.setLevel(level);
        parsedData.addAvailability(availability.toArray(new String[0]));
    }

    /**
     * Internal method used by parse to parse the header to get subject name and subject credit
     */
    private void processHeader(Document html) {
        // TODO future make the overview extraction match approximate string
        try {
            Element header = html.getElementsByClass(Constants
                    .ParsingConstant.COURSE_HEADER_INNER).get(0);

            processNameFromHeader(header);
            processCreditFromHeader(header);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processNameFromHeader(Element header) {
        // Get the Html segment that only contains name and code
        String nameHtml = header.getElementsByClass(Constants
                .ParsingConstant.COURSE_HEADER_MAIN).get(0).html();
        Matcher matcher = Constants.ParsingConstant.COURSE_HEADER_SUBJECT_NAME_PATTERN.matcher(nameHtml);
        // If matcher found matches, output it
        if (matcher.matches()) {
            name = matcher.group(1);
            // Updated to non-greedy regex, the trailing space removing condition won't be needed
        } else {
            System.err.format("---------\n" +
                    "Regex found no matching name group\n" +
                    "    name html:\n %s\n", nameHtml);
        }
    }

    private void processCreditFromHeader(Element header){
        // Get the Html segment that contains credits points
        String creditHtml = header.getElementsByClass(Constants
                .ParsingConstant.COURSE_HEADER_DETAIL).html();
        Matcher matcher = Constants.ParsingConstant.COURSE_HEADER_CREDIT_PATTERN.matcher(creditHtml);
        // If matcher found matches, output it
        if (matcher.find()) {
            credit = Float.parseFloat(matcher.group(1));
        }
    }


    private void processBox(Document html) {
        try {
            // Get year/campus/availability/etc...
            Element overviewBox = html.getElementsByClass(Constants
                    .ParsingConstant.CLASS_OVERVIEW_BOX).get(0);
            // The information is all contained in table entries
            Elements rows = overviewBox.getElementsByTag(Constants.HTMLConstant.TR);
            for (Element row : rows) {
                Element rowHeader = row.child(0);
                Element rowCell = rowHeader.nextElementSibling();
                processIfYearPresent(rowHeader, rowCell);
                processIfCampusPresent(rowHeader, rowCell);
                processIfLevelPresent(rowHeader, rowCell);
                processIfAvailabilityPresent(rowHeader, rowCell);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processIfYearPresent(Element rowHeader, Element rowCell) {
        if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                Constants.ParsingConstant.YEAR)) {
            if (!HelperMethods.containsIgnoreCase(rowCell.text(),
                    Constants.ParsingConstant.NOT_AVAILABLE)) {
                year = Year.parse(rowCell.text());
            }
        }
    }

    private void processIfLevelPresent(Element rowHeader, Element rowCell) {
        if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                Constants.ParsingConstant.SUBJECT_LEVEL)) {
            level = rowCell.text();
        }
    }

    private void processIfCampusPresent(Element rowHeader, Element rowCell) {
        if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                Constants.ParsingConstant.CAMPUS)) {
            campus = rowCell.text();
        }
    }

    private void processIfAvailabilityPresent(Element rowHeader, Element rowCell) {
        if (HelperMethods.containsIgnoreCase(rowHeader.text(),
                Constants.ParsingConstant.AVAILABILITY)) {
            // Availability are texts captured by div tags
            // TODO deal with [Month - Online] type of availability, (month type and online)
            for (Element timeElement : rowCell.children()) {
                availability.add(timeElement.text());
            }
        }
    }
}
