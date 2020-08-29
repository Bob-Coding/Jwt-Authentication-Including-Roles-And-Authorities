package jwt.example.controller;

import jwt.example.userDto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServiceInterface extends UserDetailsService {
    UserDto createUser(UserDto user);
    UserDto getUserByUserId(String userId);
}
