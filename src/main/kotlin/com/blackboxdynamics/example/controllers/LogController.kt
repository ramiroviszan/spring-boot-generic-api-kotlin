package com.blackboxdynamics.example.controllers

import com.blackboxdynamics.example.entities.Log
import com.blackboxdynamics.example.models.LogModel
import com.blackboxdynamics.example.services.GenericService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/logs")
class LogController(@Autowired override val service: GenericService<Long, Log>)
    : GenericController<Long, LogModel, Log>(LogModel::class.java, Log::class.java)