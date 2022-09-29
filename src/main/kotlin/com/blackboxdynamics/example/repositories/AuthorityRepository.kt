package com.blackboxdynamics.example.repositories

import com.blackboxdynamics.example.entities.AppUser
import com.blackboxdynamics.example.entities.Authority
import com.blackboxdynamics.example.entities.AuthorityName
import com.blackboxdynamics.example.entities.Log
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorityRepository :JpaRepository<Authority, Long> {

    fun findByName(name:AuthorityName):Authority
}