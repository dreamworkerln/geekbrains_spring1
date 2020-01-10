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
    @Query("DELETE FROM BlacklistedToken bt WHERE bt.created < :i")
    void vacuum(@Param("i")Instant i);

    List<BlacklistedToken> findByIdGreaterThanEqual(@Param("from")Long from);
}
