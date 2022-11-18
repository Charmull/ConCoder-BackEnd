package oncoding.concoder.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketConnTest {

  protected StompSession stompSession;

  @LocalServerPort
  private int port;

  private String url;

  private WebSocketStompClient websocketClient;

  @BeforeEach
  void StompSupport() {
    this.websocketClient = new WebSocketStompClient(new SockJsClient(createTransport()));
    this.websocketClient.setMessageConverter(new MappingJackson2MessageConverter());
    this.url = "ws://localhost:";
  }

  @Test
  public void connect() throws ExecutionException, InterruptedException, TimeoutException {
    this.stompSession = this.websocketClient
        .connect(url + port + "/api/ws-connection", new StompSessionHandlerAdapter() {})
        .get(3, TimeUnit.SECONDS);
  }

  @AfterEach
  public void disconnect() {
    if (this.stompSession.isConnected()) {
      this.stompSession.disconnect();
    }
  }

  private List<Transport> createTransport() {
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    return transports;
  }

}
