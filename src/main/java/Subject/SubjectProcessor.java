package Subject;

import Util.Constants;
import Util.HelperMethods;
import Util.With;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SubjectProcessor {
    // All subject record are here
    private ArrayList<Subject> subjects = new ArrayList<>();


    public SubjectProcessor(String fileName, int num) {
        try {
            FileReader fReader = new FileReader(fileName);
            CSVParser csvParser = new CSVParser(fReader, CSVFormat.DEFAULT.withHeader());

            for (With.Index<CSVRecord> index : With.index(csvParser)) {
                subjects.add(new Subject(index.value()));

                if (index.index() >= num - 1) {
                    break;
                }
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


    public void processSubjects() {
        for (Subject subject : subjects) {
            subject.processOverview();
            subject.processEligibility();
        }
    }

    /**
     * Default save option
     */
    public void saveSubjects(String fName) {
        saveSubjects(fName, subjects.size());
    }

    /**
     * Save with given number of records to certain files. This is intended to create smaller files for test purpose.
     * @param num the number of records to be stored
     * @param fName The file to store to
     */
    public void saveSubjects(String fName, int num) {
        // Set num to be less or equal to subject.size()
        num = (num > subjects.size() || num == -1) ? subjects.size() : num;

        File file = HelperMethods.createFile(fName);

        if (file != null) {
            try {
                FileWriter fWriter = new FileWriter(fName);

                JSONObject subjectsJson = new JSONObject();

                // Only first num number of subject to write
                for (With.Index<Subject> index : With.index(subjects)) {
                    subjectsJson.put(index.value().getCode(), index.value().toJSONObject());

                    if (index.index() >= num - 1) {
                        break;
                    }
                }

                subjectsJson.write(fWriter);
                fWriter.flush();
                fWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.format("Failed to save to file: %s", fName);
        }
    }
}
