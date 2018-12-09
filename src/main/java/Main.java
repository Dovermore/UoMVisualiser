import Crawler.Crawler;
import Subject.SubjectProcessor;
import Subject.Subject;
import Util.Constants;
import Crawler.Pages;

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
            SubjectProcessor subjectProcessor = new SubjectProcessor();
            subjectProcessor.saveSubjects();
        } else if (mode == MODE_ANALYSE) {
            // Read HTML from JSON format and try to parse into sensible format
            SubjectProcessor subjectProcessor = new SubjectProcessor(
                    Subject.readSubjectsFile(Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_INFO_JSON)
            );
            subjectProcessor.processSubjects();
        }
    }
}
