package com.ecom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserDtls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String mobileNumber;
    private String email;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String password;
    private String profileImage;
    private String role;

    private Boolean isEnable;         // active or inactive user
    private Boolean accountNonLocked; // account lock status for failed attempts

    private int failedAttempt;
    private Date lockTime;
    private String resetToken;

    // -----------------------------
    // CUSTOM METHODS REQUIRED BY YOUR SECURITY CODE
    // -----------------------------

    // For checking user activation status
    public boolean isEnable() {
        return Boolean.TRUE.equals(isEnable);
    }

    // For checking account locking status
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(accountNonLocked);
    }
}
