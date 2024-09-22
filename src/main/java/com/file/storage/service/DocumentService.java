package com.file.storage.service;


import com.file.storage.dto.DocumentDto;
import com.file.storage.dto.UserDto;
import com.file.storage.helper.GridFSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@Service
@RequiredArgsConstructor
@Log4j2
public class DocumentService {

    private final UserService userService;
    private final GridFSUtil gridFSUtil;


    public DocumentDto uploadFile(MultipartFile fileContent, String userId) {
        UserDto userDto = userService.findById(userId);
        log.info("Uploading file {} for user {}",fileContent.getOriginalFilename(),userId);
        return gridFSUtil.upload(fileContent, userDto);
    }

    public DocumentDto downloadFile(String userId,String id) {
        UserDto userDto = userService.findById(userId);
        log.info("Downloading file with id: {} for user {}",id,userId);
        return gridFSUtil.download(id, userDto);
    }



}

