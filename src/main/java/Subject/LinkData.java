package Subject;

import Util.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class LinkData {
    private String link = Constants.NULL;
    private Document html = null;

    public LinkData(String link) {
        this.link = link;
    }

    public LinkData() {}

    /**
     * Fetch the html and return if successful
     * @return if the connection is successful
     */
    public boolean fetchHtmlFromLink() {
        if (!link.equals(Constants.NULL) && html == null) {
            try {
                html = Jsoup.connect(link).get();
            } catch (IOException e) {
                System.err.format("Failed to retrieve html from %s", link);
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public String getLink() {
        return link;
    }

    Document getHtml() {
        return html;
    }

    public String getHtmlString() {
        return html.html();
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setHtml(Document html) {
        this.html = html;
    }
}
