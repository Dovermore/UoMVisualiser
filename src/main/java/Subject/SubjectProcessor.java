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

public class SubjectProcessor {
    private ArrayList<Subject> subjects = new ArrayList<>();

    public SubjectProcessor(String fileName) {
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

    public SubjectProcessor(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public SubjectProcessor() {
        this(Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_LINK_CSV);
    }

    public void processSubjects() {
        for (Subject subject : subjects) {
            subject.processOverview();
            subject.processEligibility();
        }
    }

    /**
     * Default save option
     */
    public void saveSubjects() {
        saveSubjects(subjects.size(), Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_INFO_JSON);
    }

    /**
     * Save with given number of records to certain files. This is intended to create smaller files for test purpose.
     * @param num the number of records to be stored
     * @param fName The file to store to
     */
    public void saveSubjects(int num, String fName) {
        // Set num to be less or equal to subject.size()
        num = num < subjects.size() ? subjects.size() : num;

        try {
            FileWriter fWriter = new FileWriter(fName);

            JSONObject subjectsJson = new JSONObject();

            for (Subject subject : subjects.subList(0, num)) {
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
