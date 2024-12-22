package com.github.sneha.springboot_redis_caching.repository;

import com.github.sneha.springboot_redis_caching.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for interacting with the "users" table in the database.
 * It extends JpaRepository, providing CRUD operations and more.
 *
 * @author sneharavikumartl
 */
public interface UserRepository extends JpaRepository<User, String> {
    // JpaRepository provides built-in methods for basic CRUD operations (e.g., save, findAll, findById, deleteById).
}
