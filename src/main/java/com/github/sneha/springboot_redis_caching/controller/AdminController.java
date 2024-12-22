package com.github.sneha.springboot_redis_caching.controller;

import com.github.sneha.springboot_redis_caching.advice.LogExecutionTime;
import com.github.sneha.springboot_redis_caching.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class handles administrative operations such as user management
 * and system configuration. It uses Spring Security for authentication
 * based on username and password.
 *
 * @author sneharavikumartl
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    private UserService userService;

    /**
     * Clears the Redis cache for users.
     * This operation will clear any cached user data stored in Redis under the "users" cache.
     *
     * @return a message indicating successful cache clearance
     */
    @DeleteMapping("/clearAllCache")
    @Operation(summary = "Clear Cache", description = "Clears the Redis cache for users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cache successfully cleared"),
            @ApiResponse(responseCode = "500", description = "Error while clearing cache")
    })
    @LogExecutionTime // Custom annotation to log the execution time of this method
    public String clearRedisCache() {
        // Check if the cache for users exists, and if so, clear it
        if (cacheManager.getCache("users") != null) {
            cacheManager.getCache("users").clear();
        }
        return "User cache cleared successfully"; // Return success message after cache is cleared
    }

    /**
     * Deletes a user by their userId.
     * This method interacts with the UserService to delete a user by their ID.
     *
     * @param userId the ID of the user to be deleted
     */
    @DeleteMapping("/deleteUser/{userId}")
    @Operation(summary = "Delete User", description = "Deletes a user by their unique userId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId); // Delegate the deletion logic to the UserService
    }
}
