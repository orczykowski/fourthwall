package com.fourthwall.moviedb;

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [MovieDbApplication::class, IntegrationConfig::class]
)

@ActiveProfiles("integration")
abstract class BaseIntegrationTest
