package jwt.example.controller;

import jwt.example.dataTransferObject.UserDto;
import jwt.example.model.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService implements UserServiceInterface{
    @Autowired
    UserRepository userRepository;

    public Iterable<UserEntity> getAllUsers() {
        System.out.println("You Requested All Users");
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto createUser(UserDto user) {
        UserEntity userEntity = new UserEntity();
        //copy UserDtoObject fields into UserEntity
        BeanUtils.copyProperties(user, userEntity);

        //will add implementation for random userId and Bcrypt later
        //add missing fields from UserDto manual into UserEntity
        userEntity.setUserId("TestUserId");
        userEntity.setEncryptedPassword("TestPassword");
        //store created UserEntity in Database
        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    public String deleteUserById(long id) {
        System.out.println("You deleted userId: " + id);
        userRepository.deleteById(id);
        return "You Deleted User With Id: " + id;
    }

    public UserEntity updateUser(long id, UserEntity userInput) {
        System.out.println("Updating UserId: "+ id);
        UserEntity user = userRepository.findById(id).get();

        if (userInput.getFirstName() != null && userInput.getFirstName() !="") {
            user.setFirstName(userInput.getFirstName());
        }
        if (userInput.getLastName() != null && userInput.getLastName() !="") {
            user.setLastName(userInput.getLastName());
        }
        if (userInput.getEmail() != null && userInput.getEmail() !="") {
            user.setEmail(userInput.getEmail());
        }
        if (userInput.getEncryptedPassword() != null && userInput.getEncryptedPassword() !="" || userInput.getEncryptedPassword() != null) {
            user.setEncryptedPassword(userInput.getEncryptedPassword());
            //cant change password(for now)
        }
        return userRepository.save(user);
    }
}