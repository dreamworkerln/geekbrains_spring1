package jsonrpc.authjwtserver;

import jsonrpc.authjwtserver.entities.Role;
import jsonrpc.authjwtserver.entities.User;
import jsonrpc.authjwtserver.service.RoleService;
import jsonrpc.authjwtserver.service.UserService;
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

        Role refresh = new Role(Role.REFRESH);
        roleService.save(refresh);

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
        user.setPassword("{bcrypt}$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6");
        user.getRoles().add(roleService.findByName(Role.USER).get());
        userService.save(user);
    }



}
