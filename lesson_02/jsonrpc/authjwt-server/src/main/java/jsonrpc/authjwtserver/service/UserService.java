package jsonrpc.authjwtserver.service;


import jsonrpc.authjwtserver.entities.User;
import jsonrpc.authjwtserver.repository.UserRepository;
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
