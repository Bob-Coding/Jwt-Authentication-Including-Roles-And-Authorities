package jwt.example.rest;

import jwt.example.controller.AddressService;
import jwt.example.userDto.AddressDto;
import jwt.example.userDto.response.AddressResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressEndpoint {
    @Autowired
    AddressService addressService;

    @GetMapping("/users/{userId}/addresses")
    public List<AddressResponseModel> getUserAddresses(@PathVariable(value = "userId")String userId) {
        List<AddressResponseModel> returnValue = new ArrayList<>();
        List<AddressDto> addressesDto = addressService.getAddresses(userId);
        if(addressesDto != null && !addressesDto.isEmpty()) {
            Type listType = new TypeToken<List<AddressResponseModel>>() {}.getType();
            returnValue = new ModelMapper().map(addressesDto, listType);
        }
        return returnValue;
    }

    @GetMapping("/users/{userId}/addresses/{addressId}")
    public AddressResponseModel getUserAddress(@PathVariable(value = "addressId")String addressId) {
        AddressDto addressDto = addressService.getAddress(addressId);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(addressDto, AddressResponseModel.class);
    }
}
