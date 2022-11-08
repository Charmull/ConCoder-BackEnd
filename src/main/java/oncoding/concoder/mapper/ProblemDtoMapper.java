package oncoding.concoder.mapper;

import java.util.List;
import oncoding.concoder.dto.ProblemDto;
import oncoding.concoder.model.Problem;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public abstract class ProblemDtoMapper {
    public abstract List<ProblemDto.AllResponse> toAllResponseList (List<Problem> in);
    public abstract ProblemDto.AllResponse toAllResponse(Problem in);
}
