package jwt.example.controller;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import jwt.example.userDto.AddressDto;
import jwt.example.userDto.UserDto;
import jwt.example.model.UserEntity;
import jwt.example.userDto.Utils;
import jwt.example.userDto.response.UserDetailsResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService implements UserServiceInterface {
    @Autowired
    UserRepository userRepository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists");
        for(int i=0; i<user.getAddresses().size(); i++) {
            AddressDto address = user.getAddresses().get(i);
            address.setUserDetails(user);
            address.setAddressId(utils.generateAddressId(25));
            user.getAddresses().set(i, address);
        }
//        BeanUtils.copyProperties(user, userEntity);  modelmapper is better for deep cloning
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        String publicUserId = utils.generateUserId(25);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //store created UserEntity in Database
        UserEntity storedUserDetails = userRepository.save(userEntity);
//        BeanUtils.copyProperties(storedUserDetails, returnValue);
        UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);
        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException("Record not found with userId: " + userId);
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();
        //page 1 is equal to 0 to avoid confusion for the client.
        if(page>0) page = page-1;
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();
        for(UserEntity userEntity : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }
        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException(userId);
        if (userDto.getFirstName() != null && userDto.getFirstName() != "")
            userEntity.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null && userDto.getLastName() != "")
            userEntity.setLastName(userDto.getLastName());

        UserEntity updatedUser = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedUser, returnValue);
        System.out.println("Updating UserId: " + userId);
        return returnValue;
    }

    public Iterable<UserEntity> getAllUsers() {
        System.out.println("You Requested All Users");
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException(userId);
        userRepository.delete(userEntity);
        System.out.println("You deleted userId: " + userId);
    }
}