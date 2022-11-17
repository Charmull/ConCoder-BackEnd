package oncoding.concoder.model;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @Column(columnDefinition = "BINARY(16)")
  @GeneratedValue
  private UUID id;

  private String name;

  @OneToOne(mappedBy = "user")
  private Session session;

  public User(final String name) {
    this.name = name;
  }

  public void exit(final Session session) {
    if (isLinkedSession(session)) {
      this.session = null;
      session.delete();
    }
  }

  private boolean isLinkedSession(final Session session) {
    return Objects.nonNull(this.session) && this.session.equals(session);
  }

  public void setSession(final Session session) {
    this.session = session;
  }


}
