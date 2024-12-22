package com.github.sneha.springboot_redis_caching.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sneha.springboot_redis_caching.model.User;
import com.github.sneha.springboot_redis_caching.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


/**
 * Controller for managing user-related operations.
 * Provides endpoints to create, retrieve, and update user information.
 *
 * @author sneharavikumartl
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Create a new user.
     *
     * @param user the user object to be created
     * @return the created user
     */
    @Operation(summary = "Create a new user", description = "Creates a new user with auto-generated userId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/addUser")
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * Retrieve a user by userId.
     *
     * @param userId the ID of the user to retrieve
     * @return the user if found
     */
    @Operation(summary = "Get a user by ID", description = "Retrieves a user by their unique userId. Cached for performance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/getUser/{userId}")
    public User getUser(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    /**
     * Update an existing user.
     *
     * @param userId the ID of the user to update
     * @param user   the updated user object
     * @return the updated user
     */
    @Operation(summary = "Update an existing user", description = "Updates a user's details by their userId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })

    @PostMapping("/updateUser/{userId}")
    public User updateUser(@PathVariable String userId, @Valid @RequestBody User user) {
        //log.info("Received raw JSON: {}", rawJson);
        log.info("Received User: {}", user);
        return userService.updateUser(userId, user);
    }

}
