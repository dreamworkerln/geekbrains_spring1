package jsonrpc.authserver.repository;

import jsonrpc.authserver.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByName(String name);
}
