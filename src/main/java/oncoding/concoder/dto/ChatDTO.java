package oncoding.concoder.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import oncoding.concoder.model.Room;
import oncoding.concoder.model.User;

public class ChatDTO {

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DummyResponse {

    private List<UserResponse> users;
    private List<RoomResponse> rooms;

    public static DummyResponse of(final List<User> users, final List<Room> rooms) {
      List<UserResponse> userResponses = users.stream()
          .map(UserResponse::from)
          .collect(Collectors.toList());
      List<RoomResponse> roomResponses = rooms.stream()
          .map(RoomResponse::from)
          .collect(Collectors.toList());

      return new DummyResponse(userResponses, roomResponses);
    }
  }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExitResponse {

      private UUID roomId;
      private SessionResponse sessionResponse;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageRequest {

      private UUID userId;
      private String content;
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageResponse {

      private UUID userId;
      private String username;
      private String content;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomResponse {

      private UUID id;
      private Integer maxHeadCount;

      public static RoomResponse from(final Room room) {
        return new RoomResponse(room.getId(), room.getMaxHeadCount());
      }
    }


  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SessionForTest {


    private UUID id;
    private String sessionId;

  }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SessionRequest {

      private UUID userId;
      private String sessionId;
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SessionResponse {

      private List<UserResponse> userResponses;

      public static SessionResponse from(final List<User> users) {
        return new SessionResponse(users.stream()
            .map(UserResponse::from)
            .collect(Collectors.toList()));
      }
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponse {

      private UUID id;
      private String name;
      private String sessionId;

      public static UserResponse from(final User user) {
        String userSessionId;
        if(user.getSession()!=null){
          userSessionId = user.getSession().getSessionId();
        }else{
          userSessionId = "Empty";
        }
        return new UserResponse(user.getId(), user.getName(), userSessionId);
      }
    }

  }
