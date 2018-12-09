package Subject;

import Util.Constants;

import java.time.Year;
import java.util.ArrayList;

public class ParsedData {
    private String code = Constants.NULL;
    private String name = Constants.NULL;
    private String subjectLevel;
    private Year year = Year.parse("0000");
    private float credit;
    private String campus = Constants.NULL;
    private ArrayList<String> availability = new ArrayList<>();
    // TODO a proper subject overview is need to be extracted
    private String subjectOverview;
}
