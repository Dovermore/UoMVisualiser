package Subject;

import Subject.HtmlHandler.BaseHandler;

import Util.Constants;
import Util.HelperMethods;
import Util.JSONable;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.util.Map;

public class SubjectData extends BaseData implements JSONable {
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
        super(jsonObject);
        JSONObject rawDataJson = jsonObject.getJSONObject(Constants.JSONKey.RAW_DATA);
        JSONObject parsedDataJson = jsonObject.getJSONObject(Constants.JSONKey.PARSED_DATA);
        rawData = new RawData(rawDataJson);
        parsedData = new ParsedData(parsedDataJson);
    }

    /**
     * Try to fetch the HTML and store in LinkData if the link data has valid link
     * @param entry The entry to fetch
     */
    private Document fetchRawHtml(String entry) {
        Map<String, LinkData> linkDataGroup = rawData.getLinkDataGroup();
        if (linkDataGroup.containsKey(entry) && linkDataGroup.get(entry) != null) {
            LinkData linkData = linkDataGroup.get(entry);
            linkData.fetchHtmlFromLink();
            return linkData.getHtml();
        } else {
            return null;
        }
    }


    public void processEntry(String entry, BaseHandler handler) {
        Document html = fetchRawHtml(entry);
        if (HelperMethods.isNull(html)) {
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

    public ParsedData getParsedData() {
        return parsedData;
    }

    public void setParsedData(ParsedData parsedData) {
        this.parsedData = parsedData;
    }

    public RawData getRawData() {
        return rawData;
    }

    public void setRawData(RawData rawData) {
        this.rawData = rawData;
    }
}
