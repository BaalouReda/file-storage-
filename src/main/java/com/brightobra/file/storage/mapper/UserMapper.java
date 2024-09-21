package com.brightobra.file.storage.mapper;

import com.brightobra.file.storage.dao.UserDao;
import com.brightobra.file.storage.dto.UserDto;

public class UserMapper {
    public static UserDao toDao(UserDto user) {
       return UserDao.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .accountNonExpired(user.isAccountNonExpired())
                .isEnabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .id(user.getId())
                .password(user.getPassword())
                .bucketName(user.getBucketName())
                .build();
    }

    public static UserDto toDto(UserDao userDao) {
        return UserDto.builder()
                .email(userDao.getEmail())
                .username(userDao.getUsername())
                .role(userDao.getRole())
                .accountNonExpired(userDao.isAccountNonExpired())
                .isEnabled(userDao.isEnabled())
                .accountNonLocked(userDao.isAccountNonLocked())
                .credentialsNonExpired(userDao.isCredentialsNonExpired())
                .id(userDao.getId())
                .password(userDao.getPassword())
                .bucketName(userDao.getBucketName())
                .build();
    }
}
