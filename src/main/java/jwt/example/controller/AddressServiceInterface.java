package jwt.example.controller;

import jwt.example.userDto.AddressDto;

import java.util.List;

public interface AddressServiceInterface {
        List<AddressDto> getAddresses(String userId);
        AddressDto getAddress(String addressId);
}
