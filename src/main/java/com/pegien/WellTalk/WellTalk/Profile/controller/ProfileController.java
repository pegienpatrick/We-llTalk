package com.pegien.WellTalk.WellTalk.Profile.controller;

import com.pegien.WellTalk.WellTalk.Profile.entity.Profile;
import com.pegien.WellTalk.WellTalk.Profile.models.requests.LoginRequest;
import com.pegien.WellTalk.WellTalk.Profile.models.requests.RegisterProfileRequest;
import com.pegien.WellTalk.WellTalk.Profile.models.responses.LoginResponseModel;
import com.pegien.WellTalk.WellTalk.Profile.service.ProfilesService;
import com.pegien.WellTalk.WellTalk.Utils.MyUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    private ProfilesService profilesService;

    @RequestMapping("/ping")
    public ResponseEntity<String> ping()
    {
        return ResponseEntity.ok("Ping");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseModel> loginUser(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()||loginRequest==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LoginResponseModel.builder().message(MyUtils.createErrorMessage(bindingResult)).build());

        return profilesService.loginProfile(loginRequest);
    }


    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterProfileRequest registerRequest, BindingResult bindingResult)
    {

        if(bindingResult.hasErrors()||registerRequest==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MyUtils.createErrorMessage(bindingResult));
        else
            return profilesService.registerProfile(registerRequest);
    }

    @GetMapping("/userInfo")
    public ResponseEntity<Profile> userInfo()
    {
        Profile profile=profilesService.currentProfile();
        if(profile==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(profile);
        else
            return ResponseEntity.ok(profile);
    }

    @GetMapping("/userInfo/{userId}")
    public ResponseEntity<Profile> userInfo(@PathVariable("userId")UUID userId)
    {
        return profilesService.userInfo(userId);
    }

    @GetMapping("/searchUser/{username}")
    public ResponseEntity<Profile> searchUser(@PathVariable("username") String username)
    {
        return profilesService.searchUser(username);
    }

    @GetMapping("/listUsers")
    public ResponseEntity<List<Profile>> listUsers()
    {
        return profilesService.listUsers();
    }






}
