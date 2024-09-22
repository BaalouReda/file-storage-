package com.file.storage.web.rest;


import com.file.storage.dto.DocumentDto;
import com.file.storage.pojo.FileDtoViews;
import com.file.storage.service.DocumentService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/document/")
public class DocumentController {
    private final DocumentService documentService;

    @RequestMapping(
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = { "multipart/form-data"},
            path = "{user}"
    )
    @JsonView(FileDtoViews.DocumentDtoUpload.class)
    public ResponseEntity<?> uploadFile(
            @PathVariable String user,
            @RequestPart("file") MultipartFile file
    ) {
        try {
            return new  ResponseEntity<>(documentService.uploadFile(file,user), HttpStatus.OK) ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = "application/json",
            path = "{user}/{id}"
    )
    @JsonView(FileDtoViews.DocumentDtoDownload.class)
    public ResponseEntity<?> downloadFile(
            @PathVariable String user,
            @PathVariable String id
    ) {
        try {
            DocumentDto documentDto = documentService.downloadFile(user,id);
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\""
                            + FilenameUtils.getBaseName(documentDto.getFileName())
                            + "\"");
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");
            header.add("metadata", documentDto.getFileMetadata().toString());
            return ResponseEntity.ok()
                    .headers(header)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(documentDto.getFile());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
