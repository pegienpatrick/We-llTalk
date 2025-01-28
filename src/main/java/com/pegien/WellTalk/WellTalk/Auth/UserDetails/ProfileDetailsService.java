package com.pegien.WellTalk.WellTalk.Auth.UserDetails;


import com.pegien.WellTalk.WellTalk.Profile.entity.Profile;
import com.pegien.WellTalk.WellTalk.Profile.repository.ProfileRepository;
import com.pegien.WellTalk.WellTalk.Utils.ConvertionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileDetailsService implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;


    @Override
    public ProfileDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Profile> usr=profileRepository.findById(UUID.fromString(username));
        if(usr.isEmpty())
            throw new UsernameNotFoundException("Profile Not Found");

        Profile profile=usr.get();

//        HashSet<GrantedAuthority> authorities=new HashSet<>();
//        if(profile.getRoles()!=null) {
//            for (String i : profile.getRoles())
//                authorities.add(new SimpleGrantedAuthority(i));
//        }
      ProfileDetails myProfileDetails= ProfileDetails.builder()
                .username(username)
                .password(profile.getPassword())
                .authorities(new HashSet<>())
                .build();
        return myProfileDetails;
    }
}
