package jsonrpc.authserver.service;


import jsonrpc.authserver.entities.User;
import jsonrpc.authserver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {

        userRepository.save(user);
    }

    public Optional<User> findByName(String name) {

        return userRepository.findOneByName(name);
    }


}
