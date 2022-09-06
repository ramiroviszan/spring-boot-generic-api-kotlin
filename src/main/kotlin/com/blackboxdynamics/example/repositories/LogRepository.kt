package com.blackboxdynamics.example.repositories

import com.blackboxdynamics.example.entities.Log
import org.springframework.stereotype.Repository

@Repository
interface LogRepository : GenericRepository<Long, Log>