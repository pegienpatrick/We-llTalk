package com.pegien.WellTalk.WellTalk.Profile.ProfilePhotos.service;



import com.pegien.WellTalk.WellTalk.Auth.AuthService;
import com.pegien.WellTalk.WellTalk.Profile.entity.Profile;
import com.pegien.WellTalk.WellTalk.Profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfilePhotoService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AuthService authService;


    @Value("UserPhotosFolder")
    private String UserPhotosFolder;


    public ResponseEntity<String> upload(MultipartFile multipartFile) {
        File saveFolder=new File(UserPhotosFolder);
        try{
            if(!saveFolder.exists())
                saveFolder.mkdirs();
        }catch (Exception es)
        {
            es.printStackTrace();
        }
        try {
            File saveFile = new File(saveFolder, "User" + authService.getActiveProfile().getUid());
            Files.copy(multipartFile.getInputStream(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("Uploaded Successfully");
        }catch (Exception es)
        {
            return ResponseEntity.status(500).body(es.getStackTrace().toString());
        }

    }

    public ResponseEntity<byte[]> download() {

        File saveFolder=new File(UserPhotosFolder);
        File saveFile = new File(saveFolder, "User" +authService.getActiveProfile().getUid());

        try {
            byte[] imageBytes;
            if (saveFile.exists())
                imageBytes = new FileInputStream(saveFile).readAllBytes();

            else
                imageBytes=getClass().getResourceAsStream("/defaultPerson/default.png").readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            String fileName = saveFile.exists()?saveFile.getName():"default";
            headers.setContentDispositionFormData("attachment", fileName+".png");

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }catch (Exception es)
        {
            es.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }


    public File getUserImage(UUID userId)
    {
        Optional<Profile> optionalProfile=profileRepository.findById(userId);
        if(optionalProfile.isEmpty())
           return (null);
        File saveFolder=new File(UserPhotosFolder);
        File saveFile = new File(saveFolder, "User" + userId);
        if(saveFile.exists())
            return saveFile;
        else
            return new File(getClass().getResource("/defaultPerson/default.png").getFile());
    }







}
