package Subject;

import Util.Constants;
import Util.JSONable;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

import java.util.Map;

public class RawData extends Data implements JSONable {
    private final Map<String, LinkData> linkDataGroup = Map.of(
            Constants.HTMLConstant.OVERVIEW, new LinkData(),
            Constants.HTMLConstant.ELIGIBILITY, new LinkData(),
            Constants.HTMLConstant.ASSESSMENT, new LinkData(),
            Constants.HTMLConstant.DATES_TIMES, new LinkData(),
            Constants.HTMLConstant.FURTHER_INFO, new LinkData(),
            Constants.HTMLConstant.PRINT, new LinkData()
    );

    RawData(String code) {
        super(code);
    }

    /**
     * Initialise RawData from a CSVRecord
     * @param csvRecord The CSVRecord of corresponding subject
     */
    RawData(CSVRecord csvRecord) {
        this(csvRecord.get(0));
        for (int i = 0; i < 5; i++) {
            String entryName = Constants.HTMLConstant.ALL_ENTRIES.get(i);
            // Starts from 1 because 0 is code
            linkDataGroup.get(entryName).setLink(csvRecord.get(i+1));
        }
    }


    RawData(JSONObject jsonObject) {
        super(jsonObject);
        // TODO other detail
    }

    public Map<String, LinkData> getLinkDataGroup() {
        return linkDataGroup;
    }
}
