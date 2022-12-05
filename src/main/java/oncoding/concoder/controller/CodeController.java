package oncoding.concoder.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CodeController {
  private final SimpMessagingTemplate template;

  /**
   * 코드를 해당 룸 사용자들에게 공유
   * @param roomId
   * @param ob
   */
  @MessageMapping("/code/{roomId}")
  private void caller(@DestinationVariable final String roomId,JSONObject ob) {

    template.convertAndSend("/sub/code/"+roomId,ob);

    log.info("convertAndSend to /sub/code/"+roomId+" : ",ob);

  }

}
