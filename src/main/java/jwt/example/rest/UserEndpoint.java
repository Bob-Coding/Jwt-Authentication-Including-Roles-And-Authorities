package jwt.example.rest;

import jwt.example.controller.UserService;
import jwt.example.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class UserEndpoint {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public Iterable <UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/users/{id}")
    public UserEntity getUserById(@PathVariable(value = "id")long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/register")
    public UserEntity addUser(@RequestBody UserEntity newUser) {
        return userService.addUser(newUser);
    }

    @PutMapping("/update/{id}")
    public UserEntity updateUser(@PathVariable(value="id")long id, @RequestBody UserEntity userInput) {
        return userService.updateUser(id, userInput);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUserById(@PathVariable(value= "id")long id) {
        userService.deleteUserById(id);
        return "You Deleted User With Id: " + id;
    }
}