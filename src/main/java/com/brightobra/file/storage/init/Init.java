package com.brightobra.file.storage.init;

import com.brightobra.file.storage.dao.UserDao;
import com.brightobra.file.storage.dto.UserDto;
import com.brightobra.file.storage.pojo.Role;
import com.brightobra.file.storage.service.UserService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;

@Component
@Log4j2
@RequiredArgsConstructor
@Profile("dev")
public class Init implements CommandLineRunner  {
    private final UserService userService;


    @Override
    public void run(String... args) throws Exception {
        //String secretKey = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
       // log.info("Secret Key generated : {}", secretKey);
        UserDto admin = UserDto
                .builder()
                .email("brightobra.agency@gmail.com")
                .username("admin")
                .password("password")
                .role(Collections.singleton(Role.ROLE_ADMIN))
                .isEnabled(Boolean.TRUE)
                .accountNonExpired(Boolean.TRUE)
                .accountNonLocked(Boolean.TRUE)
                .credentialsNonExpired(Boolean.TRUE)
                .bucketName("admin-"+System.currentTimeMillis())
                .build();
        createUser(admin);

        UserDto user = UserDto
                .builder()
                .email("reda.baalou.dev@gmail.com")
                .username("reda")
                .password("password")
                .role(Collections.singleton(Role.ROLE_ADMIN))
                .isEnabled(Boolean.TRUE)
                .accountNonExpired(Boolean.TRUE)
                .accountNonLocked(Boolean.TRUE)
                .credentialsNonExpired(Boolean.TRUE)
                .bucketName("reda-"+System.currentTimeMillis())
                .build();
        createUser(user);

    }

    private void createUser(UserDto userDto) {
        UserDto user = userService.loadUserByEamil(userDto.getEmail());
        if(user == null){
            user = userService.save(userDto);
        }
        log.info("{} user created : {}",user.getUsername(), user.getId());
    }
}
