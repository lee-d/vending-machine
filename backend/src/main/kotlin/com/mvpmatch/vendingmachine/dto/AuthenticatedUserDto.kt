package com.mvpmatch.vendingmachine.dto

import java.util.*

data class AuthenticatedUserDto(
    val userId: UUID,
    val username: String,
    val token: String,
    val roles: String,
)