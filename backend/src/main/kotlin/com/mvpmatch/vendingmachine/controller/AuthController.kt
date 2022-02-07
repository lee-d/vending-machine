package com.mvpmatch.vendingmachine.controller

import com.mvpmatch.vendingmachine.config.JwtService
import com.mvpmatch.vendingmachine.config.UserDetailService
import com.mvpmatch.vendingmachine.dto.AuthenticatedUserDto
import com.mvpmatch.vendingmachine.repository.UserRepository
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
    val userRepository: UserRepository,
    val bCryptPasswordEncoder: BCryptPasswordEncoder,
    val jwtService: JwtService,
) {

    @PostMapping(path = ["login"])
    fun login(
        @RequestParam username: String,
        @RequestParam password: String
    ): AuthenticatedUserDto {
        val userDetails = userDetailsService.loadUserByUsername(username)
        val userId = userRepository.findByUsername(username)!!.id
        if (bCryptPasswordEncoder.matches(password, userDetails.password)) {
            val roles = userDetails.authorities.joinToString(separator = ",")
            val jwt: String = jwtService.createJwtForClaims(
                username,
                mapOf("roles" to roles)
            )
            return AuthenticatedUserDto(userId, username, jwt, roles)
        }
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated")
    }
}