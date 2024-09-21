package com.brightobra.file.storage.service;

import com.brightobra.file.storage.configuration.security.OpenBSDBCryptPasswordEncoder;
import com.brightobra.file.storage.dao.UserDao;
import com.brightobra.file.storage.dto.UserDto;
import com.brightobra.file.storage.repositorie.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.brightobra.file.storage.pojo.Role;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserDto save(UserDto userDto){
        UserDao userDao = new UserDao();
        userDao.setEmail(userDto.getEmail());
        userDao.setUsername(userDto.getUsername());
        userDao.setPassword(encoder.encode(userDto.getPassword()));
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

    public UserDao getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return this.findByRole(auth.getName());
    }

    public UserDao findById(String id){
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
    }


    public UserDao loadUserByEamil(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDao findByRole(String role){
        return userRepository.findByRole(Role.valueOf(role)).orElseThrow(()->new RuntimeException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }
}
