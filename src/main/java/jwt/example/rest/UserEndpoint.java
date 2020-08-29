package jwt.example.rest;

import jwt.example.controller.UserService;
import jwt.example.userDto.UserDto;
import jwt.example.userDto.UserDetailsRequestModel;
import jwt.example.userDto.UserDetailsResponseModel;
import jwt.example.model.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;


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
    public UserDetailsResponseModel getUserByUserId(@PathVariable(value = "id")String id) {
        UserDetailsResponseModel returnValue = new UserDetailsResponseModel();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);
        return returnValue;
    }

    //Example for taking xml/json as a request(consumes) and to respond in xml or json, first value is default( in this example json )
    //Add Maven dependency Jackson Dataformat XML and add header for sending xml 'Content-Type application/xml' And 'Accept application/xml' for receiving back xml
    @PostMapping(value = "/users",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDetailsResponseModel createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails , userDto);
        UserDetailsResponseModel returnValue = new UserDetailsResponseModel();
        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);
        System.out.println("Added UserEntity");
        return returnValue;
    }

    @PutMapping("/users/{id}")
    public UserEntity updateUser(@PathVariable(value="id")long id, @RequestBody UserEntity userInput) {
        return userService.updateUser(id, userInput);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUserById(@PathVariable(value= "id")long id) {
        userService.deleteUserById(id);
        return "You Deleted User With Id: " + id;
    }
}