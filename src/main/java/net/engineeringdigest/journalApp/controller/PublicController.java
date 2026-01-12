package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserServices userServices;

    @PostMapping("/create")
    public ResponseEntity<User> createEntry(@RequestBody User users){
        try {
            userServices.saveNewUser(users);
            return ResponseEntity.status(HttpStatus.CREATED).body(users);
        }
        catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }
}
