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
import lombok.extern.slf4j.Slf4j;

@Getter
@Entity
@Slf4j
@NoArgsConstructor
public class Session extends JpaBaseEntity {

  private String sessionId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id")
  private Room room;


  @Builder
  public Session(final String sessionId, final User user, final Room room) {
    this(null, sessionId, user, room);
  }


  @Builder
  public Session(final UUID id, final String sessionId, final User user, final Room room) {
    this.id = id;
    this.sessionId = sessionId;
    this.user = user;
    this.room = room;

    user.setSession(this);
    room.addSession(this);
  }

  public void delete() {
    log.info("Session user"+this.user);
    log.info("Session room"+this.room);
    this.user.exit(this);
    this.room.exit(this);
  }
}
