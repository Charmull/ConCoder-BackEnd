package oncoding.concoder.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Slf4j
@NoArgsConstructor
public class Session extends JpaBaseEntity {

  private String sessionId;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id")
  private Room room;



  @Builder
  public Session(final UUID id, final String sessionId, final User user, final Room room) {
    this.id = id;
    this.sessionId = sessionId;
    this.user = user;
    this.room = room;

    this.user.setSession(this);
    this.room.addSession(this);
  }

  public void delete() {
    log.info("Session user"+this.user); //여기서 null터짐
    log.info("Session room"+this.room);
    this.user.exit(this);
    this.room.exit(this);
  }
}
