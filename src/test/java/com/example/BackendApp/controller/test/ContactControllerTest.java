package com.example.BackendApp.controller.test;
import com.example.BackendApp.controller.ContactController;
import com.example.BackendApp.model.Contact;
import com.example.BackendApp.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    private Contact testContact;

    @BeforeEach
    void setUp() {
        testContact = new Contact();
        testContact.setId(1L);
        testContact.setFirstName("John");
        testContact.setLastName("Doe");
        testContact.setPhoneNumber("1234567890");
        testContact.setEmail("john@example.com");
        testContact.setBirthdate(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testAddContact() {
        when(contactService.addContact(1L, testContact)).thenReturn(testContact);

        ResponseEntity<Contact> response = contactController.addContact(1L, testContact);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testContact, response.getBody());
        verify(contactService, times(1)).addContact(1L, testContact);
    }

    @Test
    void testGetAllContacts() {
        List<Contact> contacts = Collections.singletonList(testContact);
        when(contactService.getAllContacts(1L)).thenReturn(contacts);

        ResponseEntity<List<Contact>> response = contactController.getAllContacts(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contacts, response.getBody());
        verify(contactService, times(1)).getAllContacts(1L);
    }

    @Test
    void testGetContact_UserAndContact() {
        when(contactService.getContactById(1L, 1L)).thenReturn(testContact);

        ResponseEntity<Contact> response = contactController.getContact(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testContact, response.getBody());
        verify(contactService, times(1)).getContactById(1L, 1L);
    }

    @Test
    void testGetContactById() {
        when(contactService.getContactById(1L)).thenReturn(testContact);

        ResponseEntity<Contact> response = contactController.getContactById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testContact, response.getBody());
        verify(contactService, times(1)).getContactById(1L);
    }

    @Test
    void testDeleteContact() {
        doNothing().when(contactService).deleteContact(1L, 1L);

        ResponseEntity<Void> response = contactController.deleteContact(1L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(contactService, times(1)).deleteContact(1L, 1L);
    }

    @Test
    void testGetAllContactsWithPagination() {
        List<Contact> contacts = Collections.singletonList(testContact);
        when(contactService.getAllContacts(1L, 0, 10, "firstName")).thenReturn(contacts);

        ResponseEntity<List<Contact>> response = contactController.getAllContacts(1L, 0, 10, "firstName");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contacts, response.getBody());
        verify(contactService, times(1)).getAllContacts(1L, 0, 10, "firstName");
    }
}