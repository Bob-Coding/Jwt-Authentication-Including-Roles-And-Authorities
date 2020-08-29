package jwt.example.controller;

import jwt.example.userDto.UserDto;

public interface UserServiceInterface {
    UserDto createUser(UserDto user);
    UserDto getUserByUserId(String userId);
}
