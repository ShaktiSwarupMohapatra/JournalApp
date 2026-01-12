package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserServices userServices;

    @GetMapping("/getall")
    public ResponseEntity<?> getAllUsers() {
        List<User> all = userServices.showAll();
        if (all != null && !all.isEmpty()) {
            return ResponseEntity.ok(all);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public void createAdmin(@RequestBody User user) {
        userServices.saveAdmin(user);

    }
}
