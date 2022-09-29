package com.blackboxdynamics.example.services

import com.blackboxdynamics.example.entities.AppUser

interface UserService {

    fun create(user:AppUser):AppUser

    fun delete(userId:Long)
}