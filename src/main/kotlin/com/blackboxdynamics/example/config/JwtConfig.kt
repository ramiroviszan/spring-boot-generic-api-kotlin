package com.blackboxdynamics.example.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "application.jwt")
@ConfigurationPropertiesScan
data class JwtConfig(
    var secret: String = "",
    var tokenPrefix: String = "",
    var issuer: String = "",
    var expirationDays: Int = 0
)