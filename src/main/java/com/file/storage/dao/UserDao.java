package com.file.storage.dao;


import com.file.storage.pojo.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user")
public class UserDao  {
    @Id
    String id;
    String username;
    String email;
    @JsonIgnore
    String password;
    Set<Role> role;
    String bucketName;
    boolean accountNonExpired;
    boolean isEnabled;
    boolean accountNonLocked;
    boolean credentialsNonExpired;

}
