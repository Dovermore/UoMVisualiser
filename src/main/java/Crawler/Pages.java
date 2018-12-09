package Crawler;

import Util.Constants;
import Util.PageQueue;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
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
            if (subjects.get(code) == null) {
                subjects.put(code, new SubjectHolder(code));
            }

            SubjectHolder subjectHolder = subjects.get(code);
            subjectHolder.addLink(link);
        }
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
    public Pages(int year) {
        searches = new ArrayList<>();
        subjects = new HashMap<>();
        seen = new ArrayList<>();

        searches.add(LinkProcessor.getInstance().getRoot());
    }

    public void writeToFile() {
        try {
            FileWriter fileWriter = new FileWriter(Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_LINK_CSV);
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter,
                    CSVFormat.DEFAULT.withHeader("code", "overview",
                            "eligibility", "assessment",
                            "datesTimes", "furtherInfo", "print"));
            for (SubjectHolder subjectHolder : subjects.values()) {
                csvPrinter.printRecord(subjectHolder.toList());
            }
            csvPrinter.flush();
            csvPrinter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
