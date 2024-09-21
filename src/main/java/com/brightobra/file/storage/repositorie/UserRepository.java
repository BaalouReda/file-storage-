package com.brightobra.file.storage.repositorie;

import com.brightobra.file.storage.dao.UserDao;
import com.brightobra.file.storage.pojo.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends   MongoRepository<UserDao,String> {
    Optional<UserDao> findByEmail(String email);
    Optional<UserDao> findByUsername(String username);
    Optional<UserDao> findByRole(Role role);
}
