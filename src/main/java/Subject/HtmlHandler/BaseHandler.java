package Subject.HtmlHandler;

import Subject.ParsedData;
import org.jsoup.nodes.Document;

public interface BaseHandler {
    void parse(Document html, ParsedData parsedData);
}
