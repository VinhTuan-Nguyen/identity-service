package com.tuannv78.identity_service.model.repository;

import com.tuannv78.identity_service.common.entity.Permission;
import com.tuannv78.identity_service.common.enums.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, PermissionEnum> {
}
