package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreateRoleRequest;
import com.sgic.Defect_Tracker_BE.dto.RoleDto;
import com.sgic.Defect_Tracker_BE.entity.Role;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    public RoleDto toDto(Role entity) {
        if (entity == null) {
            return null;
        }
        return RoleDto.builder()
                .id(entity.getId())
                .name(entity.getRoleName())
                .roleName(entity.getRoleName())
                .type(entity.getRoleType())
                .roleType(entity.getRoleType())
                .description(entity.getDescription())
                .build();
    }

    public List<RoleDto> toDtoList(List<Role> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Role toEntity(CreateRoleRequest request) {
        if (request == null) {
            return null;
        }
        return Role.builder()
                .roleName(request.resolveName())
                .roleType(request.resolveType())
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(Role entity, CreateRoleRequest request) {
        if (entity == null || request == null) {
            return;
        }
        String name = request.resolveName();
        if (name != null && !name.isEmpty()) {
            entity.setRoleName(name);
        }
        if (request.resolveType() != null) {
            entity.setRoleType(request.resolveType());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
    }
}
