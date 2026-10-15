// Spring Boot entry point — starts the whole backend on port 8080.
package com.salehhafyane.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Component scanning starts here, so everything below this package gets picked up.
@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

}