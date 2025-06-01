package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.RoleRequest;
import com.spring.fastfood.dto.response.RoleResponse;
import com.spring.fastfood.model.Role;

import java.util.List;

public interface RoleService {

    RoleResponse createRole (RoleRequest request);
    List<RoleResponse> getAllRole ();
    RoleResponse updateRoleById (long roleId, RoleRequest roleRequest);
    void deleteRoleById (long roleId);
}
