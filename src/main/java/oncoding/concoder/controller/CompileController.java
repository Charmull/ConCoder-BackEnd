package oncoding.concoder.controller;

import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.service.CompileService;
import oncoding.concoder.service.TestCaseService;
import org.json.simple.JSONObject;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/compile")
@RequiredArgsConstructor
public class CompileController {
    private final CompileService compileService;
    private final TestCaseService testCaseService;

    @MessageMapping("/compile/{roomId}")
    public void compileByTestcases(@DestinationVariable final String roomId, JSONObject obj) {
        String code = (String) obj.get("code");
        // Redis 로부터 roomId 로 가져오기
        Map<String, JSONObject> testCases = testCaseService.getTestCases(roomId);

        for (Entry<String, JSONObject> testCase : testCases.entrySet()){
            String testCaseId = testCase.getKey();
            String input = (String) testCase.getValue().get("input");
            compileService.run(roomId, code, input, testCaseId);
        }

    }

}
