package com.blackboxdynamics.example.servicesImp

import com.blackboxdynamics.example.entities.IEntity
import com.blackboxdynamics.example.repositories.GenericRepository
import com.blackboxdynamics.example.services.GenericService
import com.blackboxdynamics.example.services.ServiceException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class GenericServiceImp<I, T : IEntity<I>>(
    @Autowired protected val repository: GenericRepository<I, T>)
    : GenericService<I, T> {
    
    override fun getById(id: I): T {
        return repository.findById(id).orElseThrow()
    }

    override fun getAll(pageNum: Int, pageSize: Int, sortAndOrders: Sort): Page<T> {
        val pageable: Pageable = PageRequest.of(pageNum, pageSize, sortAndOrders)
        return repository.findAll(pageable)
    }

    override fun create(entity: T): T {
        val existent: Optional<T> = repository.findById(entity.id)
        if (existent.isPresent) {
            throw ServiceException("Entity already exists", null)
        }
        return repository.save(entity)
    }

    override fun update(id: I, entity: T): T {
        getById(id)
        entity.id = id
        return repository.save(entity)
    }

    override fun delete(id: I) {
        val entity: Optional<T> = repository.findById(id)
        if (entity.isPresent) {
            repository.delete(entity.get())
        }
    }
}