package Crawler;

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
public class SubjectQueue implements Queue {

    // All search pages to go over
    private ArrayList<String> searches;

    // Stored subject page information
    private HashMap<String, SubjectHolder> holderHashMap;

    // All previously enqueued pages
    private ArrayList<String> seen;

    /**
     * Initialise the page object with correct PREFIX/postfix for different pages
     */
    public SubjectQueue(Year year) {
        searches = new ArrayList<>();
        holderHashMap = new HashMap<>();
        seen = new ArrayList<>();
        LinkProcessor.setYear(year);
        searches.add(LinkProcessor.getInstance().getRoot());
    }

    /**
     * Initialise the page object with correct PREFIX/postfix for different pages
     */
    public SubjectQueue() {
        this(Constants.DEFAULT_YEAR);
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
        } else if (linkProcessor.isSubjectPage(link)) {
            System.out.format("SubjectPage : %s\n", link);

            // get subjectHolder code
            String code = linkProcessor.getSubjectCode(link);
            // if subjectHolder not already existed, create it and put into map
            if (holderHashMap.get(code) == null) {
                holderHashMap.put(code, new SubjectHolder(code));
            }

            SubjectHolder subjectHolder = holderHashMap.get(code);
            subjectHolder.addLink(link);
        }
    }

    public HashMap<String, SubjectHolder> getHolderHashMap() {
        return holderHashMap;
    }

    @Override
    public int size() {
        return searches.size();
    }

    /**
     * dequeue the next element to read.
     * @return the next link to parse
     */
    @Override
    public String dequeue() {
        // Dequeue only if the subject field is not filled or there are still un-searched link in searches
        if (searches.size() > 0) {
            return searches.remove(0);
        }
        return null;
    }

    public void saveSubjectsToCSV(String fName) {
        saveSubjectsToCSV(fName, holderHashMap.size());
    }


    public void saveSubjectsToCSV(String fName, int num) {
        num = (num > holderHashMap.size() || num == 0) ? holderHashMap.size() : num;

        File file = HelperMethods.createFile(fName);
        if (file != null) {
            try {

                FileWriter fileWriter = new FileWriter(file);
                CSVPrinter csvPrinter = new CSVPrinter(fileWriter,
                        CSVFormat.DEFAULT.withHeader("code", "overview",
                                "eligibility", "assessment",
                                "datesTimes", "furtherInfo", "print"));

                for (With.Index<SubjectHolder> index : With.index(holderHashMap.values())) {
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
