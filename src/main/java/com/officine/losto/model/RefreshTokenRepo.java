package com.officine.losto.model;

import com.officine.losto.entity.RefreshToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);

	List<RefreshToken> findByUser_IdAndRevokedFalse(Long userId);

	@Modifying
	@Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user.id = :userId AND rt.revoked = false")
	int revokeAllActiveForUser(@Param("userId") Long userId);

	@Modifying
	@Query("DELETE FROM RefreshToken rt WHERE rt.user.id = :userId")
	void deleteAllByUserId(@Param("userId") Long userId);
}
