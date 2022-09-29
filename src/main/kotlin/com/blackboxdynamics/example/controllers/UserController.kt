package com.blackboxdynamics.example.controllers

import com.blackboxdynamics.example.entities.AppUser
import com.blackboxdynamics.example.models.UserCreationRequest
import com.blackboxdynamics.example.models.UserCreationResponse
import com.blackboxdynamics.example.services.ServiceException
import com.blackboxdynamics.example.servicesImp.CustomUserDetailsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val service:CustomUserDetailsService) {

    @PreAuthorize("permitAll()")
    @PostMapping
    fun post(@RequestBody model: UserCreationRequest): ResponseEntity<UserCreationResponse> {
        return try {
            var entity = AppUser(model.firstName, model.lastName, model.email, model.username, model.password)
            entity = service.create(entity)
            val modelOut = Mapper.map(entity, UserCreationResponse::class.java)
            modelOut.grantedAuthorities = entity.getAuthorities()
            ResponseEntity(modelOut, HttpStatus.CREATED)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.CONFLICT)
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable(name = "id") id: Long): ResponseEntity<*> {
        service.delete(id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }
}