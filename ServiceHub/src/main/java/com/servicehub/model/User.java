package com.servicehub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="users", uniqueConstraints=@UniqueConstraint(name="uk_users_email", columnNames="email"))
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=100) private String name;
    @Column(nullable=false, unique=true, length=150) private String email;
    @Column(nullable=false, length=30) private String phone;
    @Column(nullable=false, length=100) private String password;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private Role role;
    @Column(name="created_at", nullable=false, updatable=false) private LocalDateTime createdAt;
    protected User() {}
    public User(String name,String email,String phone,String password,Role role){this.name=name;this.email=email;this.phone=phone;this.password=password;this.role=role;}
    @PrePersist void setCreatedAt(){if(createdAt==null) createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public String getName(){return name;} public String getEmail(){return email;} public String getPhone(){return phone;} public String getPassword(){return password;} public Role getRole(){return role;} public LocalDateTime getCreatedAt(){return createdAt;}
}
