package com.pegien.WellTalk.WellTalk.Profile.service;


import com.pegien.WellTalk.WellTalk.Auth.AuthService;
import com.pegien.WellTalk.WellTalk.Logs.service.LogService;
import com.pegien.WellTalk.WellTalk.Profile.ForgotPassword.PasswordResetCode;
import com.pegien.WellTalk.WellTalk.Profile.ForgotPassword.PasswordResetCodeRepository;
import com.pegien.WellTalk.WellTalk.Profile.entity.Profile;
import com.pegien.WellTalk.WellTalk.Profile.models.requests.*;
import com.pegien.WellTalk.WellTalk.Profile.models.responses.LoginResponseModel;
import com.pegien.WellTalk.WellTalk.Profile.repository.ProfileRepository;
import com.pegien.WellTalk.WellTalk.TWOFactor.TwoFactor;
import com.pegien.WellTalk.WellTalk.TWOFactor.TwoFactorRepository;
import com.pegien.WellTalk.WellTalk.Utils.EmailUtils;
import com.pegien.WellTalk.WellTalk.Utils.MyUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ProfilesService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private LogService logService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordResetCodeRepository passwordResetCodeRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TwoFactorRepository twoFactorRepository;


    public ResponseEntity<String> registerProfile(RegisterProfileRequest registerRequest) {
        Optional<Profile> optionalUsername=profileRepository.findByUsernameIgnoreCase(registerRequest.getUsername().trim());
        if(optionalUsername.isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already Exists");

        Profile profile= Profile.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername().trim().trim())
                .phone(registerRequest.getPhone())
                .profileName(registerRequest.getProfileName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
//                .phone(registerRequest.getPhone())
                .build();

        profileRepository.saveAndFlush(profile);

        logService.recordLog("Registering Profile : "+profile);

        return ResponseEntity.ok("Registered Successfully. Go to Login");
    }

    public ResponseEntity<String> updateProfile(UpdateRequest updateRequest) {
        Profile profile=authService.getActiveProfile();

        Optional<Profile> optionalUsername=profileRepository.findByUsernameIgnoreCase(updateRequest.getUsername().trim());
        if(!profile.getUsername().equals(updateRequest.getUsername())&&optionalUsername.isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already Exists");



        profile.setEmail(updateRequest.getEmail());
        profile.setPhone(updateRequest.getPhone());
        profile.setProfileName(updateRequest.getProfileName());


        profile.setUsername(updateRequest.getUsername().trim());
        profileRepository.save(profile);

        return ResponseEntity.ok("Saved Successfully");
    }

    public ResponseEntity<String> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        Profile profile=authService.getActiveProfile();
        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(),profile.getPassword()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Incorrect old Password");

        if(!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Passwords dont match");

        profile.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        profileRepository.save(profile);

        return ResponseEntity.ok("Updated Successfully");
    }

    public ResponseEntity<String> emailForgotPassword(ForgotPasswordEMailRequest forgotPasswordEMailRequest) {
        Optional<Profile> optionalProfile=profileRepository.findByUsernameIgnoreCase(forgotPasswordEMailRequest.getUsername().trim());
        if(optionalProfile.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profilename not found");

        Profile profile=optionalProfile.get();
        if(!profile.getEmail().trim().equalsIgnoreCase(forgotPasswordEMailRequest.getEmail().trim()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("email does not match with profilename");

        String resetCode= MyUtils.generateVerificationCode(6);

        PasswordResetCode passwordResetCode=PasswordResetCode.builder()
                .userId(profile.getUid())
                .created(new Date().getTime())
                .passwordResetCode(resetCode)
                .expiryTime(TimeUnit.MINUTES.toMillis(10))
                .build();

        passwordResetCodeRepository.save(passwordResetCode);

        String message="Your Password Reset Code is "+resetCode+" valid for "+10+" Minutes. Do not share it with anyone.";
        String subject="Exam System Password reset Code";
        EmailUtils.sendEmail(subject,message,profile.getEmail().trim());

        return ResponseEntity.ok("Password ResetCode sent to email "+profile.getEmail());
    }


    public ResponseEntity<String> passwordReset(ResetPasswordRequest resetPasswordRequest) {

        Optional<Profile> usr=profileRepository.findByUsernameIgnoreCase(resetPasswordRequest.getUsername().trim());
        if(usr.isEmpty())
            throw new UsernameNotFoundException(resetPasswordRequest.getUsername()+" does not exist");

        Profile profile=usr.get();

        Optional<PasswordResetCode> optionalPasswordResetCode=passwordResetCodeRepository.findResetCode(profile.getUid(),resetPasswordRequest.getResetCode());
        if(optionalPasswordResetCode.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password Reset code");

        PasswordResetCode passwordResetCode=optionalPasswordResetCode.get();
        if(!passwordResetCode.isUsable())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Expired Password Reset code");

        if(!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Passwords dont match");

        profile.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        profileRepository.save(profile);

        Optional<TwoFactor> optionalTwoFactor=twoFactorRepository.findById(profile.getUid());
        if(optionalTwoFactor.isPresent())
            twoFactorRepository.delete(optionalTwoFactor.get());

        return ResponseEntity.ok("Password Reset Successfully");
    }



    public ResponseEntity<String> updateProfile(UpdateRequest updateRequest, UUID uid) {
        Optional<Profile> usr=profileRepository.findById(uid);
        if(usr.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        Profile profile=usr.get();

        profile.setEmail(updateRequest.getEmail());

        profile.setPhone(updateRequest.getPhone());
        profile.setProfileName(updateRequest.getProfileName());

        profile.setUsername(updateRequest.getUsername().trim());
        profileRepository.save(profile);

        return ResponseEntity.ok("Saved Successfully");
    }

    public ResponseEntity<String> updatePassword(UpdatePasswordRequest updatePasswordRequest, UUID uid) {
        Optional<Profile> usr=profileRepository.findById(uid);
        if(usr.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        Profile profile=usr.get();

        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(),profile.getPassword()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Incorrect old Password");

        if(!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Passwords dont match");

        profile.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        profileRepository.save(profile);

        return ResponseEntity.ok("Updated Successfully");

    }

    public ResponseEntity<LoginResponseModel> loginProfile(LoginRequest loginRequest) {
        Optional<Profile> optionalProfile = profileRepository.findByUsernameIgnoreCase(loginRequest.getUsername().trim());
        if (optionalProfile.isEmpty())
            throw new UsernameNotFoundException("Invalid Profilename");

        Profile profile = optionalProfile.get();

        String authorization = authService.loginProfile(profile.getUid(), loginRequest.getPassword(),authenticationManager);

        LoginResponseModel loginResponseModel = LoginResponseModel.builder()
                .twoFactorEnabled(authService.get2FAEnabled(profile.getUid()))
                .message("Login Successful.")
                .Authorization(authorization)
                .build();

        return ResponseEntity.ok(loginResponseModel);
    }


    public Profile currentProfile() {
        return authService.getActiveProfile();
    }

    public ResponseEntity<String> logOut(HttpServletRequest request) {
        return ResponseEntity.ok(authService.logOut(request));
    }

    public ResponseEntity<String> logOutAllDevices() {
        return ResponseEntity.ok(authService.logOutAllDevices());
    }

    public ResponseEntity<Profile> userInfo(UUID userId) {
        Optional<Profile> optionalProfile=profileRepository.findById(userId);
        if(optionalProfile.isPresent())
            return ResponseEntity.ok(optionalProfile.get());
        else
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body(null);
    }

    public ResponseEntity<Profile> searchUser(String username) {
        Optional<Profile> optionalProfile=profileRepository.findByUsernameIgnoreCase(username);
        if(optionalProfile.isPresent())
            return ResponseEntity.ok(optionalProfile.get());
        else
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body(null);
    }

    public ResponseEntity<List<Profile>> listUsers() {
        return ResponseEntity.ok(profileRepository.findAll());
    }
}
