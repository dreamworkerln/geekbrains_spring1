package jsonrpc.authjwtserver.service;

import jsonrpc.authjwtserver.entities.Role;
import jsonrpc.authjwtserver.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void save(Role role) {
        roleRepository.save(role);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findOneByName(name);
    }

}
