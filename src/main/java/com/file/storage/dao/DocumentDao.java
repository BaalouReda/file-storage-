package com.file.storage.dao;


import com.file.storage.pojo.Metadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "file")
public class DocumentDao {
    @Id
    String id;
    Metadata fileMetadata;
    //DataBuffer fileContent;
}
