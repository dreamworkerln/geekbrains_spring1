package jsonrpc.authserver.repository;

import jsonrpc.authserver.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Modifying
    @Query("UPDATE Token t set t.enabled = true WHERE t.id = :id")
    void ApproveTokenById(@Param("id")Long id);

    @Modifying
    @Query("DELETE FROM Token t WHERE " +
           "(t.type = jsonrpc.protocol.http.TokenType.ACCESS  AND t.created < :accessI ) OR " +
           "(t.type = jsonrpc.protocol.http.TokenType.REFRESH AND t.created < :refreshI)")
    void vacuum(@Param("accessI")Instant access, @Param("refreshI")Instant refresh);
}
