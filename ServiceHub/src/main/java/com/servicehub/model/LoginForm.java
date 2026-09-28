package com.servicehub.model;
import jakarta.validation.constraints.*;
public class LoginForm {
 @NotBlank(message="Email cannot be empty") @Email(message="Enter a valid email address") private String email;
 @NotBlank(message="Password cannot be empty") private String password;
 public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getPassword(){return password;} public void setPassword(String v){password=v;}
}
