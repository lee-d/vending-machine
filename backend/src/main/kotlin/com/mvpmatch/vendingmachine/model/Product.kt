package com.mvpmatch.vendingmachine.model

import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

@Document(collection = "product")
data class Product(
    val id: UUID,
    var amountAvailable: Int,
    var cost: Int,
    var productName: String,
    val sellerId: UUID,
) {
    fun calculateTotalPrice(amount: Int): Int {
        return cost.times(amount)
    }

    fun hasInsufficientAvailability(amount: Int): Boolean {
        return amountAvailable < amount
    }

    fun reduceAvailability(amount: Int) {
        this.amountAvailable -= amount
    }
}

data class ProductCreationDto(
    val amountAvailable: Int,
    val cost: Int,
    val productName: String,
    val sellerId: UUID,
)

data class ProductDto(
    val id: UUID,
    val amountAvailable: Int,
    val cost: Int,
    val productName: String,
) {
    companion object {
        fun fromProduct(product: Product): ProductDto {
            return ProductDto(
                product.id,
                product.amountAvailable,
                product.cost,
                product.productName,
            )
        }
    }
}

data class ProductUpdateDto(
    val amountAvailable: Int,
    val cost: Int,
    val productName: String,
)

data class ProductPurchaseDto(
    val userId: UUID,
    val amount: Int
)