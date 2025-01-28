package com.pegien.WellTalk.WellTalk.Configurations;

import com.pegien.WellTalk.WellTalk.Auth.JWTTOkenFilter;
import com.pegien.WellTalk.WellTalk.Auth.UserDetails.ProfileDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
//@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    public static String jwtSecretKey="PatrickGeePatrickGeePatrickGeePatrickGeePatrickGeePatrickGeeChatApp";

    @Autowired
    private JWTTOkenFilter jwtTokenFilter;

    @Autowired
    private ProfileDetailsService myProfileDetailsService;

   /* @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);

        http
                .cors().
                and()

                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**",
                        "/v3/api-docs/**"
                        ,"/api/v1/profile/registerProfile"
                        ,"/api/v1/profile/allRoles"
                        ,"/api/v1/profile/passwordReset"
                        ,"/api/v1/profile/smsForgotPassword"
                        ,"/api/v1/profile/emailForgotPassword"
                        ,"/api/v1/profile/login"
                        ,"/api/v1/profile/possibleGenders"
                        ,"/api/v1/profile/verify2FA"


                )
                .permitAll()
                .antMatchers("/api/v1/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


                http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }
*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http.csrf(AbstractHttpConfigurer::disable)
               .cors(httpSecurityCorsConfigurer -> {
                   CorsConfiguration configuration = new CorsConfiguration();
                   configuration.applyPermitDefaultValues();
                   configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
                   UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                   source.registerCorsConfiguration("/**",configuration);
                   httpSecurityCorsConfigurer.configurationSource(source);
               }).
               authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                   authorizationManagerRequestMatcherRegistry.requestMatchers("/swagger-ui/**",
                           "/v3/api-docs/**"
                           ,"/api/v1/profile/registerUser"
                           ,"/api/v1/profile/allRoles"
                           ,"/api/v1/profile/passwordReset"
                           ,"/api/v1/profile/smsForgotPassword"
                           ,"/api/v1/profile/emailForgotPassword"
                           ,"/api/v1/profile/login"
                           ,"/api/v1/profile/possibleGenders"
                           ,"/api/v1/profile/verify2FA"
                           ,"/api/v1/profile/ping"
                           ).permitAll()

                           .requestMatchers("/api/v1/**").authenticated()
                           .anyRequest().permitAll();
               }).sessionManagement(httpSecuritySessionManagementConfigurer -> {
                   httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
               });

       http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);


       return http.build();
    }




    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }



   @Bean
   public AuthenticationProvider authenticationProvider(){
       DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
       authenticationProvider.setUserDetailsService(myProfileDetailsService);
       authenticationProvider.setPasswordEncoder(passwordEncoder());
       return authenticationProvider;
   }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



}
