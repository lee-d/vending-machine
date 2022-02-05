package com.mvpmatch.vendingmachine.dto

data class PurchaseResponseDto(
    val totalPrice: Int,
    val productName: String,
    val change: List<Int>,
)