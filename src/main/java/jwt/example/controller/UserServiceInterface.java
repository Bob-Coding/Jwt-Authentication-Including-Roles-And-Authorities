package jwt.example.controller;

import jwt.example.dataTransferObject.UserDto;

public interface UserServiceInterface {
    UserDto createUser(UserDto user);
}
