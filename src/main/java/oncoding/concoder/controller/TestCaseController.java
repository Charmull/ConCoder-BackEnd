package oncoding.concoder.controller;

import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.ChatDTO.MessageRequest;
import oncoding.concoder.dto.SnapshotDto;
import oncoding.concoder.service.TestCaseService;
import org.json.simple.JSONObject;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestCaseController {

  private final TestCaseService testCaseService;
  private final SimpMessagingTemplate template;

  /**
   * 전체 조회
   * @param roomId
   */
  @MessageMapping("/testcases/all/{roomId}")
  public void getTestCases(@DestinationVariable final String roomId) {

    Map<String,JSONObject> map =  testCaseService.getTestCases(roomId);
    template.convertAndSend("/sub/testcases/"+ roomId,map);
    log.info("after /sub/testcases/");
  }



  /**
   * 단건 조회
   * @param
   * @return
   */
  @MessageMapping("/testcases/single/{roomId}")
  public void getTestCase(@DestinationVariable final String roomId, JSONObject ob) {
    log.info("/testcases/single/"+roomId+": "+ob.get("testCaseId"));
    template.convertAndSend("/sub/testcases/single/"+ roomId ,  testCaseService.getTestCase(roomId, (String) ob.get("testCaseId")));
    log.info("after /testcases/single/");
  }

  /**
   * 새로운 테스트 케이스 저장
   * @param roomId
   */
  @MessageMapping("/testcases/create/{roomId}")
  public void createTestCases(@DestinationVariable final String roomId,JSONObject ob) {

    JSONObject res = testCaseService.createTestCase(roomId,ob);
    log.info("/testcases/create/"+roomId+": "+res);
    template.convertAndSend("/sub/testcases/create/"+ roomId,res);
    log.info("after /sub/testcases/create/");
  }


  /**
   * 테스트 케이스 수정
   * @param roomId
   */
  @MessageMapping("/testcases/modify/{roomId}")
  public void modifyTestCases(@DestinationVariable final String roomId,JSONObject ob) {

    String testCaseId = (String) ob.get("testCaseId");
    JSONObject newOb = new JSONObject();
    newOb.put("input",ob.get("input"));
    newOb.put("output",ob.get("output"));
    JSONObject res = testCaseService.modifyTestCase(roomId,testCaseId,newOb);
    log.info("/testcases/modify/"+roomId+": "+res);
    template.convertAndSend("/sub/testcases/modify/"+ roomId,res);
    log.info("after /sub/testcases/modify");
  }

  /**
   * 테스트 케이스 삭제
   * @param roomId
   * @param ob
   */
  @MessageMapping("/testcases/delete/{roomId}")
  public void deleteTestCases(@DestinationVariable final String roomId,JSONObject ob) {

    String testCaseId = (String) ob.get("testCaseId");

    testCaseService.deleteTestCase(roomId,testCaseId);
    log.info("/testcases/delete/"+roomId+": "+testCaseId);
    template.convertAndSend("/sub/testcases/delete/"+ roomId,testCaseId);
    log.info("after /sub/testcases/delete");
  }
  
  


}
