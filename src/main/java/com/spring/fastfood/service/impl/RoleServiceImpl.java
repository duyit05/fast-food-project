package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.RoleRequest;
import com.spring.fastfood.dto.response.RoleResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.model.Role;
import com.spring.fastfood.repository.RoleRepository;
import com.spring.fastfood.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleResponse> getAllRole() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(
                role -> RoleResponse.builder()
                        .roleId(role.getId())
                        .roleName(role.getRoleName())
                        .description(role.getDescription())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public RoleResponse createRole(RoleRequest request) {
        Role role = Role.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .build();
        roleRepository.save(role);
        return RoleResponse.builder()
                .roleId(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .build();
    }

    @Override
    public RoleResponse updateRoleById(long roleId, RoleRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("not found with role id: " + roleId));
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        roleRepository.save(role);
        return RoleResponse.builder()
                .roleId(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .build();
    }

    @Override
    public void deleteRoleById(long roleId) {
        roleRepository.deleteById(roleId);
    }
}

