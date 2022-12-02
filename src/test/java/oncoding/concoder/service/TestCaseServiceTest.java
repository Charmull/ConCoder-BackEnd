package oncoding.concoder.service;

import static org.junit.jupiter.api.Assertions.assertTrue;


import io.restassured.internal.util.SafeExceptionRethrower;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import oncoding.concoder.model.Snapshot;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class TestCaseServiceTest {

  @Autowired
  TestCaseService service;

  @Autowired
  RedisTemplate<String, Object> redisTemplate;

  private String roomId= UUID.randomUUID().toString();

//  @Before
//  void roomId_임시_생성(){
//    this.roomId = UUID.randomUUID().toString();
//  }


  @Test
  void testCase_생성_및_저장(){

    JSONObject ob = new JSONObject();
    ob.put("input","input");
    ob.put("output","output");

    JSONObject result = (JSONObject) service.createTestCase(this.roomId,ob);

    //key확인
    Set<String> redisKeys = redisTemplate.keys(this.roomId);
    Iterator<String> it = redisKeys.iterator();
    while (it.hasNext()) {
      String data = it.next();
    }

    assertTrue(result.get("output").equals("output"));
    assertTrue(result.get("input").equals("input"));

  }

  @Test
  void testCase_전체_조회(){

    JSONObject ob = new JSONObject();
    ob.put("input","input");
    ob.put("output","output");


    service.createTestCase(this.roomId,ob);

    Map<String,JSONObject> map = service.getTestCases(this.roomId);


    Collection<JSONObject> list = map.values();
    List<JSONObject> list2 = new ArrayList<>(list);
    assertTrue(list2.get(0).get("input").equals("input"));

  }

  @Test
  void testCase_단건_조회(){

    //JSONObject object = service.getTestCase(this.roomId,"30d2eb5b-33a2-451a-acc3-650da3cebcab");

    JSONObject ob = new JSONObject();
    ob.put("input","input");
    ob.put("output","output");

    service.createTestCase(this.roomId,ob);

    Map<String,JSONObject> map = service.getTestCases(this.roomId);

    Set s = map.keySet();

    Iterator<String> iter = s.iterator();
    while (iter.hasNext()) {

      System.out.println("here");
      String data = iter.next();
      System.out.println(service.getTestCase(this.roomId,data));
      String data = iter.next();
      assertTrue(map.get(data).get("input").equals("input"));
    }

  }

  @Test
  void testCase_수정(){

    JSONObject ob = new JSONObject();
    ob.put("input","input");
    ob.put("output","output");

    JSONObject result = (JSONObject) service.createTestCase(this.roomId,ob);

    Map<String,JSONObject> map = service.getTestCases(this.roomId);

    Set s = map.keySet();

    Iterator<String> iter = s.iterator();
    while (iter.hasNext()) {
      String data = iter.next();
      JSONObject ob2 = new JSONObject();
      ob2.put("input","modified input");
      ob2.put("output","modified output");
      System.out.println(service.modifyTestCase(this.roomId,data,ob2).toJSONString());
      assertTrue(map.get(data).get("input").equals("modified input"));
      assertTrue(map.get(data).get("output").equals("modified output"));

    }

  }

  @Test
  void testCase_단건_삭제(){

    Map<String,JSONObject> map = service.getTestCases(this.roomId);
    Set s = map.keySet();

    Iterator<String> iter = s.iterator();
    while (iter.hasNext()) {
      String data = iter.next();
      service.deleteTestCase(roomId,data);
      assertTrue(map.get("data")==null);

    }



  }

}
