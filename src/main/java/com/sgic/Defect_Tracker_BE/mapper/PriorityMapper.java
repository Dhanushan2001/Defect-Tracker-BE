package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreatePriorityRequest;
import com.sgic.Defect_Tracker_BE.dto.PriorityDto;
import com.sgic.Defect_Tracker_BE.entity.Priority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriorityMapper {

    public PriorityDto toDto(Priority entity) {
        if (entity == null) {
            return null;
        }
        return PriorityDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .priorityName(entity.getName())
                .color(entity.getColor())
                .weight(entity.getWeight() != null ? entity.getWeight() : 1)
                .description(entity.getDescription())
                .build();
    }

    public List<PriorityDto> toDtoList(List<Priority> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Priority toEntity(CreatePriorityRequest request) {
        if (request == null) {
            return null;
        }
        return Priority.builder()
                .name(request.resolveName())
                .color(request.getColor() != null && !request.getColor().trim().isEmpty() ? request.getColor() : "#3B82F6")
                .weight(request.getWeight() != null ? request.getWeight() : 1)
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(Priority entity, CreatePriorityRequest request) {
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
        if (request.getWeight() != null) {
            entity.setWeight(request.getWeight());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
    }
}
