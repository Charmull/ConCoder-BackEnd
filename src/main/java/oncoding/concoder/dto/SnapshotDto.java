package oncoding.concoder.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class SnapshotDto {
    
    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    public static class AllResponse{
        
        private UUID id;
    
        private String memo;
    
        private String content;
     
        private LocalDateTime createdDate;
        
        private LocalDateTime modifiedDate;
        
    }
    
    
    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    public static class CreateRequest{
    
        private String memo;
    
        private String content;
        
    }
    
    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    public static class ModifyRequest{
    
        private UUID id;
        
        private String memo;
    }

}
