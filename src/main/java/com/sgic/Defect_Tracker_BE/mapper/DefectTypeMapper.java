package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreateDefectTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.DefectTypeDto;
import com.sgic.Defect_Tracker_BE.entity.DefectType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefectTypeMapper {

    public DefectTypeDto toDto(DefectType entity) {
        if (entity == null) {
            return null;
        }
        return DefectTypeDto.builder()
                .id(entity.getId())
                .name(entity.getDefectTypeName())
                .defectTypeName(entity.getDefectTypeName())
                .description(entity.getDescription())
                .build();
    }

    public List<DefectTypeDto> toDtoList(List<DefectType> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public DefectType toEntity(CreateDefectTypeRequest request) {
        if (request == null) {
            return null;
        }
        return DefectType.builder()
                .defectTypeName(request.resolveName())
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(DefectType entity, CreateDefectTypeRequest request) {
        if (entity == null || request == null) {
            return;
        }
        String name = request.resolveName();
        if (name != null && !name.isEmpty()) {
            entity.setDefectTypeName(name);
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
    }
}
