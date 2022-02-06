package com.mvpmatch.vendingmachine.model

import com.mvpmatch.vendingmachine.exception.ALLOWED_DEPOSIT_VALUES
import com.mvpmatch.vendingmachine.exception.InvalidDepositException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

class UserTest() {

    @Test
    fun that10CentsDeposited() {
        //given
        val user = createTestUser()
        //when
        user.deposit(10)
        //then
        Assertions.assertThat(user.deposit).isEqualTo(10)
    }

    @Test
    fun thatOnlyAllowedValuesDeposited() {
        //given
        val user = createTestUser()

        //when
        val allowedValues = listOf(5, 10, 20, 50, 100)
        Assertions.assertThat(allowedValues).isEqualTo(ALLOWED_DEPOSIT_VALUES)
        allowedValues.forEach{ user.deposit(it) }
    }

    @Test
    fun thatInvalidDepositAmountThrows() {
        //given
        val user = createTestUser()
        //when //then
        Assertions.assertThatThrownBy {
            user.deposit(9)
        }.isInstanceOf(InvalidDepositException::class.java)
            .hasMessage("Only amount of $ALLOWED_DEPOSIT_VALUES allowed to deposit")
    }

    private fun createTestUser(): User {
        return User(
            UUID.randomUUID(),
            "someName",
            "secret",
            0,
            Role.BUYER
        )
    }
}