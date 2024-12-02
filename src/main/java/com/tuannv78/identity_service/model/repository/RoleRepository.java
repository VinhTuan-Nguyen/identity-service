package com.tuannv78.identity_service.model.repository;

import com.tuannv78.identity_service.common.entity.Role;
import com.tuannv78.identity_service.common.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleEnum> {}
