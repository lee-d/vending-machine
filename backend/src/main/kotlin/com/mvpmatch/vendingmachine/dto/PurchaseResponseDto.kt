package com.mvpmatch.vendingmachine.dto

import java.math.BigDecimal

data class PurchaseResponseDto(
    val totalPrice: BigDecimal,
    val productName: String,
    val remainingDeposit: BigDecimal,
)