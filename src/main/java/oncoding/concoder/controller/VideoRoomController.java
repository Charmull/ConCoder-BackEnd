package oncoding.concoder.controller;

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
import oncoding.concoder.service.ChattingService;
import org.json.simple.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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


/*
  // 실시간으로 들어온 세션 감지하여 전체 세션 리스트 반환
  @MessageMapping("/video/joined-room-info")
  private SessionResponse joinRoom(@Header("simpSessionId") String sessionId,JSONObject ob) {
    
    //만약에 connectEvent 에서 sessioniD 받아올 수 있으면 HEADER가 아니라 그냥 ob 안에 담아서 처리하면 됨

    log.info("@MessageMapping(\"/video/joined-room-info\") sessionId: "+sessionId+" ");

    //final UUID roomId, @RequestBody final SessionRequest request
    UUID roomId = UUID.fromString((String)ob.get("roomId"));
    SessionRequest request = new SessionRequest(UUID.fromString((String)ob.get("userId")),sessionId);

    sessionResponse = chattingService.enter(roomId, request);
    //template.convertAndSend("/sub/rooms/" + roomId + sessionResponse);

    template.convertAndSend("/sub/video/joined-room-info",sessionResponse);


    log.info("convertAndSend to /sub/video/joined-room-info"+ sessionResponse);

    return sessionResponse;

  }
*/


  // 실시간으로 들어온 세션 감지하여 전체 세션 리스트 반환
  @MessageMapping("/video/joined-room-info")
  private SessionResponse joinRoom(JSONObject ob) {
    //만약에 connectEvent 에서 sessioniD 받아올 수 있으면 HEADER가 아니라 그냥 ob 안에 담아서 처리하면 됨
    String sessionId = (String) ob.get("sessionId");
    log.info("@MessageMapping(\"/video/joined-room-info\") sessionId: "+sessionId+" ");

    //final UUID roomId, @RequestBody final SessionRequest request
    UUID roomId = UUID.fromString((String)ob.get("roomId"));
    SessionRequest request = new SessionRequest(UUID.fromString((String)ob.get("userId")),sessionId);

    sessionResponse = chattingService.enter(roomId, request);
    //template.convertAndSend("/sub/rooms/" + roomId + sessionResponse);

    template.convertAndSend("/sub/video/joined-room-info",sessionResponse);

    log.info("convertAndSend to /sub/video/joined-room-info"+ sessionResponse);

    return sessionResponse;

  }

  // 실시간으로 들어온 세션 감지하여 전체 세션 리스트 반환
  @MessageMapping("/video/unjoined-room-info")
  private SessionResponse unJoinRoom(JSONObject ob) {
    //만약에 connectEvent 에서 sessioniD 받아올 수 있으면 HEADER가 아니라 그냥 ob 안에 담아서 처리하면 됨
    String sessionId = (String) ob.get("sessionId");
    log.info("@MessageMapping(\"/video/unjoined-room-info\") sessionId: "+sessionId+" ");

    String removedId = "";

    List<UserResponse> users = this.sessionResponse.getUserResponses();

    //현재 세션 목록에서 연결 끊은 유저 제외시킴
    for (UserResponse userResponse : users) {
      if (userResponse.getSessionId().equals(sessionId)) {
        removedId = userResponse.getSessionId();
        users.remove(userResponse);
        break;
      }
    }

    log.info("removed id: "+removedId);

    //채팅방에서도 나감
    ExitResponse response = chattingService.exit(removedId);
    //template.convertAndSend("/sub/rooms/" + response.getRoomId(), response.getSessionResponse());
    //log.info("convertAndSend to /sub/rooms/getRoomid",response.getSessionResponse());

    //종료 세션 id 전달.
   // template.convertAndSend("/sub/video/close-session", removedId);
    //log.info("convertAndSend to /sub/video/close-session",removedId);

    template.convertAndSend("/sub/video/unjoined-room-info",response);

    log.info("convertAndSend to /sub/video/unjoined-room-info"+ removedId);

    return this.sessionResponse;

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
    String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
  //connect하고 나서 클라이언트한테 본내주면 될 듯
    log.info("session connected sessionId: "+sessionId);

  }


  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    // 그냥 disconnect 결과로 프론트한테 removedID 쏴주면 아래처럼 나간 유저 정보 삭제 시키는 걸로 다시 요청 달라고 하기
    
    //정 안되면 그냥 끊을 때 프론트에서 sessionId 넘겨줘서 전처리 다 하고 끊어버리기,,, 어쩔 수 없움 ㅠ

    //String sessionId = event.getSessionId();
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = accessor.getSessionId();

    String removedId = "";

    //List<UserResponse> users = sessionResponse.getUserResponses();

    log.info("disconnect event Listener sessionId: "+sessionId);

    //현재 세션 목록에서 연결 끊은 유저 제외시킴
//    for (UserResponse userResponse : users) {
//      if (userResponse.getSessionId().equals(sessionId)) {
//        removedId = userResponse.getSessionId();
//        users.remove(userResponse);
//        break;
//      }
//    }
//

    //log.info("disconnect event Listener removed id: "+removedId);



    //채팅방에서도 나감
//    ExitResponse response = chattingService.exit(removedId);
//    //template.convertAndSend("/sub/rooms/" + response.getRoomId(), response.getSessionResponse());
//    //log.info("convertAndSend to /sub/rooms/getRoomid",response.getSessionResponse());
//
//    //종료 세션 id 전달.
//    template.convertAndSend("/sub/video/close-session", removedId);
//    log.info("convertAndSend to /sub/video/close-session",removedId);

  }

  @PostMapping("/dummy")
  public ResponseEntity<DummyResponse> createDummyRoomAndUser() {
    DummyResponse response = chattingService.createDummy();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/dummy")
  public ResponseEntity<DummyResponse> getDummyRoomAndUser() {
    DummyResponse response = chattingService.getDummy();
    return ResponseEntity.ok(response);
  }

}
