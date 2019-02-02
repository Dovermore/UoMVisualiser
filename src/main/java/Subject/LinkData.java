package Subject;

import Util.Constants;
import Util.HelperMethods;
import Util.JSONable;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class LinkData extends BaseData implements JSONable {
    private String link = Constants.NULL_STRING;
    private Document html = Constants.NULL_HTML;

    public LinkData(String code, String link) {
        super(code);
        this.link = link;
    }

    public LinkData(String code) {
        super(code);
    }

    public LinkData(JSONObject jsonObject) {
        super(jsonObject);
        String htmlString = jsonObject.getString(Constants.JSONKey.HTML);
        html = Jsoup.parse(htmlString);
        link = jsonObject.getString(Constants.JSONKey.LINK);
    }

    /**
     * Fetch the html and return if successful
     * @return if the connection is successful
     */
    public boolean fetchHtmlFromLink() {
        if (!HelperMethods.isNull(link) && HelperMethods.isNull(html)) {
            try {
                System.out.format("Fetching link for %s:\n" +
                        "        %s\n", getCode(), link);
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
