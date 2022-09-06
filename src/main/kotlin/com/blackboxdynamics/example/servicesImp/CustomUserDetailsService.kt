package com.blackboxdynamics.example.servicesImp

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return User("username",
            "password",
            object : HashSet<GrantedAuthority>() {
                init {
                    add(SimpleGrantedAuthority("read"))
                    add(SimpleGrantedAuthority("write"))
                }
            }
        )
    }
}