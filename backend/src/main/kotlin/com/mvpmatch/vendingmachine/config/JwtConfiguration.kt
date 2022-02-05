package com.mvpmatch.vendingmachine.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import java.security.Key
import java.security.KeyStore
import java.security.PublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey


@Configuration
class JwtConfiguration {

    @Value("\${security.jwt.keystore-location}")
    private val keyStorePath: String? = null

    @Value("\${security.jwt.keystore-password}")
    private val keyStorePassword: String? = null

    @Value("\${security.jwt.key-alias}")
    private val keyAlias: String? = null

    @Value("\${security.jwt.private-key-passphrase}")
    private val privateKeyPassphrase: String? = null

    @Bean
    fun keyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        val resourceAsStream = Thread.currentThread().contextClassLoader.getResourceAsStream(keyStorePath)
        keyStore.load(resourceAsStream, keyStorePassword!!.toCharArray())
        return keyStore
    }

    @Bean
    fun jwtSigningKey(keyStore: KeyStore): RSAPrivateKey? {
        val key: Key = keyStore.getKey(keyAlias, privateKeyPassphrase!!.toCharArray())
        if (key is RSAPrivateKey) {
            return key
        }
        throw IllegalArgumentException("Unable to load RSA private key")
    }

    @Bean
    fun jwtValidationKey(keyStore: KeyStore): RSAPublicKey? {
        val certificate = keyStore.getCertificate(keyAlias)
        val publicKey: PublicKey = certificate.publicKey
        if (publicKey is RSAPublicKey) {
            return publicKey
        }
        throw IllegalArgumentException("Unable to load RSA public key")
    }

    @Bean
    fun jwtDecoder(rsaPublicKey: RSAPublicKey?): JwtDecoder? {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build()
    }

}