package oncoding.concoder.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.dto.ChatDTO.MessageRequest;
import oncoding.concoder.service.ChattingService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ChattingController {

  private final SimpMessagingTemplate template; // @EnableWebSocketMessageBroker를 통해서 등록되는 bean이다.
  // 특정 Broker로 메시지를 전달한다.
  private final ChattingService chatService;

 /*
  @MessageMapping:
  @MessageMapping 설정한 url 매핑으로 클라이언트로부터 요청 메시지를 받으면
  @SendTo 설정한 구독 클라이언트들에게 메시지를 보냅니다.
 * */

  /**
   * 클라이언트에서 /publish/rooms/{roomId}/chat url로 메시지를 보내면,
   * ChatRequest의 채팅방 id를 이용하여 해당 방을 구독 중인 사용자들에게 메시지를 전달하도록 함
   * @param roomId
   * @param request
   */
  @MessageMapping("/rooms/chat/{roomId}")
  public void chat(@DestinationVariable final UUID roomId, final MessageRequest request) {
    template.convertAndSend("/sub/rooms/chat/"+ roomId , chatService.sendMessage(request));
  }





}
