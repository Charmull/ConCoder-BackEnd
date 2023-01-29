package oncoding.concoder.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Snapshot implements Serializable {
    
    private static final long serialVersionUID = 2148687123052233925L;
    
    @Id
    private UUID id;
    
    @Column(updatable=false, nullable = false)
    private LocalDateTime createdDate;
    
    @Column(updatable=false, nullable = false)
    private LocalDateTime modifiedDate;

    @Column
    @NotNull
    private String memo;
    
    @Column
    @NotNull
    private String content;

    @Builder
    public Snapshot(UUID id, LocalDateTime createdDate, LocalDateTime modifiedDate, String memo,
        String content) {
        this.id = id;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.memo = memo;
        this.content = content;
    }
    
    public void setId(UUID id){
        this.id = id;
    }
    
    public void setMemo(String memo){
        this.memo = memo;
    }
    
    public void setCreatedDate(LocalDateTime time){
        this.createdDate = time;
    }
    
    public void setModifiedDate(LocalDateTime time){
        this.modifiedDate = time;
    }
}
