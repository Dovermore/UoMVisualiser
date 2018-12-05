package Crawler;

import Util.PageQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Basic crawling unit for search through the university handbook
 */
public class Crawler {
    // Max links to visit
    private final static int MAX_TO_VISIT = 15;

    // default starting point, prefix to all links
    private final static String INITIAL_LINK = "https://handbook.unimelb.edu.au/2019/subjects";

    // visited links
    private PageQueue queue;

    public Crawler(PageQueue queue) {
        this.queue = queue;
    }

    public void parsePage(String link) {
        try {
            // Load HTML
            Document document = Jsoup.connect(link).get();

            // read links on the HTML
            Elements allPages = document.select("a[href]");

            // check and add all links
            for (Element page : allPages) {
                String pageString = page.attr("abs:href");

                queue.enqueue(pageString);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void crawl() {
        while (queue.size() > 0) {
            parsePage(queue.dequeue());
        }
    }
}
