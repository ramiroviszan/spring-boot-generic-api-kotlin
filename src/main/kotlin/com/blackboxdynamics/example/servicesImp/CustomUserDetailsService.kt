package com.blackboxdynamics.example.servicesImp

import com.blackboxdynamics.example.entities.AppUser
import com.blackboxdynamics.example.entities.AuthorityName
import com.blackboxdynamics.example.repositories.AppUserRepository
import com.blackboxdynamics.example.repositories.AuthorityRepository
import com.blackboxdynamics.example.services.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val repository: AppUserRepository,
    private val authRepository: AuthorityRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService, UserService {

    override fun loadUserByUsername(username: String): UserDetails {
        return repository.findByUsername(username)!!
    }

    override fun create(user: AppUser):AppUser {
        user.authorities.add(authRepository.findByName(AuthorityName.ROLE_ADMIN))

        val userExists: Boolean = repository.findByUsername(user.username) != null
        check(!userExists) {
            "username already taken"
        }

        val encodedPassword:String = passwordEncoder.encode(user.password)
        user.setPassword(encodedPassword)

        repository.save(user)
        repository.flush()
        return user
    }

    override fun delete(userId: Long) {
        repository.deleteById(userId)
    }
}