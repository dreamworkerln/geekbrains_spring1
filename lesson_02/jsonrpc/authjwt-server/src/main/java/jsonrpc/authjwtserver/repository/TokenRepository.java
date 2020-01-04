package jsonrpc.authjwtserver.repository;

import jsonrpc.authjwtserver.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Modifying
    @Query("UPDATE Token t set t.approved = true WHERE t.id = :id")
    void ApproveTokenById(@Param("id")Long id);
}
