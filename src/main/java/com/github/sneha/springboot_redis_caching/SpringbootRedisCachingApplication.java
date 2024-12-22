package com.github.sneha.springboot_redis_caching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * This application is to demo the implementation of redis in spring boot projects.
 * This contains essential configurations and common dependencies for building
 * modern java applications
 *
 * @author sneharavikumartl
 */

@EnableCaching
@SpringBootApplication
public class SpringbootRedisCachingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootRedisCachingApplication.class, args);
	}

}
