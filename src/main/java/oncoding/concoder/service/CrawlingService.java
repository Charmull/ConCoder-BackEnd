package oncoding.concoder.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.dto.ProblemDto;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrawlingService {
    private static final String BOJ_URL = "https://www.acmicpc.net/problem/";
    private static final String SOLVEDAC_URL = "https://solved.ac/api/v3/problem/lookup?problemIds=";
    private static final int CRAWLING_COUNT = 10;

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

    public String getProblemIds(int startNumber) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i<CRAWLING_COUNT; i++) {
            builder.append(startNumber+i);
            if (i==CRAWLING_COUNT-1) break;
            builder.append(",");
        }
        return builder.toString();
    }

    public Map<String, String> getContent(int number) throws IOException {
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

    public List<ProblemDto.CreateRequest> getRawProblems(int startNumber) throws IOException {
        String problemIds = getProblemIds(startNumber);
        URL url = new URL(SOLVEDAC_URL+problemIds);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(connection.getContent());

        List<ProblemDto.CreateRequest> rawProblems = mapper.readValue(jsonInput, new TypeReference<>(){});
        return rawProblems;
    }

}
