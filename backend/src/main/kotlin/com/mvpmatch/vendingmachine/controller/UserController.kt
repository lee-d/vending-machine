package com.mvpmatch.vendingmachine.controller

import com.mvpmatch.vendingmachine.exception.NoModelFoundException
import com.mvpmatch.vendingmachine.model.User
import com.mvpmatch.vendingmachine.model.UserCreationDto
import com.mvpmatch.vendingmachine.model.UserCreationResponse
import com.mvpmatch.vendingmachine.model.UserDto
import com.mvpmatch.vendingmachine.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*


@RestController
@RequestMapping(
    value = ["/api/v1/users"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class UserController(
    val userRepository: UserRepository,
    val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody dto: UserCreationDto): UserCreationResponse {
        val createdUser = userRepository.save(
            User(
                UUID.randomUUID(),
                dto.username,
                bCryptPasswordEncoder.encode(dto.password),
                0,
                dto.role,
            )
        )
        return UserCreationResponse(createdUser.id)
    }

    @GetMapping("/{id}")
    fun findUserById(@PathVariable id: UUID): UserDto {
        val user = userRepository.findByIdOrNull(id)
        return user?.let { UserDto.fromUser(it) } ?: throw NoModelFoundException("No user found with id $id")
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @GetMapping()
    fun findAll(): List<UserDto> {
        return userRepository.findAll().map { UserDto.fromUser(it) }
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(@PathVariable id: UUID) {
        userRepository.deleteById(id)
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @PutMapping()
    fun updateUser(@RequestBody username: String, principal: Principal): UserDto {
        val user = userRepository.findByUsername(principal.name)
        return user?.let {
                val updateUser = it.copy(username = username)
                UserDto.fromUser(userRepository.save(updateUser))
            } ?: throw NoModelFoundException("No user found with id ${principal.name}")
    }

    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    @PutMapping("/deposit")
    fun deposit(@RequestParam amount: Int, principal: Principal): UserDto {
        val user = userRepository.findByUsername(principal.name)
        return user?.let {
                it.deposit(amount)
                UserDto.fromUser(userRepository.save(it))
            } ?: throw NoModelFoundException("No user found with id ${principal.name}")
    }

    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    @PutMapping("/resetdeposit")
    @ResponseStatus(HttpStatus.OK)
    fun resetDeposit(principal: Principal) {
        userRepository.findByUsername(principal.name)?.let {
            it.resetDeposit()
        }.also { userRepository.save(it as User) } ?: throw NoModelFoundException("No user found with id ${principal.name}")
    }
}
