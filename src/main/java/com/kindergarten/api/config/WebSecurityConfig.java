package com.kindergarten.api.config;

import com.kindergarten.api.security.CAccessDeniedHandler;
import com.kindergarten.api.security.CAuthenticationEntryPoint;
import com.kindergarten.api.security.JwtRequestFilter;
<<<<<<< HEAD:src/main/java/com/kindergarten/api/config/WebSecurityConfig.java
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> develop:src/main/java/com/kindergarten/api/security/config/WebSecurityConfig.java
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
<<<<<<< HEAD:src/main/java/com/kindergarten/api/config/WebSecurityConfig.java
    private final JwtRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }
=======
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
>>>>>>> develop:src/main/java/com/kindergarten/api/security/config/WebSecurityConfig.java


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
<<<<<<< HEAD:src/main/java/com/kindergarten/api/config/WebSecurityConfig.java
                .httpBasic()
                .authenticationEntryPoint(new CAuthenticationEntryPoint())
                .and()
                .exceptionHandling().accessDeniedHandler(new CAccessDeniedHandler())

=======
                .authorizeRequests()
                .antMatchers("/api/users/**").permitAll()
                .antMatchers(HttpMethod.GET, "/exception/**", "/actuator/health", "/favicon.ico").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new CAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CAuthenticationEntryPoint())
>>>>>>> develop:src/main/java/com/kindergarten/api/security/config/WebSecurityConfig.java
                .and()


<<<<<<< HEAD:src/main/java/com/kindergarten/api/config/WebSecurityConfig.java
=======
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }
>>>>>>> develop:src/main/java/com/kindergarten/api/security/config/WebSecurityConfig.java


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**", "/h2/**");
    }

}
