package oncoding.concoder.mapper;

import java.util.List;
import oncoding.concoder.dto.CategoryDto;
import oncoding.concoder.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CategoryDtoMapper {
    public abstract List<CategoryDto.Response> toResponseList(List<Category> in);
}
