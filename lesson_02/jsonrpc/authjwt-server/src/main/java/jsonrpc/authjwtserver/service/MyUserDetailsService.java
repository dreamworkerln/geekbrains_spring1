package jsonrpc.authjwtserver.service;

import jsonrpc.authjwtserver.entities.User;
import jsonrpc.authjwtserver.entities.MyUserPrincipal;
import jsonrpc.authjwtserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findOneByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new MyUserPrincipal(user);
    }
}
