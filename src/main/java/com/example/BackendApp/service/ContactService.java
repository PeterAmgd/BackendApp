package com.example.BackendApp.service;

import com.example.BackendApp.model.Contact;

import com.example.BackendApp.model.User;
import com.example.BackendApp.repository.ContactRepository;
import com.example.BackendApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    public Contact addContact(Long userId, Contact contact) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        contact.setUser(user);

        return contactRepository.save(contact);
    }

    public List<Contact> getAllContacts(Long userId) {
        return contactRepository.findByUserId(userId);
    }

    public Contact getContactById(Long userId, Long contactId) {
        return contactRepository.findByIdAndUserId(contactId, userId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
    }
    public Contact getContactById( Long contactId) {
        return contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public void deleteContact(Long userId, Long contactId) {
        Contact contact = contactRepository.findByIdAndUserId(contactId, userId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contactRepository.delete(contact);
    }

    public List<Contact> getAllContacts(Long userId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return contactRepository.findAllByUserId(userId, pageable).getContent();
    }
}
