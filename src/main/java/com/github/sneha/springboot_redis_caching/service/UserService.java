package com.github.sneha.springboot_redis_caching.service;

import com.github.sneha.springboot_redis_caching.exception.UserDetailsNotFoundException;
import com.github.sneha.springboot_redis_caching.model.User;
import com.github.sneha.springboot_redis_caching.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user operations such as retrieving, creating,
 * updating, and deleting users. It also integrates caching with Redis for better performance.
 *
 * @author sneharavikumartl
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

    /**
     * Retrieves a user by their userId from the cache or the database.
     * The result is cached with the userId as the key.
     * If the user is not found in the cache, it fetches from the database.
     *
     * @param userId the ID of the user to retrieve
     * @return the user with the given userId
     */
    @Cacheable(value = "users", key = "#userId") // Caches the result of this method with userId as the key
    public User getUserById(@Valid String userId) {
        // If the user is not found in the cache, it will fetch from DB
        log.info("Fetching user with ID: {} from the database", userId);
        return userRepository.findById(userId).orElseThrow(() -> new UserDetailsNotFoundException("User not found for the Id "+userId));
    }

    /**
     * Creates a new user and saves it to the database.
     * This method does not apply caching.
     *
     * @param user the user object to create
     * @return the created user
     */
    public User createUser(@Valid User user) {
        user.setUserId(generateUserId());  // Automatically generate userId
        return userRepository.save(user);  // Save the new user to the database
    }

    /**
     * Updates an existing user's information and applies cache operations.
     * The cache is updated with the new user data if user data is present in cache
     *
     * @param userId the ID of the user to update
     * @param updatedUser the updated user data
     * @return the updated user
     */
    public User updateUser(String userId, @Valid User updatedUser) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserDetailsNotFoundException("User not found for the Id - " + userId));
        user.setUserName(updatedUser.getUserName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setAddress(updatedUser.getAddress());
        User savedUser = userRepository.save(user);

        Cache cache = cacheManager.getCache("users");
        if (cache != null && cache.get(userId) != null)// Check if the user is in cache
            cache.put(userId, savedUser); // Update cache only if the user is present

        return savedUser;
    }

    /**
     * Deletes a user by their userId and evicts the corresponding cache.
     *
     * @param userId the ID of the user to delete
     */
    @CacheEvict(value = "users", key = "#userId") // Evicts the user from the cache when deleted
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserDetailsNotFoundException("User not found for the Id - "+userId));
        userRepository.delete(user);  // Delete the user from the database
    }

    /**
     * Utility method to generate a unique userId (example using UUID).
     *
     * @return a unique userId
     */
    private String generateUserId() {
        return java.util.UUID.randomUUID().toString(); // Generates a random UUID as the userId
    }

}
