package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.RoleRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.RoleResponse;
import com.spring.fastfood.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/role")
@RestController
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<List<RoleResponse>> getAllRole (){
        return new DataResponse<>(HttpStatus.OK.value(), "get all role",roleService.getAllRole());
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<RoleResponse> createRole (@RequestBody RoleRequest request){
        return new DataResponse<>(HttpStatus.CREATED.value(), "create role",roleService.createRole(request));
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<RoleResponse> createRole (@PathVariable long roleId, @RequestBody RoleRequest request){
        return new DataResponse<>(HttpStatus.CREATED.value(), "create role",roleService.updateRoleById(roleId,request));
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<?> deleteRoleById (@PathVariable long roleId){
        roleService.deleteRoleById(roleId);
        return new DataResponse<>(HttpStatus.CREATED.value(), "delete role");
    }
}
