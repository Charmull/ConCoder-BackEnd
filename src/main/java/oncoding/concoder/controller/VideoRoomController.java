package oncoding.concoder.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.ChatDTO.DummyResponse;
import oncoding.concoder.dto.ChatDTO.ExitResponse;
import oncoding.concoder.dto.ChatDTO.SessionRequest;
import oncoding.concoder.dto.ChatDTO.SessionResponse;
import oncoding.concoder.dto.ChatDTO.UserResponse;
import oncoding.concoder.model.Room;
import oncoding.concoder.model.User;
import oncoding.concoder.service.ChattingService;
import org.json.simple.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideoRoomController {

  // 테스트용 세션 리스트.

  private final ChattingService chattingService;
  private final SimpMessagingTemplate template;


  private SessionResponse sessionResponse;

  // 실시간으로 들어온 세션 감지하여 전체 세션 리스트 반환
  @MessageMapping("/video/joined-room-info")
  //@SendTo("/sub/video/joined-room-info")
  private SessionResponse joinRoom(JSONObject ob) {

    log.info("@MessageMapping(\"/video/joined-room-info\")",ob.toString());

    //final UUID roomId, @RequestBody final SessionRequest request
    UUID roomId = UUID.fromString((String)ob.get("roomId"));
    SessionRequest request = new SessionRequest(UUID.fromString((String)ob.get("userId")),(String)ob.get("sessionId"));


    sessionResponse = chattingService.enter(roomId, request);
    template.convertAndSend("/sub/rooms/" + roomId, sessionResponse);
    template.convertAndSend("/sub/video/joined-room-info",sessionResponse);

    log.info("convertAndSend to /sub/video/joined-room-info",sessionResponse);
    return sessionResponse;

  }


  // caller의 정보를 다른 callee들에게 쏴준다.
  @MessageMapping("/video/caller-info")
  //@SendTo("/sub/video/caller-info")
  private Map<String, Object> caller(JSONObject ob) {

    // caller의 정보를 소켓으로 쏴준다.
    Map<String, Object> data = new HashMap<>();
    data.put("from", ob.get("from"));
    data.put("to", ob.get("toCall"));
    data.put("signal", ob.get("signal")); //그냥 data
    data.put("type", ob.get("type")); //sdp 인지 ice인지 구분

    template.convertAndSend("/sub/video/caller-info",data);

    log.info("convertAndSend to /sub/video/caller-info",data);

    return data;
  }

  // caller와 callee의 signaling을 위해 callee 정보를 쏴준다.
  @MessageMapping("/video/callee-info")
  //@SendTo("/sub/video/callee-info")
  private Map<String, Object> answerCall(JSONObject ob) {

    // accepter의 정보를 소켓으로 쏴준다.
    Map<String, Object> data = new HashMap<>();
    data.put("from", ob.get("from"));
    data.put("to", ob.get("toCall"));
    data.put("signal", ob.get("signal")); //그냥 data

    template.convertAndSend("/sub/video/callee-info",data);
    log.info("convertAndSend to /sub/video/callee-info",data);
    return data;
  }




  @EventListener
  private void handleSessionConnected(SessionConnectEvent event) {

  }

  @EventListener
  private void handleSessionDisconnect(SessionDisconnectEvent event) {

    String removedID = "";

    int room_users_cnt = sessionResponse.getUserResponses().size();
    List<UserResponse> users = sessionResponse.getUserResponses();

    for (UserResponse userResponse : users) {
      if (userResponse.getSessionId().equals(event.getSessionId())) {
        removedID = userResponse.getSessionId();
        users.remove(userResponse);
        break;
      }
    }

    //채팅방에서도 나감
    ExitResponse response = chattingService.exit(event.getSessionId());
    template.convertAndSend("/sub/rooms/" + response.getRoomId(), response.getSessionResponse());
    log.info("convertAndSend to /sub/rooms/getRoomid",response.getSessionResponse());
    
    //종료 세션 id 전달.
    template.convertAndSend("/sub/video/close-session", removedID);
    log.info("convertAndSend to /sub/video/close-session",removedID);

  }

  @PostMapping("/dummy")
  public ResponseEntity<DummyResponse> createDummyRoomAndUser() {
    DummyResponse response = chattingService.createDummy();
    return ResponseEntity.ok(response);
  }

}
