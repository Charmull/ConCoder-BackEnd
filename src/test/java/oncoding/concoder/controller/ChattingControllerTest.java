package oncoding.concoder.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.RestAssured;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import oncoding.concoder.dto.ChatDTO.MessageRequest;
import oncoding.concoder.dto.ChatDTO.MessageResponse;
import oncoding.concoder.dto.ChatDTO.SessionRequest;
import oncoding.concoder.dto.ChatDTO.SessionResponse;
import oncoding.concoder.dto.ChatDTO.UserResponse;
import oncoding.concoder.model.Room;
import oncoding.concoder.model.User;
import oncoding.concoder.repository.RoomRepository;
import oncoding.concoder.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
class ChattingControllerTest {

  @LocalServerPort
  private int port;

  private BlockingQueue<SessionResponse> users;
  private BlockingQueue<MessageResponse> messages;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoomRepository roomRepository;

  private void 유저_초기화(){
    userRepository.deleteAll();
  }

  private void 방_초기화(){
    roomRepository.deleteAll();
  }

  private void 유저_삽입() {
    userRepository.save(new User("와일더"));
    userRepository.save(new User("마이클"));
    userRepository.save(new User("제이슨"));
    userRepository.save(new User("오스카"));
  }

  private void 방_생성() {
    roomRepository.save(new Room(2));
    roomRepository.save(new Room(4));
    roomRepository.save(new Room(5));
  }


  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
    유저_초기화();
    방_초기화();
    users = new LinkedBlockingDeque<>();
    messages = new LinkedBlockingDeque<>();
    유저_삽입();
    방_생성();
  }

  private WebSocketStompClient 웹_소켓_STOMP_CLIENT() {
    StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
    WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
    List<Transport> transports = Collections.singletonList(webSocketTransport);
    SockJsClient sockJsClient = new SockJsClient(transports);

    return new WebSocketStompClient(sockJsClient);
  }


  @DisplayName("유저가 입장하고 메시지를 보내면 해당 방에 메시지가 브로드 캐스팅된다.")
  @Test
  void enterUserAndBroadCastMessage()
      throws InterruptedException, ExecutionException, TimeoutException {
    Room room = roomRepository.findAll().get(0);
    User user = userRepository.findAll().get(0);

    //expected result
    UserResponse expectedUser = UserResponse.from(user);
    MessageResponse expectedMessage = new MessageResponse(user.getId(), "채팅을 보내 봅니다.");


    // Settings
    WebSocketStompClient webSocketStompClient = 웹_소켓_STOMP_CLIENT();
    webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());


    // Connection
    ListenableFuture<StompSession> connect = webSocketStompClient
        .connect("ws://localhost:" + port + "/api/ws-connection", new StompSessionHandlerAdapter() {
        });
    StompSession stompSession = connect.get(60, TimeUnit.SECONDS);

    stompSession.subscribe(String.format("/sub/rooms/%s", room.getId()), new StompFrameHandlerImpl(new SessionResponse(), users));
    stompSession.send(String.format("/pub/rooms/%s", room.getId()), new SessionRequest(user.getId(), "1A2B3C4D"));

    stompSession.subscribe(String.format("/sub/rooms/%s/chat", room.getId()), new StompFrameHandlerImpl(new MessageResponse(), messages));
    stompSession.send(String.format("/sub/rooms/%s/chat", room.getId()), new MessageRequest(user.getId(), "채팅을 보내 봅니다."));

    SessionResponse sessionResponse = users.poll(5, TimeUnit.SECONDS);
    MessageResponse messageResponse = messages.poll(5, TimeUnit.SECONDS);

    // Then
    assertThat(sessionResponse.getUserResponses().get(0)).usingRecursiveComparison().isEqualTo(expectedUser);
    assertThat(messageResponse).usingRecursiveComparison().isEqualTo(expectedMessage);
  }

}
