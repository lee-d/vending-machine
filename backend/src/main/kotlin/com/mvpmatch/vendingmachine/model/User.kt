package com.mvpmatch.vendingmachine.model

import com.mvpmatch.vendingmachine.exception.ALLOWED_DEPOSIT_VALUES
import com.mvpmatch.vendingmachine.exception.InsufficientDepositException
import com.mvpmatch.vendingmachine.exception.InvalidDepositException
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.math.MathContext
import java.util.*

@Document(collection = "user")
data class User(
    val id: UUID,
    val username: String,
    val password: String,
    var deposit: BigDecimal,
    val role: Role,
    ) {

    fun deposit(amount: Int) {
        if (!ALLOWED_DEPOSIT_VALUES.contains(amount)) {
            throw InvalidDepositException("Only amount of $ALLOWED_DEPOSIT_VALUES allowed to deposit")
        }
       this.deposit = this.deposit.add(amount.toBigDecimal().divide(BigDecimal(100)))
    }

    fun payWithDeposit(price: BigDecimal): BigDecimal {
        if (hasInsufficientDeposit(price)) {
            throw InsufficientDepositException("Oh oh, insufficient deposit. Please deposit more money")
        }
        return deposit.minus(price).apply { deposit = this }
    }

    private fun hasInsufficientDeposit(price: BigDecimal): Boolean {
        return deposit.minus(price) < BigDecimal.ZERO
    }
}

enum class Role {

    SELLER,
    BUYER,

}

data class UserCreationDto(
    var username: String,
    var password: String,
    var role: Role,
)

data class UserDto(
    var id: UUID,
    var username: String,
    var deposit: BigDecimal,
) {
    companion object {
        fun fromUser(user: User): UserDto {
            return UserDto(user.id, user.username, user.deposit);
        }
    }
}
