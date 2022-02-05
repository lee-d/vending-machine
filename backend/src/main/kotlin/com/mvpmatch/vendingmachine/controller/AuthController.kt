package com.mvpmatch.vendingmachine.controller

import com.mvpmatch.vendingmachine.config.JwtService
import com.mvpmatch.vendingmachine.config.UserDetailService
import org.springframework.http.HttpStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.stream.Collectors


@RestController
class AuthController(
    val userDetailsService: UserDetailService,
    val bCryptPasswordEncoder: BCryptPasswordEncoder,
    val jwtService: JwtService,
) {

    @PostMapping(path = ["login"])
    fun login(
        @RequestParam username: String,
        @RequestParam password: String
    ): String {
        val userDetails = userDetailsService.loadUserByUsername(username)
        if (bCryptPasswordEncoder.matches(password, userDetails.password)) {
            val jwt: String = jwtService.createJwtForClaims(
                username,
                mapOf("roles" to userDetails.authorities.joinToString(separator = ","))
            )
            return jwt
        }
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated")
    }
}