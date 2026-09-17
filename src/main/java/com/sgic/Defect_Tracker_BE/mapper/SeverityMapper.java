package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreateSeverityRequest;
import com.sgic.Defect_Tracker_BE.dto.SeverityDto;
import com.sgic.Defect_Tracker_BE.entity.Severity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeverityMapper {

    public SeverityDto toDto(Severity entity) {
        if (entity == null) {
            return null;
        }
        return SeverityDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .severityName(entity.getName())
                .color(entity.getColor())
                .weight(entity.getWeight() != null ? entity.getWeight() : 1)
                .description(entity.getDescription())
                .build();
    }

    public List<SeverityDto> toDtoList(List<Severity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Severity toEntity(CreateSeverityRequest request) {
        if (request == null) {
            return null;
        }
        return Severity.builder()
                .name(request.resolveName())
                .color(request.getColor() != null && !request.getColor().trim().isEmpty() ? request.getColor() : "#3B82F6")
                .weight(request.getWeight() != null ? request.getWeight() : 1)
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(Severity entity, CreateSeverityRequest request) {
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
