package com.mvpmatch.vendingmachine.exception

val ALLOWED_DEPOSIT_VALUES = listOf(5, 10, 20, 50, 100)

class InvalidDepositException(message: String): Exception(message)