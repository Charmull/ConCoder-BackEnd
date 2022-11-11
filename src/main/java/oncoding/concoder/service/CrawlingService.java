package oncoding.concoder.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class CrawlingService {
    static final String BOJ_URL = "https://www.acmicpc.net/problem/1018";

    public Document connect() throws IOException {
        Connection connection = Jsoup.connect(BOJ_URL);
        Document document = connection.get();
        return document;
    }

    public String joinElementsText(Elements elements, String seperator) {
        StringBuilder builder = new StringBuilder();
        for (Element element : elements) {
            builder.append(element.text());
            builder.append(seperator);
        }
        return builder.toString();
    }

    public Map<String, String> getContent() throws IOException {
        Document document = connect();
        Map<String, String> content = new HashMap<>();

        Elements descriptions = document.select("#problem_description");
        content.put("description", joinElementsText(descriptions, "\n"));
        Elements inputs = document.select("#problem_input");
        content.put("description", joinElementsText(inputs, "\n"));
        Elements outputs = document.select("#problem_output");
        content.put("description", joinElementsText(outputs, "\n"));

        return content;
    }

}
