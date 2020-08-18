package jwt.example.rest;

import jwt.example.controller.UserService;
import jwt.example.dataTransferObject.UserDto;
import jwt.example.model.UserDetailsRequestModel;
import jwt.example.model.UserDetailsResponseModel;
import jwt.example.model.UserEntity;
import org.springframework.beans.BeanUtils;
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

    @PostMapping("/users/add")
    public UserDetailsResponseModel createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserDto userDto = new UserDto();
        //copy requestbody into userDtoObject
        BeanUtils.copyProperties(userDetails , userDto);

        UserDetailsResponseModel returnValue = new UserDetailsResponseModel();
        //add remaining fields in userDtoObject and copy it to UserEntity in the method createUser
        UserDto createdUser = userService.createUser(userDto);
        //copy the created user into an return value for client
        BeanUtils.copyProperties(createdUser, returnValue);
        System.out.println("Added UserEntity");
        return returnValue;
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