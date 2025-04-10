package com.example.BackendApp.service.test;
import com.example.BackendApp.model.Contact;
import com.example.BackendApp.model.User;
import com.example.BackendApp.repository.ContactRepository;
import com.example.BackendApp.repository.UserRepository;
import com.example.BackendApp.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContactService contactService;

    private User testUser;
    private Contact testContact;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testContact = new Contact();
        testContact.setId(1L);
        testContact.setFirstName("John");
        testContact.setLastName("Doe");
        testContact.setPhoneNumber("1234567890");
        testContact.setEmail("john@example.com");
        testContact.setBirthdate(LocalDate.of(1990, 1, 1));
        testContact.setUser(testUser);
    }

    @Test
    void testAddContact_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(contactRepository.save(any(Contact.class))).thenReturn(testContact);

        Contact result = contactService.addContact(1L, testContact);

        assertEquals(testContact, result);
        assertEquals(testUser, result.getUser());
        verify(userRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).save(testContact);
    }

    @Test
    void testAddContact_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            contactService.addContact(1L, testContact);
        });
    }

    @Test
    void testGetAllContacts() {
        List<Contact> contacts = Collections.singletonList(testContact);
        when(contactRepository.findByUserId(1L)).thenReturn(contacts);

        List<Contact> result = contactService.getAllContacts(1L);

        assertEquals(1, result.size());
        assertEquals(testContact, result.get(0));
        verify(contactRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetContactById_UserAndContact() {
        when(contactRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testContact));

        Contact result = contactService.getContactById(1L, 1L);

        assertEquals(testContact, result);
        verify(contactRepository, times(1)).findByIdAndUserId(1L, 1L);
    }

    @Test
    void testGetContactById_ContactOnly() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(testContact));

        Contact result = contactService.getContactById(1L);

        assertEquals(testContact, result);
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void testGetContactById_NotFound() {
        when(contactRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            contactService.getContactById(1L, 1L);
        });
    }

    @Test
    void testDeleteContact_Success() {
        when(contactRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testContact));

        contactService.deleteContact(1L, 1L);

        verify(contactRepository, times(1)).findByIdAndUserId(1L, 1L);
        verify(contactRepository, times(1)).delete(testContact);
    }

    @Test
    void testDeleteContact_NotFound() {
        when(contactRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            contactService.deleteContact(1L, 1L);
        });
    }

    @Test
    void testGetAllContactsWithPagination() {
        Page<Contact> contactPage = new PageImpl<>(Collections.singletonList(testContact));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("firstName").ascending());
        when(contactRepository.findAllByUserId(1L, pageable)).thenReturn(contactPage);

        List<Contact> result = contactService.getAllContacts(1L, 0, 10, "firstName");

        assertEquals(1, result.size());
        assertEquals(testContact, result.get(0));
        verify(contactRepository, times(1)).findAllByUserId(1L, pageable);
    }
}