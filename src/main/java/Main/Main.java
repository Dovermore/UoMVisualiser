package Main;

import Crawler.Crawler;
import GraphComponent.UndirectedGraph;
import Subject.SubjectProcessor;
import Subject.Subject;
import Util.Constants;
import Crawler.Pages;
import org.apache.commons.cli.*;

import java.time.Year;

/**
 * Main.Main of the program
 */
public class Main {
    // TODO clean up/ make better format of the cli argument
    public static void main(String[] args) {
        UndirectedGraph ug = new UndirectedGraph();
        CommandLineParser parser = new DefaultParser();
        CommandLine cl;
        try {
            cl = parser.parse(getClOptions(), args);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        Year year = Year.parse("2019");
        int num = 0;
        boolean crawl = false;
        boolean get_document = false;
        boolean process_document = false;
        String output = null;
        String input = null;

        for (Option option : cl.getOptions()) {
            switch (option.getOpt()) {
                case "c":
                    crawl = true;
                    break;
                case "y":
                    year = Year.parse(option.getValue());
                    break;
                case "g":
                    get_document = true;
                    break;
                case "p":
                    process_document = true;
                    break;
                case "o":
                    output = option.getValue();
                    break;
                case "i":
                    input = option.getValue();
                    break;
                case "n":
                    num = Integer.parseInt(option.getValue());
                    break;
            }
        }

        // At least one option has to be true
        if (!(crawl || get_document || process_document)) {
            return;
        }

        if (crawl) {
            Pages pages = new Pages(year);
            Crawler crawler = new Crawler(pages);
            crawler.crawl();
            if (output == null) {
                output = Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_LINK_CSV;
            }
            pages.saveSubjectsToCSV(output, num);
            return;
        }


        SubjectProcessor subjectProcessor = null;
        if (get_document) {
            if (input == null) {
                input = Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_LINK_CSV;
            }
            subjectProcessor = new SubjectProcessor(input);
        }

        if (process_document) {
            if (!get_document) {
                if (input == null) {
                    input = Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_INFO_JSON;
                }
                subjectProcessor = new SubjectProcessor(Subject.readSubjectsJSON(input));
            }
            subjectProcessor.processSubjects();
        }

        if (output == null) {
            output = Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_INFO_JSON;
        }

        subjectProcessor.saveSubjects(output, num);
    }

    /**
     * Set the Command Line arguments
     * @return A collection of all the Option added
     */
    private static Options getClOptions() {
        Options options = new Options();

        Option crawlOption = Option.builder("c")
                .hasArg(false)
                .longOpt("crawl-searches")
                .desc("Use if the program should crawl all links again from search page")
                .build();
        options.addOption(crawlOption);

        Option crawlYear = Option.builder("y")
                .hasArg(true)
                .longOpt("crawl-year")
                .desc("if should craw the searches, specify this for the year to crawl, by default 2019")
                .build();
        options.addOption(crawlYear);

        Option docOption = Option.builder("g")
                .hasArg(false)
                .longOpt("get-document")
                .desc("Use if the program should get HTML documents again")
                .build();
        options.addOption(docOption);

        Option overviewParse = Option.builder("p")
                .hasArg(false)
                .longOpt("process-document")
                .desc("Parse the HTML document information")
                .build();
        options.addOption(overviewParse);

        Option output = Option.builder("o")
                .hasArg(true)
                .longOpt("output-file")
                .desc("The file to output to, if there is any option suitable for output")
                .build();
        options.addOption(output);

        Option input = Option.builder("i")
                .hasArg(true)
                .longOpt("input-file")
                .desc("The input file, if flag -g is specified should be a CSV file, " +
                        "else if -p is specified but not -g, then should be the JSON file.")
                .build();
        options.addOption(input);

        Option numRecord = Option.builder("n")
                .hasArg(true)
                .longOpt("number-record")
                .desc("Number of record to write to output file, for create smaller file for faster testing.")
                .build();
        options.addOption(input);

        return options;
    }
}
