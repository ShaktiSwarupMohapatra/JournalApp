package net.engineeringdigest.journalApp.services;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserServices {
    @Autowired
    private UserRepository userRepository;
//    private static final Logger logger = LoggerFactory.getLogger(UserServices.class);
    public static final PasswordEncoder pswd=new BCryptPasswordEncoder();

    public void saveUser(User user){userRepository.save(user);}

    public User saveNewUser(User user){
        log.info("Attempting to create user with username: {}", user.getUsername());
        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("User creation failed. Username already exists: {}", user.getUsername());
            throw new RuntimeException("Username already exists");
        }

        try{
            user.setId(null);
            user.setPassword(pswd.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            User inserted = userRepository.insert(user);
            log.info("User created successfully with id: {} and username: {}",
                    inserted.getId(), inserted.getUsername());
            return inserted;
        }
        catch (Exception e) {
            log.error("Unexpected error while creating user: {}", user.getUsername(), e);
            throw e; // rethrow so controller can return correct HTTP status
        }





    }
    public void saveAdmin(User user){
        user.setPassword(pswd.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    public List<User> showAll(){return userRepository.findAll();}

    public Optional<User> findById(ObjectId id){return userRepository.findById(id);}

    public void deletById(ObjectId id){userRepository.deleteById(id);}

    public User findByUsername(String username){ return userRepository.findByUsername(username);}



}
