package com.spring.fastfood.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleResponse {
    private long roleId;
    private String roleName;
    private String description;
}
