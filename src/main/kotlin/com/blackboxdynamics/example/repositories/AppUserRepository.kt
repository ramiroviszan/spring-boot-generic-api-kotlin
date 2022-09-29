package com.blackboxdynamics.example.repositories

import com.blackboxdynamics.example.entities.AppUser
import com.blackboxdynamics.example.entities.Log
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository :JpaRepository<AppUser, Long> {

    fun findByUsername(username:String):AppUser?
}