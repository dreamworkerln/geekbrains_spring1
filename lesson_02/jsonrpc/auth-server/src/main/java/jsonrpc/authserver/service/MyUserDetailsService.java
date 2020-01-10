package jsonrpc.authserver.service;

import jsonrpc.authserver.entities.User;
import jsonrpc.authserver.entities.MyUserPrincipal;
import jsonrpc.authserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findOneByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new MyUserPrincipal(user);
    }
}
