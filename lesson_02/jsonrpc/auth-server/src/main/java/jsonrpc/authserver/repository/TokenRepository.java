package jsonrpc.authserver.repository;

import jsonrpc.authserver.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Modifying
    @Query("UPDATE Token t set t.enabled = true WHERE t.id = :id")
    void ApproveTokenById(@Param("id")Long id);
}
