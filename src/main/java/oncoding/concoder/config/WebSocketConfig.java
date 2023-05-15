package oncoding.concoder.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@Slf4j
@EnableWebSocketMessageBroker //웹 소켓 메시지 처리 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer { //WebSocketConfigurer

  @Override
  public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
    // stomp 최대 버퍼 사이즈를 늘리기 위한 설정
    registry.setMessageSizeLimit(50000 * 1024);
    registry.setSendBufferSizeLimit(10240 * 1024);
    registry.setSendTimeLimit(600000); //10min
  }


  @Override
  public void configureMessageBroker(final MessageBrokerRegistry registry) {
    // 메세지 브로커는 아래 주소로 시작하는 주소의 Subscriber 에게 메시지를 전달하는 역할을 한다.
    // 일반적으로 /topic은 한 명이 메시지를 발행했을 때, 해당 토픽을 구독하고 있는 n명에게 메시지를 뿌릴 때 사용
    // 반면에 /queue는 한 명이 메시지를 발행했을 때, 발행한 한 명에게 다시 정보를 보내는 경우
    registry.enableSimpleBroker("/sub");
    // 클라이언트가 서버로 메시지를 보낼 때 아래 주소를 붙인다.
    //즉 도착 경로에 대한 prefix 설정
    //예를 들어 /app으로 설정해두면 /topic/hello라는 토픽에 대해 구독을 신청했을 때, 실제 경로는 /app/topic/hello가 되는 것
    registry.setApplicationDestinationPrefixes("/pub");
  }

  @Override
  public void registerStompEndpoints(final StompEndpointRegistry registry) {
    registry.addEndpoint("/ws-connection") // 클라이언트가 웹 소켓 요청을 하고 싶을 때 해당 End point 로 요청을 보낸다.
        .setAllowedOrigins("*");
    registry.addEndpoint("/ws-connection") // 클라이언트가 웹 소켓 요청을 하고 싶을 때 해당 End point 로 요청을 보낸다.
        .setAllowedOrigins("*")
        .withSockJS(); // fallback
  }





}
