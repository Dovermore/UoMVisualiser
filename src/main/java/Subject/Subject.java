package Subject;

import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class Subject {
    private String code;

    private String overviewLink;
    private String eligibilityLink;
    private Document overviewDocument;
    private Document eligibilityDocument;

    private ArrayList<String> prerequisite;
    private ArrayList<String> corequisite;
    private ArrayList<String> prohibition;

    public Subject(String overviewLink, String code) {
        this.code = code;
        this.overviewLink = overviewLink;
    }

    public Subject(CSVRecord record) {
        this.code = record.get(0);
        this.overviewLink = record.get(1);
        this.eligibilityLink = record.get(2);

        System.out.println(code);
        System.out.println(overviewLink);
        System.out.println(eligibilityLink);

        try {
            if (!overviewLink.equals("null")) {
                overviewDocument = Jsoup.connect(overviewLink).get();
            }
            if (!eligibilityLink.equals("null")) {
                eligibilityDocument = Jsoup.connect(eligibilityLink).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean sameCode(String code) {
        return this.code.equals(code);
    }

    /**
     * Accessing all the document from web takes long time, store it for better processing speed
     * @return Parsed Json object
     */
    public JSONObject toJSONObject() {
        JSONObject base = new JSONObject();
        base.put("code", code);

        JSONObject info = new JSONObject();

        JSONObject linkInfo = new JSONObject();
        // TODO make parsed info richer

        JSONObject parsedInfo = new JSONObject();
        parsedInfo.put("code", code);

        JSONObject overviewJson = linkJson("overview", overviewLink, overviewDocument);
        JSONObject eligibilityJson = linkJson("eligibility", eligibilityLink, eligibilityDocument);

        linkInfo.put("overview", overviewJson);
        linkInfo.put("eligibility", eligibilityJson);

        info.put("parsedInfo", parsedInfo);
        info.put("linkInfo", linkInfo);

        base.put("info", info);

        return base;
    }


    private static JSONObject linkJson(String type, String url, Document document) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("type", type);
        jsonObject.put("url", url);
        jsonObject.put("document", document.toString());

        return jsonObject;
    }

    public void processOverview() {

    }

    public void processEligibility() {
        try {
            eligibilityDocument = Jsoup.connect(eligibilityLink).get();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        processPrerequisite();
        processCorequisite();
        processProhibition();
    }

    private void processPrerequisite() {

    }

    private void processCorequisite() {

    }

    private void processProhibition() {

    }

    public String getOverviewLink() {
        return overviewLink;
    }

    public ArrayList<String> getPrerequisite() {
        return prerequisite;
    }

    public ArrayList<String> getCorequisite() {
        return corequisite;
    }

    public ArrayList<String> getProhibition() {
        return prohibition;
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return overviewLink.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Subject) {
            return ((Subject) obj).overviewLink.equals(overviewLink);
        }
        return false;
    }

}
