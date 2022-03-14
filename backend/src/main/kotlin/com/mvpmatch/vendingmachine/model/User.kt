package com.mvpmatch.vendingmachine.model

import com.mvpmatch.vendingmachine.exception.ALLOWED_DEPOSIT_VALUES
import com.mvpmatch.vendingmachine.exception.InsufficientDepositException
import com.mvpmatch.vendingmachine.exception.InvalidDepositException
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.crypto.util.EncodingUtils.concatenate
import java.math.BigDecimal
import java.math.MathContext
import java.util.*

@Document(collection = "user")
data class User(
    val id: UUID,
    val username: String,
    val password: String,
    var deposit: Int,
    val role: Role,
    ) {

    fun resetDeposit(): User {
        this.deposit = 0
        return this
    }

    fun deposit(amount: Int) {
        if (!ALLOWED_DEPOSIT_VALUES.contains(amount)) {
            throw InvalidDepositException("Only amount of $ALLOWED_DEPOSIT_VALUES allowed to deposit")
        }
       this.deposit = this.deposit.plus(amount)
    }

    fun payWithDeposit(price: Int): List<Int> {
        if (hasInsufficientDeposit(price)) {
            throw InsufficientDepositException("Oh oh, insufficient deposit. Please deposit more money")
        }

        deposit -= price
        val change = mutableListOf<Int>()
        for (i in ALLOWED_DEPOSIT_VALUES.sortedDescending()) {
            val changeForCoin = calculateChangeForDeposit(deposit, i)
            deposit -= changeForCoin.sum()
            change.addAll(changeForCoin)
        }
        resetDeposit()
        return change.toList()
    }

    private fun calculateChangeForDeposit(deposit: Int, coinValue: Int): List<Int> {
        val possibleCoinCountForDeposit = deposit.div(coinValue)
        return if (possibleCoinCountForDeposit > 0) {
            List(possibleCoinCountForDeposit) { coinValue }
        } else
            emptyList()
    }

    private fun hasInsufficientDeposit(price: Int): Boolean {
        return deposit.minus(price) < 0
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

data class UserCreationResponse(
    val id: UUID
)

data class UserDto(
    val username: String,
    val deposit: Int,
) {
    companion object {
        fun fromUser(user: User): UserDto {
            return UserDto(user.username, user.deposit);
        }
    }
}


