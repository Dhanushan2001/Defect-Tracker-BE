package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.DuplicateResourceException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateRoleRequest;
import com.sgic.Defect_Tracker_BE.dto.RoleDto;
import com.sgic.Defect_Tracker_BE.entity.Role;
import com.sgic.Defect_Tracker_BE.mapper.RoleMapper;
import com.sgic.Defect_Tracker_BE.repository.RoleRepository;
import com.sgic.Defect_Tracker_BE.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<RoleDto> getAllRoles(Pageable pageable) {
        Page<Role> page = roleRepository.findAll(pageable);
        return PaginatedData.<RoleDto>builder()
                .content(roleMapper.toDtoList(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .size(page.getSize())
                .number(page.getNumber())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getAllRolesList() {
        return roleMapper.toDtoList(roleRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        return roleMapper.toDto(role);
    }

    @Override
    public RoleDto createRole(CreateRoleRequest request) {
        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Role name cannot be empty");
        }

        if (roleRepository.existsByRoleNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Role already exists with name: " + name);
        }

        Role role = roleMapper.toEntity(request);
        Role saved = roleRepository.save(role);
        return roleMapper.toDto(saved);
    }

    @Override
    public RoleDto updateRole(Long id, CreateRoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Role name cannot be empty");
        }

        if (roleRepository.existsByRoleNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateResourceException("Role already exists with name: " + name);
        }

        roleMapper.updateEntity(role, request);
        Role updated = roleRepository.save(role);
        return roleMapper.toDto(updated);
    }

    @Override
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }
}
