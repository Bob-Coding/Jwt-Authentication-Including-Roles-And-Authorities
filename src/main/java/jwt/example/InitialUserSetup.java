package jwt.example;

import jwt.example.controller.AuthorityRepository;
import jwt.example.controller.RoleRepository;
import jwt.example.controller.UserRepository;
import jwt.example.model.AuthorityEntity;
import jwt.example.model.RoleEntity;
import jwt.example.model.UserEntity;
import jwt.example.userDto.Roles;
import jwt.example.userDto.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

@Component
public class InitialUserSetup {
    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    //registers this class and method on application event with springframework, when sf broadcasts an event that app has initialised all beans and components(applicationready), which will make this method run
    @EventListener
    //transactional is needed because of multiple modified queries within the same onApplication and then function
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        AuthorityEntity readUserAuthority = createAuthority("READ_USER");
        AuthorityEntity createUserAuthority = createAuthority("CREATE_USER");
        AuthorityEntity updateUserAuthority = createAuthority("UPDATE_USER");
        AuthorityEntity deleteUserAuthority = createAuthority("DELETE_USER");

        RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readUserAuthority, createUserAuthority,updateUserAuthority,deleteUserAuthority));
        RoleEntity roleUser = createRole(Roles.ROLE_USER.name(), Arrays.asList(readUserAuthority,updateUserAuthority, createUserAuthority));

        //create userentity with adminrole
        if (userRepository.findByEmail("admin@test.nl") == null) {
            UserEntity adminUser = new UserEntity();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("InitialAccount");
            adminUser.setEmail("admin@test.nl");
            adminUser.setRoles(Arrays.asList(roleAdmin));
            adminUser.setUserId(utils.generateUserId(20));
            adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("admin"));
            userRepository.save(adminUser);
        } return;
    }

    //function to create authority in table and save it in db
    @Transactional
    public AuthorityEntity createAuthority(String name) {
        //check if record already exists to prevend duplicates
        AuthorityEntity authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    //function to create role in table, and then add a collection of authorities to the field authorities in role and then save it in db
    @Transactional
    public RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
        //check if record already exists to prevend duplicates
        RoleEntity role = roleRepository.findByName(name);
        if (role == null) {
            role = new RoleEntity(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
