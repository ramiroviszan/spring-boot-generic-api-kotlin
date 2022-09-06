package com.blackboxdynamics.example.config

import com.blackboxdynamics.example.filters.JwtTokenVerifierFilter
import com.blackboxdynamics.example.filters.JwtUsernamePasswordAuthFilter
import com.blackboxdynamics.example.servicesImp.CustomUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {


    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    fun authManager(
        http: HttpSecurity,
        @Autowired customUserDetailsService: CustomUserDetailsService,
        @Autowired passwordEncoder:PasswordEncoder
    ): AuthenticationManager {
        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService<UserDetailsService>(customUserDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build()
    }

    @Bean
    fun filterChain(http: HttpSecurity,
                    @Autowired authManager: AuthenticationManager,
                    @Autowired jwtConfig: JwtConfig): SecurityFilterChain {
        http.csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(JwtUsernamePasswordAuthFilter(authManager, jwtConfig))
            .addFilterAfter(JwtTokenVerifierFilter(jwtConfig), JwtUsernamePasswordAuthFilter::class.java)
            .authorizeRequests()
            .antMatchers("/login")
            .permitAll()
            .anyRequest()
            .authenticated()
        return http.build()
    }
}