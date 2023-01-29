package oncoding.concoder.mapper;

import java.util.List;
import java.util.Map;
import oncoding.concoder.dto.SnapshotDto;
import oncoding.concoder.model.Snapshot;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(
    implementationName = "SnapshotDtoMapperImpl",
    builder=@Builder(disableBuilder = true),
    componentModel = "spring"
)
public abstract class SnapshotDtoMapper {

    public abstract Snapshot toSnapshot(SnapshotDto.CreateRequest in);
    public abstract Snapshot toSnapshot(SnapshotDto.ModifyRequest in);
    public abstract SnapshotDto.AllResponse toSnapshotDtoAll(Snapshot snapshot);
    
    public abstract List<SnapshotDto.AllResponse> toSnapshotAllList(List<Snapshot> in);
    public abstract Map<String,SnapshotDto.AllResponse> toSnapshotAllMap(Map<String,Snapshot> in);
}
