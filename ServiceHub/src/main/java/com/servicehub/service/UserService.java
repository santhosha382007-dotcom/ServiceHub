package com.servicehub.service;
import com.servicehub.model.*;
import com.servicehub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service public class UserService {
 private final UserRepository users; private final PasswordEncoder encoder;
 public UserService(UserRepository users,PasswordEncoder encoder){this.users=users;this.encoder=encoder;}
 @Transactional public User register(RegistrationForm form){String email=normalize(form.getEmail()); if(users.existsByEmailIgnoreCase(email)) throw new IllegalArgumentException("An account with that email already exists"); if(!Objects.equals(form.getPassword(),form.getConfirmPassword())) throw new IllegalArgumentException("Passwords do not match"); return users.save(new User(form.getName().trim(),email,form.getPhone().trim(),encoder.encode(form.getPassword()),Role.USER));}
 @Transactional(readOnly=true) public Optional<User> authenticate(String email,String password){return users.findByEmailIgnoreCase(normalize(email)).filter(user->encoder.matches(password,user.getPassword()));}
 @Transactional public User createAdminIfMissing(String name,String email,String phone,String password){return users.findByEmailIgnoreCase(normalize(email)).orElseGet(()->users.save(new User(name,normalize(email),phone,encoder.encode(password),Role.ADMIN)));}
 @Transactional(readOnly=true) public long countUsers(){return users.count();}
 private String normalize(String email){return email==null?"":email.trim().toLowerCase(Locale.ROOT);}
}
