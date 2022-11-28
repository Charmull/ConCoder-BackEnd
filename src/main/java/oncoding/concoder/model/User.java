package oncoding.concoder.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends JpaBaseEntity {

  private String name;

  @OneToOne(mappedBy = "user",fetch = FetchType.LAZY)
  private Session session;

  public User(final String name) {
    this.name = name;
  }

  public void exit(final Session session) {
    if (isLinkedSession(session)) {
      this.session = null;
      //session.delete();
    }
  }

  private boolean isLinkedSession(final Session session) {
    return Objects.nonNull(this.session) && this.session.equals(session);
  }

  public void setSession(final Session session) {
    this.session = session;
  }


}
