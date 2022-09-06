package com.blackboxdynamics.example.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.blackboxdynamics.example.config.JwtConfig
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenVerifierFilter(private val config: JwtConfig) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith(config.tokenPrefix)) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val token = authHeader.replace(config.tokenPrefix, "")
            val algorithm = Algorithm.HMAC256(config.secret) //use more secure key
            val verifier = JWT.require(algorithm)
                .withIssuer(config.issuer)
                .build()
            val jwt = verifier.verify(token)
            val username = jwt.subject
            val authorities = jwt.getClaim("authorities").asList(
                String::class.java
            )
            val grantedAuthoritySet = authorities.stream()
                .map { m: String? ->
                    SimpleGrantedAuthority(
                        m
                    )
                }
                .collect(Collectors.toSet())
            val auth: Authentication = UsernamePasswordAuthenticationToken(
                username, null, grantedAuthoritySet
            )
            SecurityContextHolder.getContext().authentication = auth
        } catch (e: JWTVerificationException) {
            throw IllegalStateException("Illegal Auth Token")
        }
        filterChain.doFilter(request, response)
    }
}