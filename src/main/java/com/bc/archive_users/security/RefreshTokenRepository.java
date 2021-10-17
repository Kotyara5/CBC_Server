package com.bc.archive_users.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByToken(String token);

    @Query(value = "DELETE FROM refreshtoken WHERE user_id = ?1", nativeQuery = true)
    void deleteAllRefreshToken(Long user_id);
}
