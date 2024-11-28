package com.tuannv78.identity_service.model.repository;

import com.tuannv78.identity_service.common.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
}
