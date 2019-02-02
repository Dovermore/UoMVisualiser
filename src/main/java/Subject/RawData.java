package Subject;

import Util.Constants;
import Util.JSONable;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

import java.util.Map;

public class RawData extends BaseData implements JSONable {
    private final Map<String, LinkData> linkDataGroup;

    RawData(String code) {
        super(code);
        linkDataGroup = Map.of(
                Constants.OVERVIEW, new LinkData(code),
                Constants.ELIGIBILITY, new LinkData(code),
                Constants.ASSESSMENT, new LinkData(code),
                Constants.DATES_TIMES, new LinkData(code),
                Constants.FURTHER_INFO, new LinkData(code),
                Constants.PRINT, new LinkData(code)
        );
    }

    /**
     * Initialise RawData from a CSVRecord
     * @param csvRecord The CSVRecord of corresponding subject
     */
    RawData(CSVRecord csvRecord) {
        this(csvRecord.get(0));
        for (int i = 0; i < 5; i++) {
            String entryName = Constants.ALL_ENTRIES.get(i);
            // Starts from 1 because 0 is code
            linkDataGroup.get(entryName).setLink(csvRecord.get(i+1));
        }
    }


    RawData(JSONObject jsonObject) {
        super(jsonObject);

        JSONObject linkDataGroupJSONObject = jsonObject.getJSONObject(Constants.JSONKey.LINK_DATA_GROUP);

        linkDataGroup = Map.of(
                Constants.OVERVIEW, new LinkData(linkDataGroupJSONObject.getJSONObject(Constants.OVERVIEW)),
                Constants.ELIGIBILITY, new LinkData(linkDataGroupJSONObject.getJSONObject(Constants.ELIGIBILITY)),
                Constants.ASSESSMENT, new LinkData(linkDataGroupJSONObject.getJSONObject(Constants.ASSESSMENT)),
                Constants.DATES_TIMES, new LinkData(linkDataGroupJSONObject.getJSONObject(Constants.DATES_TIMES)),
                Constants.FURTHER_INFO, new LinkData(linkDataGroupJSONObject.getJSONObject(Constants.FURTHER_INFO)),
                Constants.PRINT, new LinkData(linkDataGroupJSONObject.getJSONObject(Constants.PRINT))
        );
    }

    public Map<String, LinkData> getLinkDataGroup() {
        return linkDataGroup;
    }
}
