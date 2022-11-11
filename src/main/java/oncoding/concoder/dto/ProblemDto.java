package oncoding.concoder.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ProblemDto {

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class AllResponse {
        private Integer number;
        private String title;
        private Float rate;
        private String description;
        private String input;
        private String output;
        private LevelDto.Response level;
        private List<CategoryDto.Response> categories;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class CreateRequest {
        private Integer problemId;
        private String titleKo;
        private Integer level;
    }
}
