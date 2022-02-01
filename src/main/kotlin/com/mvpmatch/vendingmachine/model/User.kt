package com.mvpmatch.vendingmachine.model

import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

@Document(collection = "user")
data class User(
    var id: UUID,
    var username: String,
    var password: String,
    var deposit: BigDecimal,
    var role: Role,
    ) {
    companion object {
        fun fromUserDto(userDto: UserDto): User {
            return User(userDto.id, userDto.username, userDto.password, userDto.deposit, Role.SELLER);
        }
    }
}

enum class Role {

    SELLER,
    BUYER,

}

data class UserCreationDto(
    val username: String,
    val password: String,
    val role: Role,
)

data class UserDto(
    val id: UUID,
    val username: String,
    val password: String,
    val deposit: BigDecimal,
) {
    companion object {
        fun fromUser(user: User): UserDto {
            return UserDto(user.id, user.username, user.password, user.deposit);
        }
    }
}