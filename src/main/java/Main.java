import Crawler.Crawler;
import Subject.LinkRecordProcessor;
import Subject.Subject;
import Util.Constants;
import WebPage.Pages;

public class Main {
    private static final int MODE_LINK = 0;
    private static final int MODE_SUBJECT = 1;
    private static final int MODE_ANALYSE = 2;




    public static void main(String[] args) {
        int mode = 2;

        if (mode == MODE_LINK) {
            // Get all subject overview (main) links and store in CSV file
            Pages pages = new Pages(2019);
            Crawler crawler = new Crawler(pages);
            crawler.crawl();
            pages.writeToFile();
        } else if (mode == MODE_SUBJECT){
            // Get Store all HTML pages to JSON format
            LinkRecordProcessor linkRecordProcessor = new LinkRecordProcessor();
            linkRecordProcessor.saveSubjects();
        } else if (mode == MODE_ANALYSE) {
            // Read HTML from JSON format and try to parse into sensible format
            LinkRecordProcessor linkRecordProcessor = new LinkRecordProcessor(
                    Subject.readSubjectsFile(Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_INFO_JSON)
            );
            linkRecordProcessor.processSubjects();
        }
    }
}
