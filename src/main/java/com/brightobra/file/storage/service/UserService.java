package com.brightobra.file.storage.service;

import com.brightobra.file.storage.dao.UserDao;
import com.brightobra.file.storage.dto.UserDto;
import com.brightobra.file.storage.mapper.UserMapper;
import com.brightobra.file.storage.repositorie.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.brightobra.file.storage.pojo.Role;

@Service
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository userRepository;


    public UserDto save(UserDto userDto){
        UserDao userDao = new UserDao();
        userDao.setEmail(userDto.getEmail());
        userDao.setUsername(userDto.getUsername());
        userDao.setPassword(userDto.getPassword());
        userDao.setRole(userDto.getRole());
        userDao.setEnabled(Boolean.TRUE);
        userDao.setAccountNonExpired(Boolean.TRUE);
        userDao.setAccountNonLocked(Boolean.TRUE);
        userDao.setCredentialsNonExpired(Boolean.TRUE);
        userDao.setBucketName(userDto.getUsername()+"-"+System.currentTimeMillis());
        UserDao saved = userRepository.save(userDao);

        return UserDto.builder()
                .email(saved.getEmail())
                .username(saved.getUsername())
                .role(saved.getRole())
                .accountNonExpired(saved.isAccountNonExpired())
                .isEnabled(saved.isEnabled())
                .accountNonLocked(saved.isAccountNonLocked())
                .credentialsNonExpired(saved.isCredentialsNonExpired())
                .id(saved.getId())
                .password(saved.getPassword())
                .bucketName(saved.getBucketName())
                .build();
    }


    public UserDto findById(String id){
        UserDao userDao =  userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        return UserMapper.toDto(userDao);
    }


    public UserDto loadUserByEamil(String email) {
        UserDao userDao = userRepository.findByEmail(email).orElse(null);
        if (userDao == null) {
            return null;
        }
        return UserMapper.toDto(userDao);
    }

    public UserDto findByRole(String role){
        UserDao userDao =  userRepository.findByRole(Role.valueOf(role)).orElseThrow(()->new RuntimeException("User not found"));
        return UserMapper.toDto(userDao);
    }

}
