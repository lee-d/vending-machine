package com.mvpmatch.vendingmachine.config

import com.mvpmatch.vendingmachine.exception.NoModelFoundException
import com.mvpmatch.vendingmachine.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserDetailService(
    val userRepository: UserRepository,
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated")

        return org.springframework.security.core.userdetails.User(user.username, user.password,
            listOf(SimpleGrantedAuthority("${user.role}")))
    }

}