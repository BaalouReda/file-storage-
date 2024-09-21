package com.brightobra.file.storage.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.crypto.SecretKey;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Metadata {
    String fileName;
    String fileType;
    String owner;
    long fileSize;
    boolean isEncrypted;
    boolean isCompressed;
    String bucketName;
    int duplicateCount;
    int version;
    String secretKey;
}
