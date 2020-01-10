package jsonrpc.authserver.repository.token;

import jsonrpc.authserver.entities.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Modifying
    @Query("UPDATE RefreshToken t set t.enabled = true WHERE t.id = :id")
    void approveById(@Param("id")Long id);
}
