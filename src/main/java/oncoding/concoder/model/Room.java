package oncoding.concoder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

  @Id
  @Column(columnDefinition = "BINARY(16)")
  @GeneratedValue
  private UUID id;

  @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
  private List<Session> sessions;

  private int maxHeadCount;

  @Builder
  public Room(final int maxHeadCount) {
    this.maxHeadCount = maxHeadCount;
    this.sessions = new ArrayList<>();
  }

  public List<User> users() {
    return sessions.stream()
        .map(Session::getUser)
        .collect(Collectors.toList());
  }

  public void exit(final Session session) {
    if (sessions.contains(session)) {
      sessions.remove(session);
      session.delete();
    }
  }

  public void addSession(final Session session) {
    sessions.add(session);
  }
}
