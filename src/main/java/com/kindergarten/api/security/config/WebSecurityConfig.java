package com.kindergarten.api.security.config;

import com.kindergarten.api.common.exception.CAccessDeniedHandler;
import com.kindergarten.api.common.exception.CAuthenticationEntryPoint;
import com.kindergarten.api.security.JwtRequestFilter;
import com.kindergarten.api.service.Impl.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
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
    private final JwtRequestFilter jwtRequestFilter;
    private final CAuthenticationEntryPoint cAuthenticationEntryPoint;
    private final CAccessDeniedHandler cAccessDeniedHandler;
    private final UserDetailsServiceImpl userDetailsService;

    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter, CAuthenticationEntryPoint cAuthenticationEntryPoint, CAccessDeniedHandler cAccessDeniedHandler, UserDetailsServiceImpl userDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.cAuthenticationEntryPoint = cAuthenticationEntryPoint;
        this.cAccessDeniedHandler = cAccessDeniedHandler;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
                .authenticationEntryPoint(cAuthenticationEntryPoint)
                .and()
                .exceptionHandling().accessDeniedHandler(cAccessDeniedHandler)

                .and()
                .authorizeRequests()
                .antMatchers("/api/users/parent/**").permitAll()
                .antMatchers("/api/users/teacher/**").permitAll()
                .antMatchers("/api/users/director/**").permitAll()
                .antMatchers("/api/users/login").permitAll()
                .antMatchers("/api/users/list/**").hasRole("USER")
                .anyRequest().authenticated();
//                // 토큰을 활용하는 경우 모든 요청에 대해 접근이 가능하도록 함
//                .anyRequest().permitAll()
//                .and()
//                // session을 가지지 않는
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                // formlogin 비활성화
//                .formLogin()
//                .disable();
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**", "/h2/**");
    }

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
}
