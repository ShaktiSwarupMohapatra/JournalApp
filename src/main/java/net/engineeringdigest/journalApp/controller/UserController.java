package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private UserRepository userRepository;

//    @GetMapping
//    public ResponseEntity<?> getAll(){
//        List<User> entries = userServices.showAll();
//        if (entries.isEmpty()){
//            return ResponseEntity.notFound().build();
//        }
//        else {
//            return ResponseEntity.ok(entries);
//        }
//    }



//    @DeleteMapping("id/{myid}")
//    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myid){
//        journalEntryServices.deletById(myid);
//        return ResponseEntity.noContent().build();
//    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDB = userServices.findByUsername(username);
            userInDB.setUsername(user.getUsername());
            userInDB.setPassword(user.getPassword());
            userServices.saveNewUser(userInDB);
            return ResponseEntity.ok(userInDB);

    }
    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());
        return ResponseEntity.noContent().build();

    }


}
