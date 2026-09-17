package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreateReleaseTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.ReleaseTypeDto;
import com.sgic.Defect_Tracker_BE.entity.ReleaseType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReleaseTypeMapper {

    public ReleaseTypeDto toDto(ReleaseType entity) {
        if (entity == null) {
            return null;
        }
        return ReleaseTypeDto.builder()
                .id(entity.getId())
                .name(entity.getReleaseTypeName())
                .releaseTypeName(entity.getReleaseTypeName())
                .description(entity.getDescription())
                .build();
    }

    public List<ReleaseTypeDto> toDtoList(List<ReleaseType> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ReleaseType toEntity(CreateReleaseTypeRequest request) {
        if (request == null) {
            return null;
        }
        return ReleaseType.builder()
                .releaseTypeName(request.resolveName())
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(ReleaseType entity, CreateReleaseTypeRequest request) {
        if (entity == null || request == null) {
            return;
        }
        String name = request.resolveName();
        if (name != null && !name.isEmpty()) {
            entity.setReleaseTypeName(name);
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
    }
}
