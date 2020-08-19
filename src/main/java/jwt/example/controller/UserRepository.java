package jwt.example.controller;

import jwt.example.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends CrudRepository<UserEntity, Long>{
    UserEntity findByUserId(String userId);
}