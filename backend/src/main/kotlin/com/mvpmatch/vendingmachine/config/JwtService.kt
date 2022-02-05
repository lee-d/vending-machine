package com.mvpmatch.vendingmachine.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.stereotype.Service
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.*

@Service
class JwtService(
    val privateKey: RSAPrivateKey,
    val publicKey: RSAPublicKey
) {

    fun createJwtForClaims(subject: String, claims: Map<String, String>): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = Instant.now().toEpochMilli()
        calendar.add(Calendar.DATE, 1)
        val jwtBuilder = JWT.create().withSubject(subject)

        claims.forEach { (name: String, value: String) ->
            jwtBuilder.withClaim(name, value)
        }

        return jwtBuilder
            .withNotBefore(Date())
            .withExpiresAt(calendar.time)
            .sign(Algorithm.RSA256(publicKey, privateKey))
    }

}