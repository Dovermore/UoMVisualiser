package Subject;

import Util.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LinkRecordProcessor {
    private ArrayList<Subject> subjects = new ArrayList<>();

    public LinkRecordProcessor(String fileName) {
        try {
            FileReader fReader = new FileReader(fileName);
            CSVParser csvParser = new CSVParser(fReader, CSVFormat.DEFAULT.withHeader());

            for (CSVRecord record : csvParser) {
                subjects.add(new Subject(record));
            }
            csvParser.close();
            fReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkRecordProcessor(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public LinkRecordProcessor() {
        this(Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_LINK_CSV);
    }

    public void processSubjects() {
        for (Subject subject : subjects) {
            subject.processOverview();
            subject.processEligibility();
        }
    }

    public void saveSubjects() {
        try {
            FileWriter fWriter = new FileWriter(Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_INFO_JSON);

            JSONObject subjectsJson = new JSONObject();

            for (Subject subject : subjects) {
                subjectsJson.put(subject.getCode(), subject.toJSONObject());
            }

            subjectsJson.write(fWriter);
            fWriter.flush();
            fWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
