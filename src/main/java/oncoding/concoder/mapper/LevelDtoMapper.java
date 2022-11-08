package oncoding.concoder.mapper;

import java.util.List;
import oncoding.concoder.dto.LevelDto;
import oncoding.concoder.model.Level;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class LevelDtoMapper {
    public abstract List<LevelDto.Response> toResponseList(List<Level> in);
}
