package Subject;

import Subject.HtmlHandler.BaseHandler;
import Util.Constants;

import Util.JSONable;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.util.Map;


// TODO VERY important, the getXXXXX method will be used by library to determine the parsed json object,
// TODO Making new class of info and link info will automatically convert current json structure to the desired structure!
// TODO This file has TOO MANY LINES, need to delegate functions to component class. Think of a way of using delegation to write further refactor
public class SubjectData extends Data implements JSONable {
    private ParsedData parsedData;
    private RawData rawData;

    /**
     * Initialise the subject just from code
     * @param code SubjectData Code
     */
    public SubjectData(String code) {
        super(code);
        parsedData = new ParsedData(code);
        rawData = new RawData(code);
    }

    /**
     * Initialise the subject from a CSVRecord
     * @param record The CSV record storing all links
     */
    public SubjectData(CSVRecord record) {
        this(record.get(0));
        rawData = new RawData(record);
        System.out.println();
    }

    public SubjectData(JSONObject jsonObject) {
        super(jsonObject.getString(Constants.JSONKey.CODE));
        JSONObject rawDataJson = jsonObject.getJSONObject("rawData");
        JSONObject parsedDataJson = jsonObject.getJSONObject("parsedData");
        rawData = new RawData(rawDataJson);
        parsedData = new ParsedData(parsedDataJson);
    }

    /**
     * Try to fetch the HTML and store in LinkData if the link data has valid link
     * @param entry The entry to fetch
     */
    private Document fetchRawHtml(String entry) {
        Map<String, LinkData> linkDataGroup = rawData.getLinkDataGroup();
        if (linkDataGroup.keySet().contains(entry) && linkDataGroup.get(entry) != null) {
            LinkData linkData = linkDataGroup.get(entry);
            linkData.fetchHtmlFromLink();
            return linkData.getHtml();
        } else {
            return null;
        }
    }


    public void processEntry(String entry, BaseHandler handler) {
        Document html = fetchRawHtml(entry);
        if (html == null) {
            System.err.format("Unable to fetch html for:\n            %s : %s\n", toString(), entry);
            return;
        }
        handler.parse(html, parsedData);
    }

    @Override
    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    @Override
    public String toString() {
        // Null value
        return String.format("code: %s, name: %s, year: %s", getCode(), parsedData.getName(),
                parsedData.getYear().toString());
    }
}
