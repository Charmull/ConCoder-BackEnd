//package oncoding.concoder.service;
//
//
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import oncoding.concoder.dto.ChatDTO.ExitResponse;
//import oncoding.concoder.dto.ChatDTO.SessionResponse;
//import oncoding.concoder.dto.ChatDTO.UserResponse;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//@Service
//@Slf4j
//@Transactional
//@RequiredArgsConstructor
//public class VideoRoomService {
//
//
//  public String getSessionIdForDisconnect(String sessionId) {
//    return sessionId;
//  }
//
//  public String handleSessionDisconnect(String sessionId, SessionResponse sessionResponse) {
//
//    String removedId = "";
//
//    List<UserResponse> users = sessionResponse.getUserResponses();
//
//    for (UserResponse userResponse : users) {
//      if (userResponse.getSessionId().equals(sessionId)) {
//        removedId = userResponse.getSessionId();
//        users.remove(userResponse);
//        break;
//      }
//    }
//
//    return removedId;
//
//  }
//
//}
