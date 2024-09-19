package com.brightobra.file.storage.service;

import com.brightobra.file.storage.dao.UserDao;
import com.brightobra.file.storage.repositorie.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.brightobra.file.storage.pojo.Role;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDao loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDao findByRole(String role){
        return userRepository.findByRole(Role.valueOf(role)).orElseThrow(()->new RuntimeException("User not found"));
    }
}
