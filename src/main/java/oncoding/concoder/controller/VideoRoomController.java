package oncoding.concoder.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.ChatDTO.DummyResponse;
import oncoding.concoder.dto.ChatDTO.ExitResponse;
import oncoding.concoder.dto.ChatDTO.MessageRequest;
import oncoding.concoder.dto.ChatDTO.SessionRequest;
import oncoding.concoder.dto.ChatDTO.SessionResponse;
import oncoding.concoder.dto.ChatDTO.UserResponse;
import oncoding.concoder.service.ChattingService;
import org.json.simple.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  private Map<UUID, Object> usersAtRooms;



  @MessageMapping("/video/chat/{roomId}")
  public void chat(@DestinationVariable final String roomId, JSONObject ob) {
    log.info("/rooms/chat/"+roomId+" userId:  "+ob.get("userId"));
    log.info("/rooms/chat/"+roomId+" content: "+ob.get("content"));
    MessageRequest request = new MessageRequest(UUID.fromString((String) ob.get("userId")), (String)ob.get("content"));
    template.convertAndSend("/sub/video/chat/"+ roomId , chattingService.sendMessage(request));
    log.info("after chatting convert and send");
  }

  /**
   * 코드를 해당 룸 사용자들에게 공유
   * @param roomId
   * @param ob
   */
  @MessageMapping("/code/{roomId}")
  private void codeShare(@DestinationVariable final String roomId,JSONObject ob) {

    template.convertAndSend("/sub/code/"+roomId,ob);

    log.info("convertAndSend to /sub/code/"+roomId+" : "+ ob);

  }




  // 실시간으로 들어온 세션 감지하여 전체 세션 리스트 반환
  @MessageMapping("/video/joined-room-info/{roomId}")
  private SessionResponse joinRoom(@DestinationVariable final String roomId,JSONObject ob) {
    //만약에 connectEvent 에서 sessioniD 받아올 수 있으면 HEADER가 아니라 그냥 ob 안에 담아서 처리하면 됨
    String sessionId = (String) ob.get("sessionId");

    log.info("@MessageMapping(\"/video/joined-room-info\") sessionId: "+sessionId+" ");
    log.info("@MessageMapping(\"/video/joined-room-info\") roomId : "+roomId+" ");
    log.info("@MessageMapping(\"/video/joined-room-info\") userId : "+(String)ob.get("userId")+" ");

    UUID realRoomId = UUID.fromString(roomId);
    SessionRequest request = new SessionRequest(UUID.fromString((String)ob.get("userId")),sessionId);

    sessionResponse = chattingService.enter(realRoomId, request); //현재 roomId에 해당되는 애들만

    template.convertAndSend("/sub/video/joined-room-info/"+ roomId,sessionResponse);

    log.info("convertAndSend to /sub/video/joined-room-info/"+ roomId +" : "+ sessionResponse);

    return sessionResponse;

  }

  // 실시간으로 나간 세션 감지하여 리턴
  @MessageMapping("/video/unjoined-room-info/{roomId}")
  private JSONObject unJoinRoom(@DestinationVariable final String roomId,JSONObject ob) {

    String sessionId = (String) ob.get("sessionId");
    log.info("@MessageMapping(\"/video/unjoined-room-info\") sessionId: "+sessionId+" ");

    String removedId = "";

    List<UserResponse> users = this.sessionResponse.getUserResponses();

    //현재 세션 목록에서 연결 끊은 유저 제외시킴
    for (UserResponse userResponse : users) {
      if (userResponse.getSessionId().equals(sessionId)) {
        removedId = sessionId;
        users.remove(userResponse);
        break;
      }
    }

    log.info("removed id: "+removedId);

    //채팅방에서도 나감
    ExitResponse response = chattingService.exit(sessionId);

    //template.convertAndSend("/sub/rooms/" + response.getRoomId(), response.getSessionResponse());
    //log.info("convertAndSend to /sub/rooms/getRoomid",response.getSessionResponse());

    JSONObject object=new JSONObject();
    object.put("userId",ob.get("userId"));
    object.put("roomId",roomId);

    template.convertAndSend("/sub/video/unjoined-room-info/"+roomId,object);

    log.info("convertAndSend to /sub/video/unjoined-room-info/"+ removedId);

    return object;

  }




  // caller의 정보를 다른 callee들에게 쏴준다.
  @MessageMapping("/video/caller-info/{roomId}")
  private void caller(@DestinationVariable final String roomId,JSONObject ob) {


    template.convertAndSend("/sub/video/caller-info/"+roomId,ob);

    log.info("convertAndSend to /sub/video/caller-info/"+roomId+" : " + ob);

  }

  // caller와 callee의 signaling을 위해 callee 정보를 쏴준다.
  @MessageMapping("/video/callee-info/{roomId}")
  private void answerCall(@DestinationVariable final String roomId,JSONObject ob) {

    template.convertAndSend("/sub/video/callee-info/"+roomId,ob);
    log.info("convertAndSend to /sub/video/callee-info/"+roomId+" : "+ ob);

  }


  @EventListener
  private void handleSessionConnected(SessionConnectEvent event) {
    String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
  //connect하고 나서 클라이언트한테 본내주면 될 듯
    log.info("session connected sessionId: "+sessionId);

  }


  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = accessor.getSessionId();

    log.info("disconnect event Listener sessionId: "+sessionId);
  }


  @PostMapping("/video")
  public ResponseEntity<DummyResponse> createRoomAndUser(@RequestBody JSONObject ob) {
    String username = (String) ob.get("username");
    DummyResponse response = chattingService.createRoomAndUser(username);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/video/user")
  public ResponseEntity<UserResponse> createUser(@RequestBody JSONObject ob) {
    String username = (String) ob.get("username");
    UserResponse response = chattingService.createOnlyUser(username);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/video")
  public void clearRoomAndUser() {
    chattingService.clear();
  }

  @GetMapping("/dummy")
  public ResponseEntity<DummyResponse> getDummyRoomAndUser() {
    DummyResponse response = chattingService.getDummy();
    return ResponseEntity.ok(response);
  }


  @PostMapping("/dummy")
  public ResponseEntity<DummyResponse> createDummyRoomAndUser() {
    DummyResponse response = chattingService.createDummy();
    return ResponseEntity.ok(response);
  }



}
