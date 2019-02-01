package Crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Basic crawling unit for search through the university handbook
 */
public class Crawler {
    // default starting point, PREFIX to all links
    private final static String INITIAL_LINK = "https://handbook.unimelb.edu.au/2019/subjects";

    // visited links
    private SubjectQueue subjectQueue;

    public Crawler(SubjectQueue subjectQueue) {
        this.subjectQueue = subjectQueue;
    }

    public void parsePage(String link, int maxSubjects) {
        try {
            // Load HTML
            Document document = Jsoup.connect(link).get();

            // read links on the HTML
            Elements allPages = document.select("a[href]");

            // check and add all links
            for (Element page : allPages) {
                String pageString = page.attr("abs:href");

                subjectQueue.enqueue(pageString);

                if (subjectQueue.getHolderHashMap().size() >= maxSubjects) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void crawl(int maxSubjects) {
        while (subjectQueue.size() > 0 && subjectQueue.getHolderHashMap().size() < maxSubjects) {
            parsePage(subjectQueue.dequeue(), maxSubjects);
        }
    }

    public void crawl() {
        crawl(Integer.MAX_VALUE);
    }
}
