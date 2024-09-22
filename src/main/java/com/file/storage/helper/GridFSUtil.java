package com.file.storage.helper;

import com.file.storage.dto.DocumentDto;
import com.file.storage.dto.UserDto;
import com.file.storage.pojo.Metadata;
import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.*;
import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@RequiredArgsConstructor
@Log4j2
public class GridFSUtil {

    private final GridFsTemplate gridFsTemplate;
    private SecretKey secretKey;

    @PostConstruct
    public void init() throws Exception {
        secretKey = EncryptionUtil.generateKey();
    }


    public  DocumentDto upload(MultipartFile fileContent, UserDto userDto) {
        try{

            InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
            InputStream encryptedStream = EncryptionUtil.encrypt(inputStream, secretKey);
            Metadata metadata = Metadata.builder()
                    .fileName(fileContent.getOriginalFilename())
                    .fileSize(fileContent.getSize())
                    .fileType(fileContent.getContentType())
                    .isCompressed(false)
                    .owner(userDto.getId())
                    .duplicateCount(0)
                    .isEncrypted(true)
                    .version(1)
                    .build();

            ObjectId objectId = gridFsTemplate.store(encryptedStream,
                    Objects.requireNonNull(fileContent.getOriginalFilename()),
                    metadata);

            return DocumentDto
                    .builder()
                    .id(objectId.toHexString())
                    .fileName(fileContent.getOriginalFilename())
                    .build();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public DocumentDto download(String id, UserDto userDto) {
        try {
            GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(id)));

            if(file == null){
                throw new RuntimeException("File not found");
            }

            if(!file.getMetadata().getString("owner").equals(userDto.getId())){
                throw new RuntimeException("Unauthorized access");
            }

            log.info("File found: {}",file.getFilename());
            GridFsResource GridFSResource = gridFsTemplate.getResource(file);
            InputStream inputStream = GridFSResource.getInputStream();
            InputStream decryptedStream  = EncryptionUtil.decrypt(inputStream, secretKey);
            ByteArrayResource resource =  new ByteArrayResource(decryptedStream.readAllBytes());

            assert file.getMetadata() != null;
            return DocumentDto.builder()
                    .id(file.getId().toString())
                    .fileName(file.getFilename())
                    .fileMetadata(Metadata.builder()
                            .fileName(file.getFilename())
                            .fileSize(file.getLength())
                            .fileType(file.getMetadata().getString("fileType"))
                            .isCompressed(file.getMetadata().getBoolean("isCompressed"))
                            .isEncrypted(file.getMetadata().getBoolean("isEncrypted"))
                            .version(file.getMetadata().getInteger("version"))
                            .build())
                    .file(resource)
                    .build();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
