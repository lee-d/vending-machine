package com.mvpmatch.vendingmachine.model

import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

@Document(collection = "product")
data class Product(
    var id: UUID,
    val amountAvailable: Int,
    val cost: BigDecimal,
    val productName: String,
    var sellerId: UUID,
)

data class ProductCreationDto(
    var amountAvailable: Int,
    var cost: BigDecimal,
    var productName: String,
    var sellerId: UUID,
)

data class ProductDto(
    var id: UUID,
    var amountAvailable: Int,
    var cost: BigDecimal,
    var productName: String,
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
    var amountAvailable: Int,
    var cost: BigDecimal,
    var productName: String,
)