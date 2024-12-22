package com.github.sneha.springboot_redis_caching.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a user entity with relevant details such as username, email, phone number, and address.
 * This class is mapped to the "users" table in the database.
 * It includes validation constraints to ensure data integrity.
 *
 * @author sneharavikumartl
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @Column(name = "user_id")
    private String userId;

    /**
     * The name of the user.
     * Cannot be blank and must be between 3 and 50 characters.
     */
    @Column(name = "user_name", nullable = false)
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String userName;

    /**
     * The email address of the user.
     * Cannot be blank and must follow a valid email format.
     */
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    /**
     * The phone number of the user.
     * Must be exactly 10 digits.
     */
    @Column(name = "phone_number")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    /**
     * The address of the user.
     */
    @Column(name = "address")
    private String address;
}
