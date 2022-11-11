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
import oncoding.concoder.dto.ProblemDto.CreateRequest;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CrawlingService {
    private static final String BOJ_URL = "https://www.acmicpc.net/problem/";
    private static final String SOLVEDAC_URL = "https://solved.ac/api/v3/problem/lookup?problemIds=";
    private static final int CRAWLING_COUNT = 10;

    public Document connect(int number) throws IOException {
        Connection connection = Jsoup.connect(BOJ_URL+number);
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
        Document document = connect(number);
        Map<String, String> content = new HashMap<>();

        Elements descriptions = document.select("#problem_description");
        content.put("description", joinElementsText(descriptions, "\n"));
        Elements inputs = document.select("#problem_input");
        content.put("input", joinElementsText(inputs, "\n"));
        Elements outputs = document.select("#problem_output");
        content.put("output", joinElementsText(outputs, "\n"));

        return content;
    }

    public List<ProblemDto.CreateRequest> getRawProblems(int startNumber) throws IOException {
        String problemIds = getProblemIds(startNumber);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<ProblemDto.CreateRequest>> response = restTemplate.exchange(
            SOLVEDAC_URL+problemIds,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>(){});
        List<ProblemDto.CreateRequest> rawProblems = response.getBody();
        return rawProblems;
    }

}
