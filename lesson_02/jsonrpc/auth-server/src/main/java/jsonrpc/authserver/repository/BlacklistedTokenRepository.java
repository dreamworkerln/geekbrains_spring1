package jsonrpc.authserver.repository;

import jsonrpc.authserver.entities.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    @Modifying
    @Query("DELETE FROM BlacklistedToken t WHERE t.expiredAt < CURRENT_TIMESTAMP")
    void vacuum();

    List<BlacklistedToken> findByIdGreaterThanEqual(@Param("from")Long from);
}
