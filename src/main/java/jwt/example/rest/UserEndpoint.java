package jwt.example.rest;

import jwt.example.controller.AddressService;
import jwt.example.controller.UserService;
import jwt.example.exceptionHandling.ErrorMessages;
import jwt.example.exceptionHandling.UserServiceException;
import jwt.example.model.UserEntity;
import jwt.example.userDto.AddressDto;
import jwt.example.userDto.Roles;
import jwt.example.userDto.response.OperationStatusResponseModel;
import jwt.example.userDto.UserDto;
import jwt.example.userDto.request.UserDetailsRequestModel;
import jwt.example.userDto.response.UserDetailsResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserEndpoint {
    @Autowired
    UserService userService;
    @Autowired
    AddressService addressService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/users")
    public List<UserDetailsResponseModel> getUsers(@RequestParam(value="page", defaultValue = "1") int page,
                                                   @RequestParam(value="limit", defaultValue = "25") int limit) {
        List<UserDetailsResponseModel> returnValue = new ArrayList<>();
        List<UserDto> users = userService.getUsers(page, limit);
        Type listType = new TypeToken<List<UserDetailsResponseModel>>() {}.getType();
        returnValue = new ModelMapper().map(users, listType);
    //        for(UserDto userDto : users) {
    //            UserDetailsResponseModel userModel = new UserDetailsResponseModel();
    //            BeanUtils.copyProperties(userDto, userModel);
    //            returnValue.add(userModel);
    //        }
        return returnValue;
    }

    @PreAuthorize("hasAnyRole('ADMIN')or #userId == principal.userId")
    @GetMapping("/users/{userId}")
    public UserDetailsResponseModel getUserByUserId(@PathVariable(value = "userId")String userId) {
        UserDetailsResponseModel returnValue = new UserDetailsResponseModel();
        UserDto userDto = userService.getUserByUserId(userId);
        ModelMapper modelMapper = new ModelMapper();
        returnValue = modelMapper.map(userDto, UserDetailsResponseModel.class);
        return returnValue;
    }

    //Example for taking xml/json as a request(consumes) and to respond in xml or json, first value is default( in this example json )
    //Add Maven dependency Jackson Dataformat XML and add header for sending xml 'Content-Type application/xml' And 'Accept application/xml' for receiving back xml
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(value = "/users",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDetailsResponseModel createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
            UserDetailsResponseModel returnValue = new UserDetailsResponseModel();
            if(userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty() || userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty() || userDetails.getAddresses() == null)
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        //        Other way of copying properties with modelmapper(mvn dependency added) to make deep clones
        //        UserDto userDto = new UserDto();
        //        BeanUtils.copyProperties(userDetails , userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));
        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserDetailsResponseModel.class);
        System.out.println("Added UserEntity");
        return returnValue;
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == principal.userId")
    @PutMapping("/users/{userId}")
    public UserDetailsResponseModel updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetailsRequest) {
        UserDetailsResponseModel returnValue = new UserDetailsResponseModel();
        UserDto userDto = new UserDto();
        userDto = new ModelMapper().map(userDetailsRequest, UserDto.class);
        UserDto updatedUser = userService.updateUser(userId, userDto);
        returnValue= new ModelMapper().map(updatedUser, UserDetailsResponseModel.class);
        return returnValue;
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == principal.userId")
    @DeleteMapping("/users/{userId}")
    public OperationStatusResponseModel deleteUserById(@PathVariable(value= "userId")String userId) {
        OperationStatusResponseModel returnValue = new OperationStatusResponseModel();
        returnValue.setOperationName("DELETE");
        userService.deleteUser(userId);
        returnValue.setOperationResult("SUCCESS");
        return returnValue;
    }
}