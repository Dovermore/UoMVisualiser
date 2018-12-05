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

//            int i = 0;

            for (CSVRecord record : csvParser) {
//                i++;
                subjects.add(new Subject(record));
//                if (i > 10) {
//                    break;
//                }
            }
            csvParser.close();
            fReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkRecordProcessor() {
        this(Constants.fPath + Constants.linkFName);
    }

    public void processSubjects() {

    }

    public void saveSubjects() {
        try {
            FileWriter fWriter = new FileWriter(Constants.fPath + Constants.subjectFName);

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
