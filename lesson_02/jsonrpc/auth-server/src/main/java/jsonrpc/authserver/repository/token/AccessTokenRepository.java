package jsonrpc.authserver.repository.token;

import jsonrpc.authserver.entities.token.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
}
