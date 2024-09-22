package com.file.storage.dto;

import com.file.storage.pojo.FileDtoViews;
import com.file.storage.pojo.Metadata;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ByteArrayResource;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class DocumentDto {
    @JsonView(FileDtoViews.DocumentDtoUpload.class)
    String id;
    @JsonView(FileDtoViews.DocumentDtoUpload.class)
    String fileName;
    @JsonView(FileDtoViews.DocumentDtoDownload.class)
    ByteArrayResource file;
    @JsonView(FileDtoViews.DocumentDtoDownload.class)
    Metadata fileMetadata;
}
