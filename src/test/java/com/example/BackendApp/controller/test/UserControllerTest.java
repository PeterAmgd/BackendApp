package com.example.BackendApp.controller.test;

import com.example.BackendApp.controller.UserController;
import com.example.BackendApp.model.User;
import com.example.BackendApp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("createdUser");
        testUser.setEmail("usercontro@gm.com");
        testUser.setPassword("abcd0000");
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Collections.singletonList(testUser);
        when(userService.allUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).allUsers();
    }

    @Test
    void testGetSingleUser() {
        when(userService.singleUser("createdUser")).thenReturn(Optional.of(testUser));

        ResponseEntity<Optional<User>> response = userController.getSingleUser("createdUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals(testUser, response.getBody().get());
        verify(userService, times(1)).singleUser("createdUser");
    }

    @Test
    void testSignup_Success() {
        when(userService.singleUser("usercontro@gm.com")).thenReturn(Optional.empty());
        when(userService.register(testUser)).thenReturn(testUser);

        ResponseEntity<?> response = userController.signup(testUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).register(testUser);
    }

    @Test
    void testSignup_EmailTaken() {
        when(userService.singleUser("usercontro@gm.com")).thenReturn(Optional.of(testUser));

        ResponseEntity<?> response = userController.signup(testUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already taken", response.getBody());
    }

    @Test
    void testLogin_Success() {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", "usercontro@gm.com");
        payload.put("password", "abcd0000");

        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("token", "jwt_token");
        loginResult.put("user", testUser);

        when(userService.loginUser("usercontro@gm.com", "abcd0000")).thenReturn(loginResult);

        ResponseEntity<?> response = userController.login(payload);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginResult, response.getBody());
        verify(userService, times(1)).loginUser("usercontro@gm.com", "abcd0000");
    }

    @Test
    void testLogin_Failure() {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", "usercontro@gm.com");
        payload.put("password", "wrongpass");

        when(userService.loginUser("usercontro@gm.com", "wrongpass"))
                .thenThrow(new RuntimeException("Wrong password"));

        ResponseEntity<?> response = userController.login(payload);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Wrong password", body.get("error"));
    }
}
