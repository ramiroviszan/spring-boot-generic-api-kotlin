package com.blackboxdynamics.example

import org.modelmapper.ModelMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SpringBootKotlinExampleApplication {
//    @Bean
//    fun modelMapper(): ModelMapper {
//        return ModelMapper()
//    }
}

fun main(args: Array<String>) {
    runApplication<SpringBootKotlinExampleApplication>(*args)
}

