package com.blackboxdynamics.example.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.blackboxdynamics.example.config.JwtConfig
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.*
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtUsernamePasswordAuthFilter(
    private val authManager: AuthenticationManager,
    private val config: JwtConfig
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        return try {
            val mapper = jacksonObjectMapper()
            val requestMap: Map<String, String> = mapper.readValue(request.inputStream)

            val auth: Authentication = UsernamePasswordAuthenticationToken(
                requestMap["username"],
                requestMap["password"]
            )
            authManager.authenticate(auth)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        try {
            val algorithm: Algorithm = Algorithm.HMAC256(config.secret)
            val token: String = JWT.create()
                .withSubject(authResult.name)
                .withClaim("authorities",
                    authResult.authorities
                        .map { s: GrantedAuthority -> s.authority }
                        .toList())
                .withIssuedAt(Date(System.currentTimeMillis()))
                .withExpiresAt(Date(System.currentTimeMillis() + 1000 * 60 * 60 * config.expirationDays))
                .withIssuer(config.issuer)
                .sign(algorithm)
            response.addHeader("Authorization", config.tokenPrefix + token)
        } catch (e: JWTCreationException) {
            throw RuntimeException(e)
        }
    }
}