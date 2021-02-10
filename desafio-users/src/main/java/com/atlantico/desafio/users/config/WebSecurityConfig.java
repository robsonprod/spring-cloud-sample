package com.atlantico.desafio.users.config;

import com.atlantico.desafio.persistence.service.CustomUserDetailsService;
import com.atlantico.desafio.users.service.AuthServiceRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthServiceRedis authService;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                    .disable()
                .csrf()
                    .disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                .and()
                .authorizeRequests()
                    .antMatchers("/authenticate", "/users/signIn")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                .httpBasic()
                    .disable()
                .formLogin()
                    .disable()
                .logout()
                    .disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAt(this::authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private void authenticationFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Optional<Authentication> authentication = this.authService.authenticate((HttpServletRequest) request);
        authentication.ifPresent(SecurityContextHolder.getContext()::setAuthentication);
        chain.doFilter(request, response);
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
