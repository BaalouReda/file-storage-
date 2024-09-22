package com.file.storage.init;

import com.file.storage.dto.UserDto;
import com.file.storage.pojo.Role;
import com.file.storage.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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
