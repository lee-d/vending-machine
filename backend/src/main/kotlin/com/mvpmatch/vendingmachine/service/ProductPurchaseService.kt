package com.mvpmatch.vendingmachine.service

import com.mvpmatch.vendingmachine.dto.PurchaseResponseDto
import com.mvpmatch.vendingmachine.exception.NoModelFoundException
import com.mvpmatch.vendingmachine.exception.OutOfStockException
import com.mvpmatch.vendingmachine.model.ProductPurchaseDto
import com.mvpmatch.vendingmachine.repository.ProductRepository
import com.mvpmatch.vendingmachine.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductPurchaseService(
    val userRepository: UserRepository,
    val productRepository: ProductRepository
) {

    fun purchase(productId: UUID, productPurchaseDto: ProductPurchaseDto): PurchaseResponseDto {
        val product = productRepository.findByIdOrNull(productId) ?: throw NoModelFoundException("No product found for id $productId")
        if (product.hasInsufficientAvailability(productPurchaseDto.amount)) {
            throw OutOfStockException("So sorry, product ${product.productName} is out of stock.")
        }
        val totalPrice = product.calculateTotalPrice(productPurchaseDto.amount)
        product.reduceAvailability(productPurchaseDto.amount)

        val user = userRepository.findByIdOrNull(productPurchaseDto.userId) ?: throw NoModelFoundException("No user found for id ${productPurchaseDto.userId}")
        val change = user.payWithDeposit(totalPrice)
        productRepository.save(product)
        userRepository.save(user)

        return PurchaseResponseDto(
            totalPrice = totalPrice,
            productName = product.productName,
            change = change,
        )
    }

}