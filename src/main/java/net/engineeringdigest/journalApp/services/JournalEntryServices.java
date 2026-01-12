package net.engineeringdigest.journalApp.services;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryServices {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private  UserServices  userServices;
    @Transactional
    public void saveEntry(JournalEntry journalEntry , String username){
        try {
            User user = userServices.findByUsername(username);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userServices.saveUser(user);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error occured while saving the entry "+e);
        }

    }
    public void saveEntry(JournalEntry journalEntry){journalEntryRepository.save(journalEntry);}

    public List<JournalEntry> showAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }
    @Transactional
    public boolean deletById(ObjectId id, String username){
        boolean removed = false;
        try {
            User user = userServices.findByUsername(username);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed){
                userServices.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        }
        catch (Exception e) {
            log.error("Error while deleting the entry "+e);
            throw new RuntimeException("An error occured while deleting the entry "+e);
        }
        return removed;
    }



}
