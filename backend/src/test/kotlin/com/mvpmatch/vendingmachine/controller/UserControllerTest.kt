package com.mvpmatch.vendingmachine.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mvpmatch.vendingmachine.model.Role
import com.mvpmatch.vendingmachine.model.User
import com.mvpmatch.vendingmachine.model.UserCreationDto
import com.mvpmatch.vendingmachine.repository.UserRepository
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockBean
    lateinit var respository: UserRepository

    @Test
    fun thatUserCreated() {
        //given
        val userId = UUID.randomUUID()
        `when`(respository.save(any())).thenReturn(User(userId, "foo", "bar", 0, Role.SELLER))
        val objectMapper = ObjectMapper()
        val json = objectMapper.writeValueAsString(
            UserCreationDto(
                "foo",
                "bar",
                Role.BUYER
            )
        )

        //when then
        this.mockMvc.perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andDo(MockMvcResultHandlers.print())
            .andExpect(content().string(containsString(userId.toString())))
            .andExpect(status().isCreated())
    }
}