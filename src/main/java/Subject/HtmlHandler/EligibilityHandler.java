package Subject.HtmlHandler;

import Subject.ParsedData;
import Util.Constants;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.regex.Matcher;

public class EligibilityHandler implements BaseHandler {
    private HashSet<String> prerequisites = new HashSet<>();
    private HashSet<String> corequisites = new HashSet<>();
    private HashSet<String> prohibitions = new HashSet<>();

    /**
     * Process the overview document, if eligibilityLink exist and eligibility document is not "null".
     * Then store it to various variables
     */
    @Override
    public void parse(Document html, ParsedData parsedData) {
        if (html == null || html.body().text().equals(Constants.NULL)) {
            return;
        }

        processPrerequisite(html);
        processCorequisite(html);
        processProhibition(html);
    }

    /**
     * Internal method used by parse Eligibility to get prerequisite information
     */
    private void processPrerequisite(Document html) {
        // get the element, convert to just plain text
        Element prerequisitesElement = html.getElementById(Constants.ParsingConstant.PREREQUISITES);
        try {
            String requisiteText = prerequisitesElement.text();
            // Match against matcher
            Matcher matcher = Constants.ParsingConstant.CODE_PATTERN.matcher(requisiteText);
            // Collect all found matches
            while (matcher.find()) {
                prerequisites.add(matcher.group().toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Internal method used by parse Eligibility to get corequisite information
     */
    private void processCorequisite(Document html) {

    }

    /**
     * Internal method used by parse Eligibility to get not allowed subject information
     */
    private void processProhibition(Document html) {

    }
}



// Archived Old code
//    /**
//     * Internal method used by parse Eligibility to get prerequisite information
//     */
//    private void processPrerequisite(Document html) {
//        // get the element, convert to just plain text
//        Element prerequisitesElement = html.getElementById(Constants.ParsingConstant.PREREQUISITES);
//        try {
//            String requisiteText = prerequisitesElement.text();
//            // Match against matcher
//            Matcher matcher = Constants.ParsingConstant.PREREQUISITE_PATTERN.matcher(requisiteText);
//
//            // nTODO deal with 3-D requirement, but not just buckets of requirements
//            // nTODO Current: (A) and (B or C) and (D) --> need to deal with --> (A and B) or (B and C)
//            // All subject that already appeared, remove duplicated ones
//            ArrayList<String> expressions = new ArrayList<>();
//            // Collect all found matches
//            while (matcher.find()) {
//                expressions.add(matcher.group().toLowerCase());
//            }
//
//            // Prerequisite adding mode
//            int AND_MODE = 0;
//            int ONEOF_MODE = 1;
//            int mode = AND_MODE;
//
//            HashSet<String> appeared = new HashSet<>();
//            HashSet<String> currentRequisites = new HashSet<>();
//            // nFIXME I have no idea if this works or not,
//            // nTODO And also this is far too basic for parsing the HTML which has extremely complicated rules
//            // Explanation:
//            // First strip the html off tags, leaving only plain text, since subject codes will always
//            // show in plain text, then removing duplicated subject code. For some subjects includes additional
//            // description about some prerequisite (which ideally should also be parsed.)
//            // Then, the parsing assumes subject are by default separated by and relation. From then onwards, when token
//            // "one of" is seen, this switches to "one of mode".
//            // AND mode: All subjects will be stored in different HashSet to indicate all choices are needed
//            // ONEOF mode: Subsequent subjects will be stored in the same HashSet to indicate, choosing either one will
//            //             satisfy the requirement
//            // (Side note: this assumes the subject description will always be in 'sum of product' form,
//            //  which is "not always true")
//            // Example prerequisite: [[A],[B],[C,D,E]] --> A,B,C | A,B,D | A,B,E all satisfies the prerequisite
//            for (int i = 0; i < expressions.size(); i++) {
//                String expression = expressions.get(i);
//                switch (expression) {
//                    case Constants.ParsingConstant.AND_1:
//                    case Constants.ParsingConstant.AND_2:
//                        mode = AND_MODE;
//                        break;
//                    case Constants.ParsingConstant.OR_1:
//                        break;
//                    case Constants.ParsingConstant.ONE_OF:
//                        if (i > 0 && expressions.get(i - 1).equals(Constants.ParsingConstant.AND_1)) {
//                            prerequisites.add(currentRequisites);
//                            currentRequisites = new HashSet<>();
//                        }
//                        mode = ONEOF_MODE;
//                        break;
//                    default:
//                        // prevent duplication
//                        if (appeared.contains(expression)) {
//                            expressions.remove(i);
//                            i--;
//                            break;
//                        }
//                        appeared.add(expression);
//
//                        if (currentRequisites.size() < 1) {
//                            currentRequisites.add(expression);
//                        } else if ((mode == ONEOF_MODE) ||
//                                (expressions.get(i - 1).equals(Constants.ParsingConstant.OR_1))) {
//                            currentRequisites.add(expression);
//                        } else {
//                            prerequisites.add(currentRequisites);
//                            currentRequisites = new HashSet<>();
//                            currentRequisites.add(expression);
//                        }
//                }
//            }
//            if (currentRequisites.size() > 0) {
//                prerequisites.add(currentRequisites);
//            }
//            System.out.println(code);
//            System.out.println(name);
//            System.out.println(prerequisites.toString());
//        } catch (Exception e) {
//            System.out.println(code);
//            System.out.println(html.toString());
//            e.printStackTrace();
//        }
//    }
