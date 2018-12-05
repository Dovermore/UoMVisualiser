import Crawler.Crawler;
import Subject.LinkRecordProcessor;
import WebPage.Pages;

public class Main {
    private static int mode = 1;

    public static void main(String[] args) {
        if (mode == 0) {
            Pages pages = new Pages(2019);
            Crawler crawler = new Crawler(pages);
            crawler.crawl();
            pages.writeToFile();
        } else {
            LinkRecordProcessor linkRecordProcessor = new LinkRecordProcessor();
            linkRecordProcessor.saveSubjects();
        }
    }
}
