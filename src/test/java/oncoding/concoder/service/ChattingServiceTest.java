package oncoding.concoder.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import oncoding.concoder.dto.ChatDTO.SessionRequest;
import oncoding.concoder.dto.ChatDTO.SessionResponse;
import oncoding.concoder.model.Room;
import oncoding.concoder.model.Session;
import oncoding.concoder.model.User;
import oncoding.concoder.repository.RoomRepository;
import oncoding.concoder.repository.SessionRepository;
import oncoding.concoder.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class ChattingServiceTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private SessionRepository sessionRepository;

  @Autowired
  private ChattingService service;


  @DisplayName("세션을 생성(방에 유저 입장)한다 - 세션이 생성은 되었지만 user의 session Id는 업뎃 전")
  @Test
  void createSession() {
    // given
    User user = userRepository.save(new User("와일더"));
    Room room = roomRepository.save(new Room(5));

    SessionRequest request = new SessionRequest(user.getId(), "1A2B3C4D"); //userId, sessionId

    // when
    //enter : user 찾고 room 찾고 "1A2B3C4D"를 sessionId로 가지고, room, user 정보를 가지는 세션 생성하고 save
    SessionResponse response = service.enter(room.getId(), request);

    // then
    assertThat(response.getUserResponses().get(0).getName())
        .isEqualTo("와일더");

    assertThat(response.getUserResponses().get(0).getName())
        .isEqualTo("와일더");

  }


  private Session 세션_생성() {
    User user = userRepository.save(new User("테스터"));
    Room room = roomRepository.save(new Room(5));

    Session session = new Session("1A2B3C4D", user, room);

    return sessionRepository.save(session);
  }


  @DisplayName("세션을 삭제(방에 유저 퇴장)한다.")
  @Test
  void deleteSession() {
    // given
    Session session = 세션_생성();

    // when
    service.exit(session.getSessionId());

    // then
    assertThat(sessionRepository.findBySessionId(session.getSessionId())).isNotPresent();
  }

}
