package oncoding.concoder.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.model.Snapshot;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SnapshotService {
    
    private final RedisTemplate<String,Object> redisTemplate;
    
    /**
     * 목록 조회
     * @return 현재 snpshot 리스트
     */
    public Map<String,Snapshot> getSnapshots(){
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries("Snapshot");
        
    }
    
    /**
     * 단건 조회
     *
     * @return 현재 snpshot 리스트
     */
    public Snapshot getSnapshot(UUID id){
        HashOperations hashOperations = redisTemplate.opsForHash();
        return (Snapshot) hashOperations.get("Snapshot",id.toString());
      
    }
    
    /**
     * snapshot 생성
     * @param snapshot
     * @return
     */
    public Snapshot createSnapshot(Snapshot snapshot){
    
        snapshot.setId(UUID.randomUUID());
        snapshot.setCreatedDate(LocalDateTime.now());
    
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("Snapshot",snapshot.getId().toString(),snapshot);
        
        Snapshot result = this.getSnapshot(snapshot.getId());
        
        return result;
    }
    
    /**
     * 스냅샷 수정 - 메모만 수정 가능
     * @param id
     * @param memo
     * @return
     */
    public Snapshot modifySnapshot(UUID id, String memo){
    
        Snapshot shot = this.getSnapshot(id);
        shot.setMemo(memo);
        shot.setModifiedDate(LocalDateTime.now());
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("Snapshot",id.toString(),shot);
        
        return shot;
        
    }
    
    /**
     * 스냅샷 단건 삭제
     */
    public void deleteSnapshot(UUID id){
    
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete("Snapshot",id.toString());
    }
    

}
