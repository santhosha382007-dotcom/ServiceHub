package com.servicehub.model;
import jakarta.validation.constraints.*;
public class RegistrationForm {
 @NotBlank(message="Name cannot be empty") private String name;
 @NotBlank(message="Email cannot be empty") @Email(message="Enter a valid email address") private String email;
 @NotBlank(message="Phone cannot be empty") private String phone;
 @NotBlank(message="Password cannot be empty") private String password;
 @NotBlank(message="Confirm your password") private String confirmPassword;
 public String getName(){return name;} public void setName(String v){name=v;} public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getPhone(){return phone;} public void setPhone(String v){phone=v;} public String getPassword(){return password;} public void setPassword(String v){password=v;} public String getConfirmPassword(){return confirmPassword;} public void setConfirmPassword(String v){confirmPassword=v;}
}
