package io.agilehandy.boot2withkotlin

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.websocket.server.PathParam

/**
 * A quick spring boot with Kotlin
 */

@SpringBootApplication
class Boot2WithKotlinApplication

fun main(args: Array<String>) {
    runApplication<Boot2WithKotlinApplication>(*args)
}


@Component
class Commander (val repository: CustomerRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val customersList = listOf<Customer>(Customer("Haytham"), Customer("Hisham"), Customer("Hani"))
        customersList.forEach { customer -> repository.save(customer) }
    }
}

@RestController
class HelloWorldController (val customers: CustomerService) {

    @GetMapping("/")
    fun hello()  =  "Hello World"

    @GetMapping("/customers")
    fun customers() = customers.findAllCustomers()

    @GetMapping("/customers/{id}")
    fun OneCustomer(@PathParam("id") id: Int) = customers.findCustomer(id)
}

@Entity
data class Customer (val name: String = "", @Id @GeneratedValue var id: Int? = null)

interface CustomerRepository: CrudRepository<Customer, Int> {
    override fun findById(id: Int?): Optional<Customer>
    override fun findAll(): MutableIterable<Customer>
    override fun <S : Customer?> save(p0: S): S
    override fun <S : Customer?> saveAll(p0: MutableIterable<S>?): MutableIterable<S>
}

@Service
class CustomerService (val repository: CustomerRepository) {
    fun findCustomer(id: Int) = repository.findById(id)
    fun findAllCustomers() = repository.findAll()
}