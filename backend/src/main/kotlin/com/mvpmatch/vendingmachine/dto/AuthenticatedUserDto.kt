package com.mvpmatch.vendingmachine.dto

import java.util.*

data class AuthenticatedUserDto(
    val username: String,
    val token: String,
    val roles: String,
)
