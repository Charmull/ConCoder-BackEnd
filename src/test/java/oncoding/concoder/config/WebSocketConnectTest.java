package oncoding.concoder.config;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class WebSocketConnectTest {

  @DisplayName("소켓 연결 테스트")
  @Test
  void webSocketConnectTest(){
    ExtractableResponse<Response> response = given().log().all()
      .when()
      .get("/ws-connection")
      .then().log().all()
      .extract();

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }
}
