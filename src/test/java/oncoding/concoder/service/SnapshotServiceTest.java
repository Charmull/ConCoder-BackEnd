package oncoding.concoder.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.UUID;
import oncoding.concoder.model.Snapshot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class SnapshotServiceTest {
    
    @Autowired
    SnapshotService service;
    
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    
    
    @Test
    void Redis_빌더(){
        Snapshot snapshot = Snapshot.builder().memo("memo").content("hi").build();
    
    }
    
    @Test
    void redis_전체_조회(){
    
        Snapshot snapshot = Snapshot.builder().memo("memo").content("hi").build();
        snapshot.setId(UUID.randomUUID());
        
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("Snapshot",snapshot.getId().toString(),snapshot);
        
        Map<String,Snapshot> map = service.getSnapshots();
        assertTrue((map.get("332a90fb-c02f-4b35-b273-b66eab7e6140").getMemo()).equals("modified_memo"));
        
    }
    
    
    @Test
    void redis_단건_조회(){
        Snapshot snapshot = service.getSnapshot(UUID.fromString("332a90fb-c02f-4b35-b273-b66eab7e6140"));
        assertTrue(snapshot != null);
        assertTrue(snapshot.getMemo().equals("memo"));
        assertTrue(snapshot.getContent().equals("hi"));
    }
    
    @Test
    void redis_생성_및_저장(){
    
        Snapshot snapshot = Snapshot.builder().memo("new_memo").content("new_content").build();
        Snapshot newSnapshot = service.createSnapshot(snapshot);
        
        assertTrue(newSnapshot.getMemo().equals("new_memo"));
        assertTrue(snapshot.getContent().equals("new_content"));
        
    }
    
    @Test
    void redis_메모_수정(){
        Snapshot shot = service.getSnapshot(UUID.fromString("332a90fb-c02f-4b35-b273-b66eab7e6140"));
        assertTrue(shot.getMemo().equals("memo"));
        Snapshot newshot = service.modifySnapshot(UUID.fromString("332a90fb-c02f-4b35-b273-b66eab7e6140"),"modified_memo");
        assertTrue(newshot.getMemo().equals("modified_memo"));
    
    }
    
    
    @Test
    void redis_단건_삭제(){

        assertTrue(service.getSnapshot(UUID.fromString("068d718b-4a80-4bb5-96bf-fec395e1d5b3"))!= null);
        service.deleteSnapshot(UUID.fromString("068d718b-4a80-4bb5-96bf-fec395e1d5b3"));
        assertTrue(service.getSnapshot(UUID.fromString("068d718b-4a80-4bb5-96bf-fec395e1d5b3"))== null);
    
    }
    
    
}
