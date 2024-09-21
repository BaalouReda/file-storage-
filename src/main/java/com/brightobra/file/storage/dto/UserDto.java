package com.brightobra.file.storage.dto;

import com.brightobra.file.storage.pojo.Role;
import com.brightobra.file.storage.pojo.UserDtoViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
   @JsonView(UserDtoViews.UserDtoResponse.class)
    String id;
    String username;
    @JsonView(UserDtoViews.UserDtoRequest.class)
    String email;
    @JsonView(UserDtoViews.UserDtoRequest.class)
    String password;
    @JsonView(UserDtoViews.UserDtoRequest.class)
    Role role;
    String bucketName;
    boolean accountNonExpired;
    boolean isEnabled;
    boolean accountNonLocked;
    boolean credentialsNonExpired;
}
