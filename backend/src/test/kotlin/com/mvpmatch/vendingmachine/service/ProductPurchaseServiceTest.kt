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
        val product = Product(UUID.randomUUID(), 1, BigDecimal.ONE, "someProduct", UUID.randomUUID())
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
        val product = Product(UUID.randomUUID(), 10, BigDecimal(1.5), "someProduct", UUID.randomUUID())
        val user = User(userId, "someUsername", "secret", BigDecimal(13), Role.BUYER)
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

            assertThat(firstValue).isEqualTo(user.copy(deposit = BigDecimal(11.5)))
        }
        verify(productRepository).findById(product.id);
        verify(userRepository).findById(user.id);
        assertThat(result).isEqualTo(PurchaseResponseDto(BigDecimal(1.5), "someProduct", BigDecimal(11.5)))
    }

    @Test
    fun thatMultipleProductArePurchasedAndUpdatesSaved() {
        //given
        val userId = UUID.randomUUID()
        val product = Product(UUID.randomUUID(), 10, BigDecimal(1.5), "someProduct", UUID.randomUUID())
        val user = User(userId, "someUsername", "secret", BigDecimal(13), Role.BUYER)
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

            assertThat(firstValue).isEqualTo(user.copy(deposit = BigDecimal(8.5)))
        }
        verify(productRepository).findById(product.id);
        verify(userRepository).findById(user.id);
        assertThat(result).isEqualTo(PurchaseResponseDto(BigDecimal(4.5), "someProduct", BigDecimal(8.5)))
    }

}