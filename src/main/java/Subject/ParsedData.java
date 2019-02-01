package Subject;

import Util.Constants;
import Util.JSONable;
import org.json.JSONObject;

import java.time.Year;
import java.util.Arrays;
import java.util.HashSet;

public class ParsedData extends Data implements JSONable {
    private String name = Constants.NULL;
    private String overview = Constants.NULL;
    private String level = Constants.NULL;
    private Year year = Year.parse("0000");
    private float credit = 0.0f;
    private String campus = Constants.NULL;
    private HashSet<String> availability = new HashSet<>();
    private HashSet<String> prerequisites = new HashSet<>();
    private HashSet<String> corequisites = new HashSet<>();
    private HashSet<String> prohibitions = new HashSet<>();

    public ParsedData(String code) {
        super(code);
    }

    public ParsedData(JSONObject jsonObject) {
        super(jsonObject);
        // TODO further detail
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

    public void addAvailability(String... times) {
        availability.addAll(Arrays.asList(times));
    }

    @SuppressWarnings("unchecked")
    public HashSet<String> getPrerequisites() {
        return (HashSet<String>) prerequisites.clone();
    }

    public void addPrerequisites(String prerequisite) {
        this.prerequisites.add(prerequisite);
    }

    @SuppressWarnings("unchecked")
    public HashSet<String> getCorequisites() {
        return (HashSet<String>) corequisites.clone();
    }

    public void addCorequisites(String corequisite) {
        this.corequisites.add(corequisite);
    }

    @SuppressWarnings("unchecked")
    public HashSet<String> getProhibitions() {
        return (HashSet<String>) prohibitions.clone();
    }

    public void addProhibitions(String prohibition) {
        this.prohibitions.add(prohibition);
    }
}
