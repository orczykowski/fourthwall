package com.fourthwall.moviedb.cnfiguration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf(Customizer.withDefaults())
            .authorizeHttpRequests { authorize ->
                authorize.requestMatchers("api/public/**").permitAll()
                authorize.requestMatchers("actuator/**").permitAll()
                authorize.requestMatchers("docs/**").permitAll()
            }.httpBasic(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun userDetailsService(inMemoryUserProvider: InMemoryUserProvider): UserDetailsService {
        val users = inMemoryUserProvider.users.map {
            User.builder()
                .username(it.username)
                .password(passwordEncoder().encode(it.password))
                .roles("ADMIN")
                .build()
        }.toList()
        return InMemoryUserDetailsManager(users)
    }


    @ConfigurationProperties(prefix = "dummy")
    data class InMemoryUserProvider(val users: List<InMemoryUserDefinition>)

    data class InMemoryUserDefinition(val username: String, val password: String)
}