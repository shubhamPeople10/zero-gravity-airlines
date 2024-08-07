package com.zerogravityairlines.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Entity
@Table(name = "passenger")
@Data
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First Name can only contain alphabets")
    @Size(max = 20, message = "First Name cannot be longer than 20 characters")
    private String firstName;

    @Column(name = "last_name")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last Name can only contain alphabets")
    @Size(max = 20, message = "Last Name cannot be longer than 20 characters")
    private String lastName;

    @Column(name = "age")
    @NotNull(message = "Age is required")
    private int age;

    @Column(name = "gender")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @Column(name = "email")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "phone_number")
    @Pattern(regexp = "^[0-9]{0,10}$", message = "Phone Number should be numeric and up to 10 digits")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "confirmation_number", nullable = false)
    @JsonBackReference
    private Booking booking;

    @Column(name = "is_primary")
    @JsonProperty("isPrimary")
    private boolean isPrimary = false;

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}