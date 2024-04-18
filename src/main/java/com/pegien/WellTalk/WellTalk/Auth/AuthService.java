package com.pegien.WellTalk.WellTalk.Auth;


import com.pegien.WellTalk.WellTalk.Auth.AuthToken.Token;
import com.pegien.WellTalk.WellTalk.Auth.AuthToken.TokenRepository;
import com.pegien.WellTalk.WellTalk.Auth.UserDetails.ProfileDetails;
import com.pegien.WellTalk.WellTalk.Auth.UserDetails.ProfileDetailsService;
import com.pegien.WellTalk.WellTalk.Configurations.SecurityConfig;
import com.pegien.WellTalk.WellTalk.Profile.entity.Profile;
import com.pegien.WellTalk.WellTalk.Profile.repository.ProfileRepository;
import com.pegien.WellTalk.WellTalk.TWOFactor.TwoFactor;
import com.pegien.WellTalk.WellTalk.TWOFactor.TwoFactorRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Autowired
    private TokenRepository tokenRepository;


    @Autowired
    private ProfileDetailsService profileDetailsService;

    @Autowired
    private ProfileRepository profileRepository;

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Autowired
    private TwoFactorRepository twoFactorRepository;



    public void authorizeRequest(String tokenString, HttpServletRequest request, String username)
    {
        Optional<Token> tmpToken=tokenRepository.findByValue(tokenString);
        if(tmpToken.isPresent()) {
            Token token=tmpToken.get();
            if (token.isUsable()) {

                //check for 2FA
                if(check2FA(token)) {
                    if (!request.getRequestURL().toString().contains("/api/v1/profile/checkProfile")) {
                        token.setLastAccess(new Date().getTime());
                        tokenRepository.save(token);
                    }
                    ProfileDetails profileDetails = profileDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken profilenamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(profileDetails.getUsername(), profileDetails.getPassword(), profileDetails.getAuthorities());
                    profilenamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(profilenamePasswordAuthenticationToken);
                }
            }
        }
    }

    private boolean check2FA(Token token) {
        if(get2FAEnabled(token.getProfileId()))
        {
            return (token.getAuthenticated()!=null&&token.getAuthenticated());

        }else
            return true;
    }

    public Boolean get2FAEnabled(UUID profileId)
    {
        Optional<TwoFactor> optionalTwoFactor=twoFactorRepository.findById(profileId);
        return optionalTwoFactor.isPresent()&&optionalTwoFactor.get().getIsVerified();
    }

    public Profile getActiveProfile()
    {
        String profileUid =SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<Profile> optionalProfile=profileRepository.findById(UUID.fromString(profileUid));
        if(optionalProfile.isPresent()) {
            Profile currProfile = optionalProfile.get();
            return currProfile;
        }
        else{
            return null;
        }
    }


    public String loginProfile(UUID uid, String password,AuthenticationManager authenticationManager) {
//        System.out.println("Loggin in "+num+" - "+password);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(uid,password)
        );

//        System.out.println("success Loggin in "+num+" - "+password);
        String token = Jwts.builder().setClaims(new HashMap<>()).setSubject(uid.toString()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .signWith(SignatureAlgorithm.HS256, SecurityConfig.jwtSecretKey).compact();


        Token authorizationTokens=Token.builder()
                .lastAccess(new Date().getTime())
                .loggedOut(false)
                .inActiveTime(TimeUnit.MINUTES.toMillis(10))
                .value(token)
                .profileId(uid)
                .build();

        tokenRepository.saveAndFlush(authorizationTokens);

        String authorization="Bearer "+token;

        return authorization;
    }


    public String logOut(HttpServletRequest request)
    {
        String authorization=request.getHeader("Authorization");
        String init="Bearer ";
        if(authorization!=null&&authorization.contains(init)) {
            String tokenString = authorization.replaceAll(init, "");
            Optional<Token> optionalAuthorizationTokens = tokenRepository.findByValue(tokenString);
            if (optionalAuthorizationTokens.isEmpty())
                throw new UnsupportedOperationException("no such authorization");

            Token authorizationTokens = optionalAuthorizationTokens.get();

            //perform the logout action
            authorizationTokens.setLoggedOut(true);
            tokenRepository.save(authorizationTokens);

            return "Success";
        }
        else
            throw new UnsupportedOperationException("no such authorization");
    }

    public String logOutAllDevices()
    {
            List<Token> allActiveProfileTokens = tokenRepository.findByProfileId(getActiveProfile().getUid());
            for(Token authorizationTokens:allActiveProfileTokens) {
                //perform the logout action
                authorizationTokens.setLoggedOut(true);
                tokenRepository.save(authorizationTokens);
            }
            return "Success";
    }


}
