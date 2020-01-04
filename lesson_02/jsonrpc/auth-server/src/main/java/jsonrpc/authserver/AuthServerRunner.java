package jsonrpc.authserver;

import jsonrpc.authserver.entities.Role;
import jsonrpc.authserver.entities.User;
import jsonrpc.authserver.service.RoleService;
import jsonrpc.authserver.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AuthServerRunner implements ApplicationRunner {

    private final UserService userService;
    private final RoleService roleService;


    public AuthServerRunner(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @Override
    public void run(ApplicationArguments args) {

        initRoles();
        
        initUsers();
    }

    private void initRoles() {
//
//        Role refresh = new Role(Role.REFRESH);
//        roleService.save(refresh);

        Role anonymous = new Role(Role.ANONYMOUS);
        roleService.save(anonymous);

        Role user = new Role(Role.USER);
        roleService.save(user);

        Role admin = new Role(Role.ADMIN);
        roleService.save(admin);
    }


    private void initUsers() {

        User user = new User();
        user.setName("user");
        user.setPassword("{bcrypt}$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6");//password
        user.getRoles().add(roleService.findByName(Role.USER));
        userService.save(user);

        user = new User();
        user.setName("admin");
        user.setPassword("{bcrypt}$2y$10$3UKKfqyHoDe8MbVIkXr.UO8d76bJWisYP5DdC3EpSzro.JYzi38xu");//password
        user.getRoles().add(roleService.findByName(Role.ADMIN));
        userService.save(user);
    }



}
