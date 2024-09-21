package com.brightobra.file.storage.dao;


import com.brightobra.file.storage.pojo.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    Role role;
    String bucketName;
    boolean accountNonExpired;
    boolean isEnabled;
    boolean accountNonLocked;
    boolean credentialsNonExpired;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(role != null)
            authorities.add(new SimpleGrantedAuthority(role.getValue()));
        return authorities;
    }
}
