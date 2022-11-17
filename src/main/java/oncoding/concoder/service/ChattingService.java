package oncoding.concoder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.dto.ChatDTO.DummyResponse;
import oncoding.concoder.dto.ChatDTO.ExitResponse;
import oncoding.concoder.dto.ChatDTO.MessageRequest;
import oncoding.concoder.dto.ChatDTO.MessageResponse;
import oncoding.concoder.dto.ChatDTO.SessionRequest;
import oncoding.concoder.dto.ChatDTO.SessionResponse;
import oncoding.concoder.model.Room;
import oncoding.concoder.model.Session;
import oncoding.concoder.model.User;
import oncoding.concoder.repository.RoomRepository;
import oncoding.concoder.repository.SessionRepository;
import oncoding.concoder.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ChattingService {

  private final UserRepository userRepository;
  private final RoomRepository roomRepository;
  private final SessionRepository sessionRepository;


  public MessageResponse sendMessage(final MessageRequest request) {
    User user = userRepository.findById(request.getUserId()).orElseThrow(IllegalArgumentException::new);
    return new MessageResponse(user.getId(), request.getContent());
  }

  @Transactional
  public SessionResponse enter(final UUID roomId, final SessionRequest request) {
    User user = userRepository.findById(request.getUserId()).orElseThrow(IllegalArgumentException::new);
    Room room = roomRepository.findById(roomId).orElseThrow(IllegalArgumentException::new);
    Session session = new Session(request.getSessionId(), user, room);

    sessionRepository.save(session);

    return SessionResponse.from(room.users());
  }

  @Transactional
  public ExitResponse exit(final String sessionId) {
    Session session = sessionRepository.findBySessionId(sessionId).orElseThrow(IllegalArgumentException::new);
    Room room = session.getRoom();

    session.delete();
    sessionRepository.delete(session);

    return new ExitResponse(room.getId(), SessionResponse.from(room.users()));
  }


  public DummyResponse createDummy() {
    List<User> users = new ArrayList<>();
    List<Room> rooms = new ArrayList<>();

    users.add(userRepository.save(new User("와일더")));
    users.add(userRepository.save(new User("마이클")));
    users.add(userRepository.save(new User("제이슨")));
    users.add(userRepository.save(new User("오스카")));

    rooms.add(roomRepository.save(new Room(2)));
    rooms.add(roomRepository.save(new Room(4)));
    rooms.add(roomRepository.save(new Room(5)));

    return DummyResponse.of(users, rooms);
  }

}
