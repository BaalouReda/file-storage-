package com.brightobra.file.storage.dto;

import com.brightobra.file.storage.pojo.DtoViews;
import com.brightobra.file.storage.pojo.Metadata;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ByteArrayResource;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class DocumentDto {
    @JsonView(DtoViews.DocumentDtoUpload.class)
    String id;
    @JsonView(DtoViews.DocumentDtoUpload.class)
    String fileName;
    @JsonView(DtoViews.DocumentDtoDownload.class)
    ByteArrayResource file;
    @JsonView(DtoViews.DocumentDtoDownload.class)
    Metadata fileMetadata;
}
