package com.mvpmatch.vendingmachine.model

import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

@Document(collection = "product")
data class Product(
    val id: UUID,
    var amountAvailable: Int,
    var cost: BigDecimal,
    var productName: String,
    val sellerId: UUID,
)

data class ProductCreationDto(
    val amountAvailable: Int,
    val cost: BigDecimal,
    val productName: String,
    val sellerId: UUID,
)

data class ProductDto(
    val id: UUID,
    val amountAvailable: Int,
    val cost: BigDecimal,
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
    val cost: BigDecimal,
    val productName: String,
)