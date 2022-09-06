package com.blackboxdynamics.example.services

import com.blackboxdynamics.example.entities.IEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort

interface GenericService<I, T : IEntity<I>> {
    fun getById(id: I): T
    fun getAll(pageNum: Int, pageSize: Int, sortAndOrders: Sort): Page<T>
    fun create(entity: T): T
    fun update(id: I, entity: T): T
    fun delete(id: I)
}