package com.example.BackendApp.controller;

import com.example.BackendApp.model.Contact;
import com.example.BackendApp.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/Contacts")

public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/{userId}")
    public ResponseEntity<Contact> addContact(@PathVariable Long userId, @RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.addContact(userId, contact));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Contact>> getAllContacts(@PathVariable Long userId) {
        return ResponseEntity.ok(contactService.getAllContacts(userId));
    }

    @GetMapping("/{userId}/{contactId}")
    public ResponseEntity<Contact> getContact(@PathVariable Long userId, @PathVariable Long contactId) {
        return ResponseEntity.ok(contactService.getContactById(userId, contactId));
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long contactId) {
        return ResponseEntity.ok(contactService.getContactById(contactId));
    }

    @DeleteMapping("/{userId}/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long userId, @PathVariable Long contactId) {
        contactService.deleteContact(userId, contactId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sorting /{userId}")
    public ResponseEntity<List<Contact>> getAllContacts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy
    ) {
        return ResponseEntity.ok(contactService.getAllContacts(userId, page, size, sortBy));
    }
}
