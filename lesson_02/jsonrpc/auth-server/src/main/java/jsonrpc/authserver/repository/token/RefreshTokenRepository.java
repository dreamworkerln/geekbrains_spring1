package jsonrpc.authserver.repository.token;

import jsonrpc.authserver.entities.User;
import jsonrpc.authserver.entities.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void findAllByUserName(String userName);



//    @Modifying
//    @Query("UPDATE RefreshToken t set t.enabled = true WHERE t.id = :id")
//    void approveById(@Param("id")Long id);

    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expiredAt < CURRENT_TIMESTAMP")
    void vacuum();

    void deleteByUserName(String userName);
    void deleteByUser(User user);
}
