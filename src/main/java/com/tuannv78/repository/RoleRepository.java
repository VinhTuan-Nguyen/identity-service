package com.tuannv78.repository;

import com.tuannv78.entity.Role;
import com.tuannv78.identity_service.common.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleEnum> {}
