package oncoding.concoder.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.ChatDTO.MessageRequest;
import oncoding.concoder.service.ChattingService;
import org.json.simple.JSONObject;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ChattingController {

  private final SimpMessagingTemplate template; // @EnableWebSocketMessageBroker를 통해서 등록되는 bean이다.
  // 특정 Broker로 메시지를 전달한다.
  private final ChattingService chatService;


  @MessageMapping("/rooms/chat/{roomId}")
  public void chat(@DestinationVariable final String roomId, JSONObject ob) {
    log.info("/rooms/chat/"+roomId+" userId:  "+ob.get("userId"));
    log.info("/rooms/chat/"+roomId+" content: "+ob.get("content"));
    MessageRequest request = new MessageRequest(UUID.fromString((String) ob.get("userId")), (String)ob.get("content"));
    template.convertAndSend("/sub/rooms/chat/"+ roomId , chatService.sendMessage(request));
    log.info("after chatting convert and send");
  }




}
