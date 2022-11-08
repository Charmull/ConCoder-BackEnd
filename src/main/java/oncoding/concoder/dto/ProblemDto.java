package oncoding.concoder.dto;

import java.util.List;

public class ProblemDto {

    public static class AllResponse {
        private Integer number;
        private String title;
        private Float rate;
        private String content;
        private LevelDto.Response level;
        private List<CategoryDto.Response> categories;
    }
}
