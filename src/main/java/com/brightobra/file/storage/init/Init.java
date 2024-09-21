package com.brightobra.file.storage.init;

import com.brightobra.file.storage.dto.UserDto;
import com.brightobra.file.storage.pojo.Role;
import com.brightobra.file.storage.service.UserService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Log4j2
@RequiredArgsConstructor
public class Init implements CommandLineRunner  {
    private final UserService userService;


    @Override
    public void run(String... args) throws Exception {
        String secretKey = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
        log.info("Secret Key generated : {}", secretKey);
        UserDto userDto = UserDto
                .builder()
                .email("brightobra.agency@gmail.com")
                .username("admin")
                .password("password")
                .role(Role.ROLE_ADMIN)
                .isEnabled(Boolean.TRUE)
                .accountNonExpired(Boolean.TRUE)
                .accountNonLocked(Boolean.TRUE)
                .credentialsNonExpired(Boolean.TRUE)
                .bucketName("admin-"+System.currentTimeMillis())
                .build();
        if(userService.loadUserByEamil(userDto.getEmail()) == null) userService.save(userDto);
    }
}
