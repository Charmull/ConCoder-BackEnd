package oncoding.concoder.config;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class WebSocketConnectTest {
/*
  private StompSession stompSession;

  @LocalServerPort
  private int port;

  private String url;

  private WebSocketStompClient webSocketStompClient;

  private List<Transport> createTransport() {
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    return transports;
  }

  @BeforeEach
  public void setUp() {
    this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(createTransport()));
    this.webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
    this.url = "ws://localhost:";
    //RestAssured.port = port;
  }

  @DisplayName("웹 소켓 연결을 시도한다.")
  @Test
  void connect() throws ExecutionException, InterruptedException, TimeoutException {

    System.out.println("websocket url: "+url + port + "/ws-connection");

    this.stompSession = this.webSocketStompClient
        .connect(url + port + "/ws-connection", new StompSessionHandlerAdapter() {})
        .get(3, TimeUnit.SECONDS);
  }

  @AfterEach
  @Test
  public void disconnect() {
    if (this.stompSession.isConnected()) {
      this.stompSession.disconnect();
    }
  }

*/
@LocalServerPort
private int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
  }

  @DisplayName("웹 소켓 연결을 시도한다.")
  @Test
  void webSocketConnectTest() {
    ExtractableResponse<Response> response = given().log().all()
        .when()
        .get("/api/ws-connection")
        .then().log().all()
        .extract();

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

}