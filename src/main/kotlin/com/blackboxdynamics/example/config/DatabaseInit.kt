package com.blackboxdynamics.example.config

import com.blackboxdynamics.example.entities.Authority
import com.blackboxdynamics.example.entities.AuthorityName
import com.blackboxdynamics.example.repositories.AuthorityRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DatabaseInit(val authorityRepository: AuthorityRepository): CommandLineRunner {


    override fun run(vararg args: String?) {
        authorityRepository.save(Authority(AuthorityName.ROLE_USER))
        authorityRepository.save(Authority(AuthorityName.ROLE_ADMIN))
    }

}