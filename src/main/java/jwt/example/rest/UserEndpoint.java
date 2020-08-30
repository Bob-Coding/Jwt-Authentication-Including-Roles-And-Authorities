package jwt.example.rest;

import jwt.example.controller.UserService;
import jwt.example.userDto.OperationStatusResponseModel;
import jwt.example.userDto.UserDto;
import jwt.example.userDto.UserDetailsRequestModel;
import jwt.example.userDto.UserDetailsResponseModel;
import jwt.example.model.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class UserEndpoint {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<UserDetailsResponseModel> getUsers(@RequestParam(value="page", defaultValue = "1") int page,
                                                   @RequestParam(value="limit", defaultValue = "25") int limit) {
        List<UserDetailsResponseModel> returnValue = new ArrayList<>();
        List<UserDto> users = userService.getUsers(page, limit);

        for(UserDto userDto : users) {
            UserDetailsResponseModel userModel = new UserDetailsResponseModel();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }
        return returnValue;
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

    @PutMapping("/users/{userId}")
    public UserDetailsResponseModel updateGebruiker(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetailsRequest) {
        UserDetailsResponseModel returnValue = new UserDetailsResponseModel();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsRequest, userDto);
        UserDto updatedUser = userService.updateUser(userId, userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);
        return returnValue;
    }

    @DeleteMapping("/users/{userId}")
    public OperationStatusResponseModel deleteUserById(@PathVariable(value= "userId")String userId) {
        OperationStatusResponseModel returnValue = new OperationStatusResponseModel();
        returnValue.setOperationName("DELETE");
        userService.deleteUser(userId);
        returnValue.setOperationResult("SUCCESS");
        return returnValue;
    }
}