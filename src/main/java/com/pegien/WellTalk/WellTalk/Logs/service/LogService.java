package com.pegien.WellTalk.WellTalk.Logs.service;


import com.pegien.WellTalk.WellTalk.Auth.AuthService;
import com.pegien.WellTalk.WellTalk.Logs.Log;
import com.pegien.WellTalk.WellTalk.Logs.LogRepository;
import com.pegien.WellTalk.WellTalk.Profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private ProfileRepository profileRepository;


    @Autowired
    private AuthService authService;

    public void recordLog(String log)
    {
        Log myLog=Log.builder()
                .date(new Date().getTime())
                .profile(authService.getActiveProfile())
                .log(log)
                .build();

        logRepository.save(myLog);
    }

    public ResponseEntity<List<Log>> listLogs() {

        return ResponseEntity.ok(logRepository.findLastN(200));

    }


//    private Profile currentProfile()
//    {
//        Long profileId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
//        Optional<Profile> optionalProfile=ProfileRepository.findById(profileId);
//        if(optionalProfile.isPresent()) {
//            Profile currProfile = optionalProfile.get();
//            return currProfile;
//        }
//        else{
//            return null;
//        }
//    }

//    public Profile currentProfile()
//    {
//        Long profileId = ConvertionUtils.getLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
//        Optional<Profile> optionalProfile=ProfileRepository.findById(profileId);
//        if(optionalProfile.isPresent()) {
//            Profile currProfile = optionalProfile.get();
//            return currProfile;
//        }
//        else{
//            return null;
//        }
//    }

}
