package com.tuannv78.repository;

import com.tuannv78.entity.Permission;
import com.tuannv78.identity_service.common.enums.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
