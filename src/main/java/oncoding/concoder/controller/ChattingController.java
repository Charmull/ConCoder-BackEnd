package oncoding.concoder.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.dto.ChatDTO.DummyResponse;
import oncoding.concoder.dto.ChatDTO.ExitResponse;
import oncoding.concoder.dto.ChatDTO.MessageRequest;
import oncoding.concoder.dto.ChatDTO.SessionRequest;
import oncoding.concoder.service.ChattingService;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

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
   * 클라이언트에서 /publish/rooms/{roomId}/chat url로 메시지를 보내면, ChatRequest의 채팅방 id를 이용하여 해당 방을 구독 중인 사용자들에게 메시지를 전달하도록 함
   * @param roomId
   * @param request
   */
  @MessageMapping("/rooms/{roomId}/chat")
  public void chat(@DestinationVariable final UUID roomId, final MessageRequest request) {
    template.convertAndSend("/sub/rooms/" + roomId + "/chat", chatService.sendMessage(request));
  }


  /**
   * 채팅방 생성 및 세션 생성
   * @param roomId
   * @param request
   */
  @MessageMapping("/rooms/{roomId}")
  public void enter(@DestinationVariable final UUID roomId, final SessionRequest request) {
    template.convertAndSend("/sub/rooms/" + roomId, chatService.enter(roomId, request));
  }

  @EventListener //이벤트를 받아들이는 Event Listener,  파라미터로 해당 Event가 전달됨
  public void exit(final SessionDisconnectEvent event) {
    ExitResponse response = chatService.exit(event.getSessionId());
    template.convertAndSend("/sub/rooms/" + response.getRoomId(), response.getSessionResponse());
  }

//  @PostMapping("/dummy")
//  public ResponseEntity<DummyResponse> createDummyRoomAndUser() {
//    DummyResponse response = chatService.createDummy();
//    return ResponseEntity.ok(response);
//  }


}
