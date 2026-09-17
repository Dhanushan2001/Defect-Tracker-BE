package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateRoleRequest;
import com.sgic.Defect_Tracker_BE.dto.RoleDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    PaginatedData<RoleDto> getAllRoles(Pageable pageable);
    List<RoleDto> getAllRolesList();
    RoleDto getRoleById(Long id);
    RoleDto createRole(CreateRoleRequest request);
    RoleDto updateRole(Long id, CreateRoleRequest request);
    void deleteRole(Long id);
}
