package jwt.example.rest;

import jwt.example.controller.UserService;
import jwt.example.model.UserDetailsRequestModel;
import jwt.example.model.UserDetailsResponseModel;
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

    @PostMapping("/new-user")
    public UserDetailsResponseModel addUser(@RequestBody UserDetailsRequestModel userDetails) {
        return null;
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