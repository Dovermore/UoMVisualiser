package Crawler;

import Subject.Subject;
import Util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Implementation of queue used in searching of the web. Note: the order is not unique.
 */
public class Pages implements PageQueue {

    // All search pages to go over
    private ArrayList<String> searches;

    // Stored subject page information
    private HashMap<String, SubjectHolder> subjects;

    // All previously enqueued pages
    private ArrayList<String> seen;

    // Max number of subject to get from web (easier for auto-testing)
    private int numSubject;

    /**
     * Initialise the page object with correct PREFIX/postfix for different pages
     */
    public Pages(Year year, int num) {
        // We set num to MAX integer if num is -1 (default), else if given num, we use the supported value.
        numSubject = num != -1 ? num : Integer.MAX_VALUE;
        searches = new ArrayList<>();
        subjects = new HashMap<>();
        seen = new ArrayList<>();
        LinkProcessor.setYear(year);
        searches.add(LinkProcessor.getInstance().getRoot());
    }

    /**
     * Initialise the page object with correct PREFIX/postfix for different pages, with default year 2019
     */
    public Pages() {
        this(Constants.DEFAULT_YEAR, -1);
    }

    /**
     * Enqueue a link to the queue, will only enqueue links that matched either search or subject page regex.
     * @param link The link given to enqueue
     */
    @Override
    public void enqueue(String link) {
        LinkProcessor linkProcessor = LinkProcessor.getInstance();

        // previously added, return directly
        if (seen.contains(link)) {
            return;
        } else {
            // System.out.format("Verifying : %s", link);
            seen.add(link);
        }


        if (linkProcessor.isSearchPage(link)) {
            System.out.format("SearchPage : %s\n", link);
            searches.add(link);
        } else if (linkProcessor.isSubjectPage(link) && numSubject > subjects.size()) {
            System.out.format("SubjectPage : %s\n", link);

            // get subjectHolder code
            String code = linkProcessor.getSubjectCode(link);
            // if subjectHolder not already existed, create it and put into map
            if (subjects.get(code) == null) {
                subjects.put(code, new SubjectHolder(code));
            }

            SubjectHolder subjectHolder = subjects.get(code);
            subjectHolder.addLink(link);
        }
    }

    @Override
    public int size() {
        return Math.min(searches.size(), numSubject - subjects.size());
    }

    /**
     * dequeue the next element to read.
     * @return the next link to parse
     */
    @Override
    public String dequeue() {
        // Dequeue only if the subject field is not filled or there are still un-searched link in searches
        if (searches.size() > 0 && subjects.size() < numSubject) {
            return searches.remove(0);
        }
        return null;
    }


    public void saveSubjectsToCSV(String fName, int num) {
        // Assign the number of record to save to be length of array if new value is not given, else use the given value
        num = num != -1 ? num : subjects.size();

        File file = HelperMethods.createFile(fName);
        if (file != null) {
            try {

                FileWriter fileWriter = new FileWriter(file);
                CSVPrinter csvPrinter = new CSVPrinter(fileWriter,
                        CSVFormat.DEFAULT.withHeader("code", "overview",
                                "eligibility", "assessment",
                                "datesTimes", "furtherInfo", "print"));

                for (With.Index<SubjectHolder> index : With.index(subjects.values())) {
                    CSVObject csvObject = index.value().toCSVObject();
                    csvPrinter.printRecord(csvObject);
                    if (index.index() >= num - 1) {
                        break;
                    }
                }

                csvPrinter.flush();
                csvPrinter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.format("Failed to write to file: %s", fName);
        }
    }
}
