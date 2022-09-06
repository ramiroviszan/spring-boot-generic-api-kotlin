package com.blackboxdynamics.example.entities

import java.util.*
import javax.persistence.*

@Entity
data class Log (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long,

    @Column(nullable = false)
    val message: String,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    val createdAt: Date
) : IEntity<Long>