package com.mvpmatch.vendingmachine.controller

import com.mvpmatch.vendingmachine.exception.NoModelFoundException
import com.mvpmatch.vendingmachine.model.User
import com.mvpmatch.vendingmachine.model.UserCreationDto
import com.mvpmatch.vendingmachine.model.UserDto
import com.mvpmatch.vendingmachine.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*

@RestController
@RequestMapping(
    value = ["/api/v1/users"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class UserController(
    private val userRepository: UserRepository,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody dto: UserCreationDto): UUID {
        val createdUser = userRepository.save(
            User(
                UUID.randomUUID(),
                dto.username,
                dto.password,
                BigDecimal.ZERO,
                dto.role,
            )
        )
        return createdUser.id
    }

    @GetMapping("/{id}")
    fun findUserById(@PathVariable id: UUID): UserDto {
        val user = userRepository.findByIdOrNull(id)
        return user?.let { UserDto.fromUser(it) } ?: throw NoModelFoundException("No user found with id $id")
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(@PathVariable id: UUID) {
        userRepository.deleteById(id)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: UUID, @RequestBody username: String): UserDto {
        val user = userRepository.findByIdOrNull(id)
        val updatedUser =
            user?.let { it.copy(username = username) } ?: throw NoModelFoundException("No user found with id $id")
        return updatedUser
            .apply {userRepository.save(this)}
            .run { UserDto.fromUser(this) }
    }

}