package com.brightobra.file.storage.helper;

import com.brightobra.file.storage.dto.DocumentDto;
import com.brightobra.file.storage.dto.UserDto;
import com.brightobra.file.storage.pojo.Metadata;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@RequiredArgsConstructor
@Log4j2
public class GridFSUtil {

    private final MongoDatabaseFactory dbFactory;
    private final MongoConverter converter;
    private SecretKey secretKey;

    @PostConstruct
    public void init() throws Exception {
        secretKey = EncryptionUtil.generateKey();
    }

    public  DocumentDto upload(MultipartFile fileContent, UserDto userDto) {
        try{
            GridFsTemplate gridFsTemplate = new GridFsTemplate(dbFactory,converter, userDto.getBucketName());

            InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
            Metadata metadata = Metadata.builder()
                    .fileName(fileContent.getOriginalFilename())
                    .fileSize(fileContent.getSize())
                    .fileType(fileContent.getContentType())
                    .isCompressed(false)
                    .owner(userDto.getId())
                    .duplicateCount(0)
                    .bucketName(userDto.getBucketName())
                    .isEncrypted(true)
                    .version(1)
                    .build();


            InputStream encryptedStream = EncryptionUtil.encrypt(inputStream, secretKey);

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
            GridFsTemplate gridFsTemplate = new GridFsTemplate(dbFactory, converter, userDto.getBucketName());
            GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(id)));

            if(file == null || !userDto.getBucketName().equals(file.getMetadata().getString("bucketName"))){
                throw new RuntimeException("File not found");
            }

            log.info("File found: {}",file.getFilename());
            GridFsResource GridFSResource = gridFsTemplate.getResource(file);
            InputStream inputStream = GridFSResource.getInputStream();

            InputStream decryptedStream = EncryptionUtil.decrypt(inputStream, secretKey);

            ByteArrayResource resource = new ByteArrayResource(decryptedStream.readAllBytes());
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
                            .bucketName(file.getMetadata().getString("bucketName"))
                            .build())
                    .file(resource)
                    .build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
