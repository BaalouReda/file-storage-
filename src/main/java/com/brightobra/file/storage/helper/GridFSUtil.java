package com.brightobra.file.storage.helper;

import com.brightobra.file.storage.pojo.Metadata;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.springframework.stereotype.Component;

@Component
public class GridFSUtil {

    public static GridFSUploadOptions convertToGridFSFileMetadata(Metadata metadata) {
        return  new GridFSUploadOptions()
                .metadata(new org.bson.Document()
                        .append("fileName", metadata.getFileName())
                        .append("fileSize", metadata.getFileSize())
                        .append("fileType", metadata.getFileType())
                        .append("isCompressed", metadata.isCompressed())
                        .append("isEncrypted", metadata.isEncrypted())
                        .append("version", metadata.getVersion())
                        .append("bucketName", metadata.getBucketName()));
    }
}
