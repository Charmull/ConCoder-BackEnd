package oncoding.concoder.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TestCaseService {

  private final RedisTemplate<String,Object> redisTemplate;


  /**
   * 해당 룸이 가지고 있는 테스트 케이스 전체 조회
   * @param roomId
   * @return
   */
  public Map<String,JSONObject> getTestCases(String roomId){

    //roomId는 first key값, testcaseId는 secondkey값 (내부의 key값)

    HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();


    return hashOperations.entries(roomId);
  }


  /**
   * 특정 테스트 케이스 조회
   * @param roomId
   * @param testCaseId
   * @return
   */
  public JSONObject getTestCase(String roomId,String testCaseId){

    HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();

    JSONObject testcase = hashOperations.get(roomId,testCaseId);


    testcase.put("testCaseId",testCaseId);

    return testcase;

  }


  /**
   * 새로운 테스트 케이스 저장
   * @param roomId
   * @param ob
   * @return
   */
  public JSONObject createTestCase(String roomId,JSONObject ob){
    HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();

    UUID testCaseId = UUID.randomUUID();

    hashOperations.put(roomId,testCaseId.toString(),ob);

    JSONObject created = this.getTestCase(roomId,testCaseId.toString());

    return created;
  }

  /**
   * 특정 테스트 케이스 편집 - 수정
   * @param roomId
   * @param testCaseId
   * @param ob
   * @return
   */
  public JSONObject modifyTestCase(String roomId,String testCaseId,JSONObject ob){
    HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
    hashOperations.put(roomId,testCaseId,ob);

    return this.getTestCase(roomId,testCaseId.toString());
    
  }

  /**
   * 테스트 케이스 삭제
   * @param roomId
   * @param testCaseId
   */
  public void deleteTestCase(String roomId, String testCaseId){
    HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
    hashOperations.delete(roomId,testCaseId);
  }




  
}
