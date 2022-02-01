package com.mvpmatch.vendingmachine.controller

import com.mvpmatch.vendingmachine.exception.NoModelFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ResponseExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [NoModelFoundException::class])
    protected fun handleNotFound(
        ex: Exception, request: WebRequest
    ): ResponseEntity<Any?>? {
        return handleExceptionInternal(
            ex, ex.message,
            HttpHeaders(), HttpStatus.NOT_FOUND, request
        )
    }

}
