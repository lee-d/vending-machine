package com.mvpmatch.vendingmachine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class VendingMachineApplication {

	@Bean
	fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

}

fun main(args: Array<String>) {
	runApplication<VendingMachineApplication>(*args)
}
