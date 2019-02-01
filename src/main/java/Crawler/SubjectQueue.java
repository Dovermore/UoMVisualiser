package Crawler;

import Util.Constants;
import Util.HelperMethods;
import Util.Queue;
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
     * Enqueue a link to the queue, will only enqueue links that matched either search or subject page regex.
     * @param link The link given to enqueue
     */
    @Override
    public void enqueue(String link) {
        // previously added, return directly
        if (seen.contains(link)) {
            return;
        } else {
            // System.out.format("Verifying : %s", link);
            seen.add(link);
        }


        if (LinkProcessor.getInstance().isSearchPage(link)) {
            System.out.format("SearchPage : %s\n", link);
            searches.add(link);
        } else if (LinkProcessor.getInstance().isSubjectPage(link)) {
            System.out.format("SubjectPage : %s\n", link);

            // get subjectHolder code
            String code = LinkProcessor.getInstance().getSubjectCode(link);
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
        if (searches.size() > 0) {
            return searches.remove(0);
        }
        return null;
    }

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

                int counter = 0;
                for (SubjectHolder subjectHolder : holderHashMap.values()) {
                    csvPrinter.printRecord(subjectHolder.toCSVObject());
                    counter++;
                    if (counter >= num) {
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
