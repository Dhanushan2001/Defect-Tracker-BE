package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreateStatusTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.StatusTypeDto;
import com.sgic.Defect_Tracker_BE.entity.StatusType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StatusTypeMapper {

    public StatusTypeDto toDto(StatusType entity) {
        if (entity == null) {
            return null;
        }
        return StatusTypeDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .statusName(entity.getName())
                .defectStatusName(entity.getName())
                .color(entity.getColor())
                .colorCode(entity.getColor())
                .type(entity.getType())
                .statusType(entity.getType())
                .description(entity.getDescription())
                .positionX(entity.getPositionX())
                .positionY(entity.getPositionY())
                .build();
    }

    public List<StatusTypeDto> toDtoList(List<StatusType> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public StatusType toEntity(CreateStatusTypeRequest request) {
        if (request == null) {
            return null;
        }
        return StatusType.builder()
                .name(request.resolveName())
                .color(request.getColor() != null && !request.getColor().trim().isEmpty() ? request.getColor() : "#000000")
                .type(request.resolveType())
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(StatusType entity, CreateStatusTypeRequest request) {
        if (entity == null || request == null) {
            return;
        }
        String name = request.resolveName();
        if (name != null && !name.isEmpty()) {
            entity.setName(name);
        }
        if (request.getColor() != null && !request.getColor().trim().isEmpty()) {
            entity.setColor(request.getColor());
        }
        if (request.resolveType() != null) {
            entity.setType(request.resolveType());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
    }
}
