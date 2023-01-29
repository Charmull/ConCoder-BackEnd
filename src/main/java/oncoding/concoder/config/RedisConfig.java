package oncoding.concoder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    
    @Value("${spring.redis.host}")
    private String host;
    
    @Value("${spring.redis.port}")
    private int port;
    
    /*
    Spring Data Redis는 Redis에 두 가지 접근 방식을 제공합니다.
    하나는 RedisTemplate을 이용한 방식이며, 다른 하나는 RedisRepository를 이용한 방식
    
    두 방식 모두 Redis에 접근하기 위해서는 Redis 저장소와 연결하는 과정이 필요
    이 과정을 위해 RedisConnectionFactory 인터페이스(LettuceConnectionFactory )를 사용
    * */
    @Bean //redisConnectionFactory 이름의 빈(역할)은 LettuceConnectionFactory(host, port)를 구현체로 선택한 것
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }
    
    /*
    redisTemplate을 사용하여 Redis 저장소에 접근하기로 한다
    RedisTemplate은 Redis 저장소에 오브젝트를 저장할 때 기본값으로 정의된 JdkSerializationRedisSerializer을 이용
     */
    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
    
    /*
    대부분 레디스 key-value 는 문자열 위주이기 때문에 문자열에 특화된 템플릿을 제공
    RedisTemplate 을 상속받은 클래스임.
    StringRedisSerializer로 직렬화함
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        final StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return stringRedisTemplate;
    }
    
    

}
