package com.brightobra.file.storage.service;


import com.brightobra.file.storage.dto.DocumentDto;
import com.brightobra.file.storage.helper.EncryptionUtil;
import com.brightobra.file.storage.helper.GridFSUtil;
import com.brightobra.file.storage.pojo.Metadata;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;
    private final GridFSBucket gridFSBucket;
    private SecretKey secretKey;

    @PostConstruct
    public void init() throws Exception {
        secretKey = EncryptionUtil.generateKey();
    }


    public DocumentDto uploadFile(MultipartFile fileContent) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        Metadata metadata = Metadata.builder()
                .fileName(fileContent.getOriginalFilename())
                .fileSize(fileContent.getSize())
                .fileType(fileContent.getContentType())
                .isCompressed(false)
                .bucketName(gridFSBucket.getBucketName())
                .isEncrypted(true)
                .version(1)
                .build();

        InputStream encryptedStream = EncryptionUtil.encrypt(inputStream, secretKey);

        GridFSUploadStream gridFSUploadStream = gridFSBucket.openUploadStream(
                Objects.requireNonNull(fileContent.getOriginalFilename()),
                GridFSUtil.convertToGridFSFileMetadata(metadata));

        gridFSUploadStream.write(encryptedStream.readAllBytes());
        gridFSUploadStream.close();

        //ObjectId objectId =  gridFsTemplate.store(encryptedStream, Objects.requireNonNull(fileContent.getOriginalFilename()), metadata);
         return  DocumentDto
                 .builder()
                 .id(
                         gridFSUploadStream.getObjectId().toHexString()
                         )
                 .fileName(fileContent.getOriginalFilename())
                 .build();
    }

    public DocumentDto downloadFile(String id) throws Exception {
        try (GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(new ObjectId(id))) {
            GridFSFile file = downloadStream.getGridFSFile();
            //GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(id)));
            InputStream inputStream = gridFsOperations.getResource(file).getContent();
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
        }
    }

}
