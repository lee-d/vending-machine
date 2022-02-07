package com.mvpmatch.vendingmachine.service

import com.mvpmatch.vendingmachine.dto.PurchaseResponseDto
import com.mvpmatch.vendingmachine.exception.OutOfStockException
import com.mvpmatch.vendingmachine.model.Product
import com.mvpmatch.vendingmachine.model.ProductPurchaseDto
import com.mvpmatch.vendingmachine.model.Role
import com.mvpmatch.vendingmachine.model.User
import com.mvpmatch.vendingmachine.repository.ProductRepository
import com.mvpmatch.vendingmachine.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.math.BigDecimal
import java.util.*
import kotlin.contracts.contract

class ProductPurchaseServiceTest {

    lateinit var testee: ProductPurchaseService
    lateinit var userRepository: UserRepository
    lateinit var productRepository: ProductRepository

    @BeforeEach
    fun setup() {
        userRepository = mock()
        productRepository = mock()
        testee = ProductPurchaseService(
            userRepository,
            productRepository,
        )
    }

    @Test
    fun thatInsufficientStockThrows() {
        //given
        val product = Product(UUID.randomUUID(), 1, 100, "someProduct", UUID.randomUUID())
        whenever(productRepository.findById(any())).thenReturn(Optional.of(product))

        //when //then
        assertThatThrownBy { testee.purchase(product.id, ProductPurchaseDto(UUID.randomUUID(), 2)) }
            .isInstanceOf(OutOfStockException::class.java)
            .hasMessage("So sorry, product ${product.productName} is out of stock.")
    }

    @Test
    fun that1ProductIsPurchasedAndUpdatesSaved() {
        //given
        val userId = UUID.randomUUID()
        val product = Product(UUID.randomUUID(), 10, 150, "someProduct", UUID.randomUUID())
        val user = User(userId, "someUsername", "secret", 1300, Role.BUYER)
        whenever(productRepository.findById(any())).thenReturn(Optional.of(product))
        whenever(userRepository.findById(any())).thenReturn(Optional.of(user))

        //when
        val result = testee.purchase(product.id, ProductPurchaseDto(userId, 1))

        //then
        argumentCaptor<Product>().apply {
            verify(productRepository).save(capture())

            assertThat(firstValue).isEqualTo(product.copy(amountAvailable = 9))
        }
        argumentCaptor<User>().apply {
            verify(userRepository).save(capture())

            assertThat(firstValue).isEqualTo(user.copy(deposit = 0))
        }
        verify(productRepository).findById(product.id);
        verify(userRepository).findById(user.id);
        assertThat(result).isEqualTo(PurchaseResponseDto(150, "someProduct", List(11) {100} + listOf(50)))
    }

    @Test
    fun thatMultipleProductArePurchasedAndUpdatesSaved() {
        //given
        val userId = UUID.randomUUID()
        val product = Product(UUID.randomUUID(), 10, 150, "someProduct", UUID.randomUUID())
        val user = User(userId, "someUsername", "secret", 1335, Role.BUYER)
        whenever(productRepository.findById(any())).thenReturn(Optional.of(product))
        whenever(userRepository.findById(any())).thenReturn(Optional.of(user))

        //when
        val result = testee.purchase(product.id, ProductPurchaseDto(userId, 3))

        //then
        argumentCaptor<Product>().apply {
            verify(productRepository).save(capture())

            assertThat(firstValue).isEqualTo(product.copy(amountAvailable = 7))
        }
        argumentCaptor<User>().apply {
            verify(userRepository).save(capture())

            assertThat(firstValue).isEqualTo(user.copy(deposit = 0))
        }
        verify(productRepository).findById(product.id);
        verify(userRepository).findById(user.id);
        assertThat(result).isEqualTo(PurchaseResponseDto(450, "someProduct",
            List(8) {100} + listOf(50) + listOf(20) + listOf(10) + listOf(5))
        )
    }

}