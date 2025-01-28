package com.pegien.WellTalk.WellTalk.Auth;


import com.pegien.WellTalk.WellTalk.Configurations.SecurityConfig;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Date;

@Component
public class JWTTOkenFilter extends OncePerRequestFilter {


    @Autowired
    private AuthService authService;




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization=request.getHeader("Authorization");
        String init="Bearer ";
        if(authorization!=null&&authorization.contains(init))
        {
            try {
                String tokenString = authorization.replaceAll(init, "");
                Jws<Claims> claims = Jwts.parser().setSigningKey(SecurityConfig.jwtSecretKey).build().parseClaimsJws(tokenString);
                String profilename = claims.getBody().getSubject();
                if (profilename != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Date expiry = claims.getBody().getExpiration();
                    Date current = new Date();
                    if (current.before(expiry)) {
                        authService.authorizeRequest(tokenString, request, profilename);
                    }
                }
            }catch (Exception es)
            {

            }
        }
        filterChain.doFilter(request, response);
    }

}
