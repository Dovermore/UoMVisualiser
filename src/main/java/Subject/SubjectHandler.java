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
            Map.of(Constants.OVERVIEW, new OverviewHandler(),
                    Constants.ELIGIBILITY, new EligibilityHandler());

    private ArrayList<SubjectData> subjectData = new ArrayList<>();

    public SubjectHandler(String fileName) {
        loadSubjects(fileName);
    }

    public SubjectHandler(ArrayList<SubjectData> subjectData) {
        this.subjectData = subjectData;
    }

    public SubjectHandler() {
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
    public void saveSubjects(String fileName) {
        saveSubjects(fileName, subjectData.size());
    }

    /**
     * Save with given number of records to certain files. This is intended to create smaller files for test purpose.
     * @param num the number of records to be stored
     * @param fileName The file to store to
     */
    public void saveSubjects(String fileName, int num) {
        // Set num to be less or equal to subject.size()
        num = (num > subjectData.size() || num == 0) ? subjectData.size() : num;

        File file = HelperMethods.createFile(fileName);

        if (file != null) {
            try {
                FileWriter fWriter = new FileWriter(fileName);

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
            System.err.format("Failed to save to file: %s", fileName);
        }
    }

    /**
     * Read the subject information JSON file and return a ArrayList containing all read information absubjectData the file
     * @param fileName path and name of the file to be read
     * @param format The format to read the file with currently support "json", "csv
     */
    public void loadSubjects(String fileName, String format) {
        // reset the subject data
        subjectData = new ArrayList<>();
            if (format.equalsIgnoreCase("json")) {
                // If file format is specified as json
                loadSubjectsFromJSON(fileName);
            } else if (format.equalsIgnoreCase("csv")) {
                // If file format is specified as csv
                loadSubjectsFromCSV(fileName);
            }
    }

    private void loadSubjectsFromJSON(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            JSONObject jsonObject = new JSONObject(bufferedReader.lines().collect(Collectors.joining()));

            for (String key : jsonObject.keySet()) {
                // System.subjectData.println(key);
                subjectData.add(new SubjectData(jsonObject.getJSONObject(key)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSubjectsFromCSV(String fileName) {
        try (FileReader fileReader = new FileReader(fileName)) {
            // If file format is specified as csv
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader());

            for (CSVRecord record : csvParser) {
                subjectData.add(new SubjectData(record));
            }
            csvParser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the subject information JSON file and return a ArrayList containing all read information absubjectData the file
     * @param fileName path and name of the file to be read
     */
    public void loadSubjects(String fileName) {
        if (fileName.toLowerCase().endsWith(".json")) {
            loadSubjects(fileName, "json");
        } else if (fileName.toLowerCase().endsWith(".csv")) {
            loadSubjects(fileName, "csv");
        }
    }


}
