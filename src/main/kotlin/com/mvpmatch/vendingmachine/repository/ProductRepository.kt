package com.mvpmatch.vendingmachine.repository

import com.mvpmatch.vendingmachine.model.Product
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ProductRepository: MongoRepository<Product, UUID>{
}