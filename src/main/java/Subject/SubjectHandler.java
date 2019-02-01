package Subject;

import Subject.HtmlHandler.BaseHandler;
import Subject.HtmlHandler.EligibilityHandler;
import Subject.HtmlHandler.OverviewHandler;
import Util.Constants;
import Util.HelperMethods;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class SubjectHandler {
    private static Map<String, BaseHandler> handlerPairs =
            Map.of(Constants.HTMLConstant.OVERVIEW, new OverviewHandler(),
                    Constants.HTMLConstant.ELIGIBILITY, new EligibilityHandler());

    private ArrayList<SubjectData> subjectData = new ArrayList<>();

    public SubjectHandler(String fileName) {
        try {
            FileReader fReader = new FileReader(fileName);
            CSVParser csvParser = new CSVParser(fReader, CSVFormat.DEFAULT.withHeader());

            for (CSVRecord record : csvParser) {
                subjectData.add(new SubjectData(record));
            }
            csvParser.close();
            fReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SubjectHandler(ArrayList<SubjectData> subjectData) {
        this.subjectData = subjectData;
    }

    public SubjectHandler() {
        this(Constants.FileConstant.F_PATH + Constants.FileConstant.SUBJECT_LINK_CSV);
    }

    public void processSubjects() {
        for (SubjectData subjectData : this.subjectData) {
            for (Map.Entry<String, BaseHandler> handlePair : handlerPairs.entrySet()) {
                subjectData.processEntry(handlePair.getKey(), handlePair.getValue());
            }
        }
    }

    /**
     * Default save option
     */
    public void saveSubjects(String fName) {
        saveSubjects(fName, subjectData.size());
    }

    /**
     * Save with given number of records to certain files. This is intended to create smaller files for test purpose.
     * @param num the number of records to be stored
     * @param fName The file to store to
     */
    public void saveSubjects(String fName, int num) {
        // Set num to be less or equal to subject.size()
        num = (num > subjectData.size() || num == 0) ? subjectData.size() : num;

        File file = HelperMethods.createFile(fName);

        if (file != null) {
            try {
                FileWriter fWriter = new FileWriter(fName);

                JSONObject subjectsJson = new JSONObject();

                for (SubjectData subjectData : this.subjectData.subList(0, num)) {
                    subjectsJson.put(subjectData.getCode(), subjectData.toJSONObject());
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

    /**
     * Read the subject information JSON file and return a ArrayList containing all read information absubjectData the file
     * @param fName path and name of the file to be read
     */
    public void loadSubjectsJSON(String fName) {
        subjectData = new ArrayList<>();
        try (BufferedReader buffer = new BufferedReader(new FileReader(fName))) {
            JSONObject jsonObject = new JSONObject(buffer.lines()
                    .collect(Collectors.joining()));

            for (String key : jsonObject.keySet()) {
                // System.subjectData.println(key);
                subjectData.add(new SubjectData(jsonObject.getJSONObject(key)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
