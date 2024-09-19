package com.brightobra.file.storage.dao;


import com.brightobra.file.storage.pojo.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class UserDao implements UserDetails {
    @Id
    String id;
    String username;
    String email;
    @JsonIgnore
    String password;
    Set<Role> authorities; // Roles are simplified for illustration
    boolean accountNonExpired;
    boolean isEnabled;
    boolean accountNonLocked;
    boolean credentialsNonExpired;
}
