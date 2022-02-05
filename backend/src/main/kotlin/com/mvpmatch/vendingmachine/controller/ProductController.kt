package com.mvpmatch.vendingmachine.controller

import com.mvpmatch.vendingmachine.dto.PurchaseResponseDto
import com.mvpmatch.vendingmachine.exception.NoModelFoundException
import com.mvpmatch.vendingmachine.model.*
import com.mvpmatch.vendingmachine.repository.ProductRepository
import com.mvpmatch.vendingmachine.service.ProductPurchaseService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(
    value = ["/api/v1/products"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class ProductController(
    private val productRepository: ProductRepository,
    private val productPurchaseService: ProductPurchaseService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody dto: ProductCreationDto): UUID {
        val createdProduct = productRepository.save(
            Product(
                UUID.randomUUID(),
                dto.amountAvailable,
                dto.cost,
                dto.productName,
                dto.sellerId
            )
        )
        return createdProduct.id
    }

    @GetMapping("/{id}")
    fun findProductById(@PathVariable id: UUID): ProductDto {
        return productRepository.findByIdOrNull(id)?.let { ProductDto.fromProduct(it) }
            ?: throw NoModelFoundException("No product found with id $id")
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteProduct(@PathVariable id: UUID) {
        productRepository.deleteById(id)
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: UUID, @RequestBody dto: ProductUpdateDto): ProductDto {
        val updatedProduct =
            productRepository.findByIdOrNull(id)?.let {
                it.copy(amountAvailable = dto.amountAvailable,
                cost = dto.cost, productName = dto.productName)
            } ?: throw NoModelFoundException("No product found with id $id")
        return updatedProduct
            .apply {productRepository.save(this)}
            .run { ProductDto.fromProduct(this) }
    }

    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    @PutMapping("/{id}/purchase")
    fun deposit(@PathVariable id: UUID, @RequestBody dto: ProductPurchaseDto): PurchaseResponseDto {
        return productPurchaseService.purchase(id, dto)
    }

}