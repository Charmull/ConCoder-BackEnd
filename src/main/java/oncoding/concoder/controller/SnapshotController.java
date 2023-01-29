package oncoding.concoder.controller;

import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.dto.SnapshotDto;
import oncoding.concoder.mapper.SnapshotDtoMapper;
import oncoding.concoder.service.SnapshotService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/snapshots", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class SnapshotController {

    private final SnapshotService service;
    private final SnapshotDtoMapper mapper;

    /**
     * 전체 조회
     * @return
     */
    @GetMapping
    public Map<String, SnapshotDto.AllResponse> getSnapshots(){
        return mapper.toSnapshotAllMap(service.getSnapshots());
    }

    /**
     * 단건 조회
     * @param
     * @return
     */
    @GetMapping("/{snapshotId}")
    public SnapshotDto.AllResponse getSnapshot(@PathVariable UUID snapshotId){
        return mapper.toSnapshotDtoAll(service.getSnapshot(snapshotId));
    }

    /**
     * 새로운 스냅샷 저장
     * @param in
     * @return
     */
    @PostMapping
    public SnapshotDto.AllResponse createSnapshot(@RequestBody SnapshotDto.CreateRequest in){
        return mapper.toSnapshotDtoAll(service.createSnapshot(mapper.toSnapshot(in)));
    }


    /**
     * 수정
     * @param
     * @return
     */
    @PutMapping
    public SnapshotDto.AllResponse modifySnapshot(@RequestBody SnapshotDto.ModifyRequest in){
        UUID id = in.getId();
        String memo = in.getMemo();
        return mapper.toSnapshotDtoAll(service.modifySnapshot(id, memo));
    }


    /**
     * 삭제
     * @param
     * @return
     */
    @DeleteMapping("/{snapshotId}")
    public void deleteSnapshot(@PathVariable UUID snapshotId){
        service.deleteSnapshot(snapshotId);
    }


}
