package com.hyeonsik.boot.configuration;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hyeonsik.boot.service.CustomOAuth2UserService;
import com.hyeonsik.boot.service.UserService;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	public final UserService userService;
	AuthenticationManager authenticationManager;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthFailureHandler customFailureHandler;

    
	
    @Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOriginPatterns(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
    
    
    @Bean
    public WebSecurityCustomizer assetCustomizer() {
        return (web -> web.ignoring().antMatchers("/css/**", "/script/**", "image/**", "/fonts/**", "lib/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    	
    	http
        .csrf().disable()
        .authorizeRequests()
            .antMatchers("/login", "/store", "/logout", "/register", "/user_access", "/access_denied", "/resources/**").permitAll()
            .antMatchers("/Owner_Main").hasRole("ADMIN")
        .and()
        .formLogin()
            .loginPage("/mainpage")
            .loginProcessingUrl("/login_proc")
            .failureHandler(customFailureHandler) // 로그인 실패 핸들러
        .and()
        .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/mainpage")
            .deleteCookies("JSESSIONID", "remember-me")
            .invalidateHttpSession(true)
        .and()
        .oauth2Login()
            .loginPage("/login")
            .defaultSuccessUrl("/mainpage")
            .userInfoEndpoint()
            .userService(customOAuth2UserService);
    	
    	
    	
    	
                     //로그인 창
        return http.build();
    }
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl antMatchers(String string) {
		// TODO Auto-generated method stub
		return null;
	}


	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	
           	
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
    
    
    

   
    
    
    
    }