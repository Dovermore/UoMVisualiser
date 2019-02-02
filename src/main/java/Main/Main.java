package Main;

import Crawler.Crawler;
import Crawler.SubjectQueue;
import Subject.SubjectHandler;
import Util.Constants;
import org.apache.commons.cli.*;

import java.time.Year;

/**
 * Main.Main of the program
 */
public class Main {
    // TODO clean up/ make better format of the cli argument
    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        CommandLine cl;
        try {
            cl = parser.parse(getClOptions(), args);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        // Specifies the version (year) of handbook to crawl
        Year year = Year.parse("2019");
        // Specifies if program should crawl links from scratch
        boolean crawlLink = false;
        // Specifies if program should download documents from given CSV file (Or default path if not specified
        boolean downloadHtml = false;
        // Specifies if program should process the document, either from URL or from local JSON file
        boolean parseHtml = false;
        // Location of input file (if using CSV or JSON as input source)
        String input = null;
        // Number of subject to read for CrawlUrl/DownloadHtml/ProcessHtml file (C for -c, D for -d, p for -P)
        // TODO Rework logic for reading from input
        int numSubjectC = -1;
        int numSubjectD = -1;
        int numSubjectP = -1;
        // Location of output file
        String output = null;
        // Specifies the number of records to store to output files
        int numToSave = -1;


        // TODO add try catch statement for parsing error.
        // NOTE: FOR OPTION c, g, p.         c takes priority over g and p and is not compatible with g and p
        // NOTE: while g and p can be chained in to a Pipeline
        for (Option option : cl.getOptions()) {
            System.out.println(option.getOpt());
            switch (option.getOpt()) {
                case "c":
                    crawlLink = true;
                    // If the number of subject to read hasn't been specified, parse it (overrides -g value).
                    if (option.getValue() != null) {
                        numSubjectC = Integer.parseInt(option.getValue());
                    }
                    break;
                case "y":
                    year = Year.parse(option.getValue());
                    break;
                case "g":
                    System.err.println("-g option will be replaced by -d option in future development");
                case "d":
                    downloadHtml = true;
                    // Only parse this field if -c field is not yet specified.
                    if (option.getValue() != null) {
                        numSubjectD = Integer.parseInt(option.getValue());
                    }
                    break;
                case "p":
                    parseHtml = true;
                    if (option.getValue() != null) {
                        numSubjectP = Integer.parseInt(option.getValue());
                    }
                    break;
                case "o":
                    output = option.getValue();
                    break;
                case "i":
                    input = option.getValue();
                    break;
                case "n":
                    numToSave = Integer.parseInt(option.getValue());
                    break;
            }
        }

        if (!(crawlLink || downloadHtml || parseHtml)) {
            return;
        }

        // If trying to crawl links
        if (crawlLink) {
            SubjectQueue subjectQueue = new SubjectQueue(year);
            Crawler crawler = new Crawler(subjectQueue);
            crawler.crawl(numSubjectC);
            if (output == null) {
                output = Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_LINK_CSV;
            }
            subjectQueue.saveSubjectsToCSV(output, numToSave);
            return;
        }

        // If not trying to crawl links
        SubjectHandler subjectHandler = null;
        // Download document from URL in CSV
        if (downloadHtml) {
            if (input == null) {
                input = Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_LINK_CSV;
            }
            subjectHandler = new SubjectHandler(input, numSubjectD);
        }

        // Process Html stored in JSON
        if (parseHtml) {
            // If didn't specify document, then means reading from local file.
            if (!downloadHtml) {
                if (input == null) {
                    input = Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_INFO_JSON;
                }
                subjectHandler = new SubjectHandler(input, numSubjectP);
            }
            subjectHandler.processSubjects();
        }

        if (output == null) {
            output = Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_INFO_JSON;
        }
        subjectHandler.saveSubjects(output, numToSave);
    }

    /**
     * Set the Command Line arguments
     * @return A collection of all the Option added
     */
    private static Options getClOptions() {
        Options options = new Options();

        Option crawlLinks = Option.builder("c")
                .numberOfArgs(1)
                .optionalArg(true)
                .longOpt("crawl-searches")
                .desc("Use if the program should crawl all links again from search page, " +
                        "Optionally pass a number to indicate the number of Subjects to crawl from Handbook")
                .build();
        options.addOption(crawlLinks);

        Option setYear = Option.builder("y")
                .numberOfArgs(1)
                .longOpt("crawl-year")
                .desc("if should craw the searches, specify this for the year to crawl, by default 2019")
                .build();
        options.addOption(setYear);

        Option getHtml = Option.builder("g")
                .numberOfArgs(1)
                .optionalArg(true)
                .longOpt("get-document")
                .desc("Use if the program should get HTML documents again, can specify optional argument of " +
                        "how many subjects' html it should download (This option will be replaced by -d in the future)")
                .build();
        options.addOption(getHtml);

        Option downloadHtml = Option.builder("d")
                .numberOfArgs(1)
                .optionalArg(true)
                .longOpt("download-document")
                .desc("Use if the program should get HTML documents again, can specify optional argument of " +
                        "how many subjects' html it should download")
                .build();
        options.addOption(downloadHtml);

        Option parseHtml = Option.builder("p")
                .numberOfArgs(1)
                .optionalArg(true)
                .longOpt("process-document")
                .desc("Parse the HTML document information, specify an optional argument of number of record to process")
                .build();
        options.addOption(parseHtml);

        Option output = Option.builder("o")
                .numberOfArgs(1)
                .longOpt("output-file")
                .desc("The file to output to, if there is any option suitable for output")
                .build();
        options.addOption(output);

        Option input = Option.builder("i")
                .numberOfArgs(1)
                .longOpt("input-file")
                .desc("The input file, if flag -g is specified should be a CSV file, " +
                        "else if -p is specified but not -g, then should be the JSON file.")
                .build();
        options.addOption(input);

        Option numRecord = Option.builder("n")
                .numberOfArgs(1)
                .longOpt("number-record")
                .desc("Number of records to write to output file, for create smaller file for faster testing.")
                .build();
        options.addOption(numRecord);

        // TODO Add -h Option and implement help

        return options;
    }
}
