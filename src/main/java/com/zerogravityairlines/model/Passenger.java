package com.zerogravityairlines.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

@Entity
@Table(name = "passenger")
@Data
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotEmpty(message = "First Name is required")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "Last Name is required")
    private String lastName;

    @Column(name = "age")
    @NotNull(message = "Age is required")
    private int age;

    @Column(name = "gender")
    @NotEmpty(message = "Gender is required")
    private String gender;

    @Column(name = "email")
    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "phone_number")
    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number should be 10 digits")
    private String phoneNumber;


    @ManyToOne
    @JoinColumn(name = "confirmation_number", nullable = false)
    @JsonBackReference
    private Booking booking;
}