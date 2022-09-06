package com.blackboxdynamics.example.controllers

import com.blackboxdynamics.example.entities.IEntity
import com.blackboxdynamics.example.models.PageResponse
import com.blackboxdynamics.example.services.GenericService
import com.blackboxdynamics.example.services.ServiceException
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

abstract class GenericController<I, M, E : IEntity<I>>(private val classM: Class<M>, private val classE: Class<E>) {
    protected abstract val service: GenericService<I, E>

    @GetMapping("/{id}")
     fun get(@PathVariable(name = "id") id: I): ResponseEntity<M> {
        return try {
            val entity = service.getById(id)
            ResponseEntity(Mapper.map(entity, classM), HttpStatus.OK)
        } catch (e: NoSuchElementException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping
    fun getAll(
        @RequestParam(value = "pageNum", defaultValue = "0", required = false) pageNumber: Int,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) pageSize: Int,
        @RequestParam(value = "sort", defaultValue = "id,desc", required = false) sort: Array<String>
    ): ResponseEntity<PageResponse<M>> {
        return try {
            val sortAndOrders = getSortAndOrder(sort)
            val result = service.getAll(pageNumber, pageSize, Sort.by(sortAndOrders))
            val response = getPageResponse(result)
            ResponseEntity(response, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    private fun getSortAndOrder(sort: Array<String>): List<Sort.Order> {
        if (sort[0].contains(",")) {
            return sortMultipleColumns(sort)
        } else {
            return listOf(Sort.Order(getSortDirection(sort[1]), sort[0]))
        }
    }

    private fun sortMultipleColumns(sort: Array<String>):List<Sort.Order> {
        return sort.map {
                s:String -> Sort.Order(getSortDirection(s.split(",")[1]), s.split(",")[0])
        }.toList()
    }

    private fun getSortDirection(direction: String): Sort.Direction {
        if (direction == "asc") {
            return Sort.Direction.ASC
        } else if (direction == "desc") {
            return Sort.Direction.DESC
        }
        return Sort.Direction.ASC
    }

    private fun getPageResponse(page: Page<E>): PageResponse<M> {
        return PageResponse(
                page.content.map {
                        e: E -> Mapper.map(e, classM)
                }.toList(),
                page.number,
                page.size,
                page.totalElements,
                page.totalPages,
                page.isLast
        )
    }

    @PostMapping
    fun post(@RequestBody model: M): ResponseEntity<M> {
        return try {
            val entity = service.create(Mapper.map(model, classE))
            ResponseEntity(Mapper.map(entity, classM), HttpStatus.CREATED)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.CONFLICT)
        }
    }

    @PutMapping("/{id}")
    fun put(@PathVariable(name = "id") id: I, @RequestBody model: M): ResponseEntity<M> {
        return try {
            val entity = service.update(id, Mapper.map(model, classE))
            ResponseEntity(Mapper.map(entity, classM), HttpStatus.OK)
        } catch (e: NoSuchElementException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(name = "id") id: I): ResponseEntity<*> {
        service.delete(id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

}