package com.blackboxdynamics.example.models

import com.blackboxdynamics.example.config.NoArg
import java.util.*

@NoArg
data class LogModel(val id: Long, val message: String, val createdAt: Date)
