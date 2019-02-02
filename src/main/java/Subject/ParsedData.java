package Subject;

import Util.Constants;
import Util.HelperMethods;
import Util.JSONable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Year;
import java.util.Arrays;
import java.util.HashSet;

public class ParsedData extends BaseData implements JSONable {
    private String name = Constants.NULL_STRING;
    private String overview = Constants.NULL_STRING;
    private String level = Constants.NULL_STRING;
    private Year year = Constants.NULL_YEAR;
    private float credit = 0.0f;
    private String campus = Constants.NULL_STRING;
    private HashSet<String> availability = new HashSet<>();
    private HashSet<String> prerequisites = new HashSet<>();
    private HashSet<String> corequisites = new HashSet<>();
    private HashSet<String> prohibitions = new HashSet<>();

    public ParsedData(String code) {
        super(code);
    }

    public ParsedData(JSONObject jsonObject) {
        super(jsonObject);
        //
        name = jsonObject.getString(Constants.JSONKey.NAME);
        overview = jsonObject.getString(Constants.JSONKey.OVERVIEW);
        level = jsonObject.getString(Constants.JSONKey.LEVEL);
        year = Year.parse(jsonObject.getString(Constants.JSONKey.YEAR));
        credit = jsonObject.getFloat(Constants.JSONKey.CREDIT);
        campus = jsonObject.getString(Constants.JSONKey.CAMPUS);
        //
        JSONArray jsonArray = jsonObject.getJSONArray(Constants.JSONKey.AVAILABILITY);
        availability.addAll(HelperMethods.jsonArrayToArrayList(jsonArray));
        jsonArray = (JSONArray) jsonObject.get(Constants.JSONKey.PREREQUISITES);
        prerequisites.addAll(HelperMethods.jsonArrayToArrayList(jsonArray));
        jsonArray = (JSONArray) jsonObject.get(Constants.JSONKey.COREQUISITES);
        corequisites.addAll(HelperMethods.jsonArrayToArrayList(jsonArray));
        jsonArray = (JSONArray) jsonObject.get(Constants.JSONKey.PROHIBITIONS);
        prohibitions.addAll(HelperMethods.jsonArrayToArrayList(jsonArray));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @SuppressWarnings("unchecked")
    public HashSet<String> getAvailability() {
        return (HashSet<String>) availability.clone();
    }

    public void addAvailability(String... availabilities) {
        this.availability.addAll(Arrays.asList(availabilities));
    }

    @SuppressWarnings("unchecked")
    public HashSet<String> getPrerequisites() {
        return (HashSet<String>) prerequisites.clone();
    }

    public void addPrerequisites(String... prerequisites) {
        this.prerequisites.addAll(Arrays.asList(prerequisites));
    }

    @SuppressWarnings("unchecked")
    public HashSet<String> getCorequisites() {
        return (HashSet<String>) corequisites.clone();
    }

    public void addCorequisites(String... corequisites) {
        this.corequisites.addAll(Arrays.asList(corequisites));
    }

    @SuppressWarnings("unchecked")
    public HashSet<String> getProhibitions() {
        return (HashSet<String>) prohibitions.clone();
    }

    public void addProhibitions(String... prohibitions) {
        this.prohibitions.addAll(Arrays.asList(prohibitions));
    }
}
