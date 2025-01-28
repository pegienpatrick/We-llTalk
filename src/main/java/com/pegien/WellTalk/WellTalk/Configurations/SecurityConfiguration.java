//package com.pegien.WellTalk.WellTalk.Configurations;
//
//import com.pegien.WellTalk.WellTalk.Auth.JWTTOkenFilter;
//import com.pegien.WellTalk.WellTalk.Auth.UserDetails.ProfileDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@EnableWebSecurity
//@Configuration
//public class SecurityConfiguration {
//
//    public static String jwtSecretKey="PatrickGeePatrickGeePatrickGeePatrickGeePatrickGeePatrickGeeChatApp";
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new ProfileDetailsService();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .cors().
//                and()
//
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/swagger-ui/**",
//                        "/v3/api-docs/**"
//                        ,"/api/v1/profile/registerProfile"
//                        ,"/api/v1/profile/allRoles"
//                        ,"/api/v1/profile/passwordReset"
//                        ,"/api/v1/profile/smsForgotPassword"
//                        ,"/api/v1/profile/emailForgotPassword"
//                        ,"/api/v1/profile/login"
//                        ,"/api/v1/profile/possibleGenders"
//                        ,"/api/v1/profile/verify2FA"
//
//
//                )
//                .permitAll()
//                .antMatchers("/api/v1/**").authenticated()
//                .anyRequest().permitAll()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//
//        http.addFilterBefore(new JWTTOkenFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
//    }
//
//}