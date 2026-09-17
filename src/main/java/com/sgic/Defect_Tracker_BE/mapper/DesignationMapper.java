package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreateDesignationRequest;
import com.sgic.Defect_Tracker_BE.dto.DesignationDto;
import com.sgic.Defect_Tracker_BE.entity.Designation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DesignationMapper {

    public DesignationDto toDto(Designation entity) {
        if (entity == null) {
            return null;
        }
        return DesignationDto.builder()
                .id(entity.getId())
                .name(entity.getDesignationName())
                .designationName(entity.getDesignationName())
                .description(entity.getDescription())
                .build();
    }

    public List<DesignationDto> toDtoList(List<Designation> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Designation toEntity(CreateDesignationRequest request) {
        if (request == null) {
            return null;
        }
        return Designation.builder()
                .designationName(request.resolveName())
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(Designation entity, CreateDesignationRequest request) {
        if (entity == null || request == null) {
            return;
        }
        String name = request.resolveName();
        if (name != null && !name.isEmpty()) {
            entity.setDesignationName(name);
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
    }
}
