package com.spring.fastfood.service.impl;

import com.spring.fastfood.model.Role;
import com.spring.fastfood.repository.RoleRepository;
import com.spring.fastfood.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Override
    public List<Role> findAll() {

        return roleRepository.findAll();
    }
}

