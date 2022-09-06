package com.blackboxdynamics.example.models

data class PageResponse<T>(
    val content: List<T>,
    val number:Int,
    val pageSize:Int,
    val totalElements: Long,
    val totalPages:Int,
    val isLast:Boolean
)