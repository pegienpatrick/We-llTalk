package com.pegien.WellTalk.WellTalk.Profile.ProfilePhotos.Controller;



import com.pegien.WellTalk.WellTalk.Profile.ProfilePhotos.service.ProfilePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/UserPhoto")
public class ProfilePhotoController {

    @Autowired
    private ProfilePhotoService profilePhotoService;


    @PostMapping("/uploadUserPhoto")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile multipartFile) {
        return profilePhotoService.upload(multipartFile);
    }


    @GetMapping("/getUserPhoto")
    public ResponseEntity<byte[]> getPhoto() {
        return profilePhotoService.download();
    }

}
