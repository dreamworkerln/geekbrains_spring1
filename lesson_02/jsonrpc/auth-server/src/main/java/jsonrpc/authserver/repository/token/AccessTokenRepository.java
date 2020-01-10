package jsonrpc.authserver.repository.token;

import jsonrpc.authserver.entities.token.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    @Modifying
    @Query("DELETE FROM AccessToken t WHERE t.expiredAt < CURRENT_TIMESTAMP")
    void vacuum();
}
