package com.mvpmatch.vendingmachine.repository

import com.mvpmatch.vendingmachine.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository: MongoRepository<User, UUID>{

    fun findByUsername(username: String): User?

}