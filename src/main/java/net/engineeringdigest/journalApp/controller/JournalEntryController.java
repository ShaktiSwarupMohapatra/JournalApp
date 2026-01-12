package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.services.JournalEntryServices;
import net.engineeringdigest.journalApp.services.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryServices journalEntryServices;
    @Autowired
    private UserServices userServices;


    @GetMapping
    public ResponseEntity<?> getJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userServices.findByUsername(username);
        List<JournalEntry> entries = user.getJournalEntries();
        if (entries.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(entries);
        }
    }
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry jentry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            journalEntryServices.saveEntry(jentry, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(jentry);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("id/{myid}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userServices.findByUsername(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid)).collect(Collectors.toList());
        if (!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryServices.findById(myid);
//        return journalEntry.isPresent()? new ResponseEntity<>(journalEntry.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (journalEntry.isPresent()){
                return ResponseEntity.ok(journalEntry.get());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{myid}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed = journalEntryServices.deletById(myid, username);
        if (removed){return ResponseEntity.noContent().build();}
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{myid}")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable ObjectId myid,
            @RequestBody JournalEntry newEntry) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userServices.findByUsername(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryServices.findById(myid);

            if (journalEntry.isPresent()) {
                JournalEntry oldEntry = journalEntry.get();
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());
                journalEntryServices.saveEntry(oldEntry);
                return ResponseEntity.ok(oldEntry);
            }

        }
        return ResponseEntity.notFound().build();
    }

}
