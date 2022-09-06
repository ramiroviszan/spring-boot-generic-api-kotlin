package com.blackboxdynamics.example.repositories

import com.blackboxdynamics.example.entities.IEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface GenericRepository<I, T : IEntity<I>> : JpaRepository<T, I>