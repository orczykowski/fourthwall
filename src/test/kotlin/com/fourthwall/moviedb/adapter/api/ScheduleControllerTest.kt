package com.fourthwall.moviedb.adapter.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(ScheduleController::class)
class ScheduleControllerTest {
    @Autowired
    lateinit var mvc: MockMvc
}