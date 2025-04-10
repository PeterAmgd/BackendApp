package com.example.BackendApp.service.test;

import com.example.BackendApp.config.JwtTokenProvider;
import com.example.BackendApp.model.User;
import com.example.BackendApp.repository.UserRepository;
import com.example.BackendApp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("createdUser");
        testUser.setEmail("testing@gm.com");
        testUser.setPassword("abcd1234");
    }

    @Test
    void testAllUsers() {
        List<User> users = Collections.singletonList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.allUsers();

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testSingleUser() {
        when(userRepository.findByUsername("createdUser")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.singleUser("createdUser");

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository, times(1)).findByUsername("createdUser");
    }

    @Test
    void testCreateUser() {
        when(passwordEncoder.encode("abcd1234")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User createdUser = userService.register(testUser);

        assertEquals(testUser, createdUser);
        assertEquals("hashedPassword", testUser.getPassword());
        verify(passwordEncoder, times(1)).encode("abcd1234");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testRegister() {
        when(passwordEncoder.encode("abcd1234")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User registeredUser = userService.register(testUser);

        assertEquals(testUser, registeredUser);
        verify(passwordEncoder, times(1)).encode("abcd1234");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findByEmail("testing@gm.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userService.loadUserByUsername("testing@gm.com");

        assertEquals("testing@gm.com", userDetails.getUsername());
        assertEquals("abcd1234", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        verify(userRepository, times(1)).findByEmail("testing@gm.com");
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent@example.com");
        });
    }

    @Test
    void testLoginUser_Success() {
        when(userRepository.findByEmail("testing@gm.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("abcd1234", "abcd1234")).thenReturn(true);
        when(jwtTokenProvider.generateToken("testing@gm.com")).thenReturn("jwt_token");

        Map<String, Object> result = userService.loginUser("testing@gm.com", "abcd1234");

        assertNotNull(result);
        assertEquals("jwt_token", result.get("token"));
        assertEquals(testUser, result.get("user"));
        verify(passwordEncoder, times(1)).matches("abcd1234", "abcd1234");
        verify(jwtTokenProvider, times(1)).generateToken("testing@gm.com");
    }

    @Test
    void testLoginUser_WrongPassword() {
        when(userRepository.findByEmail("testing@gm.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpass", "abcd1234")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            userService.loginUser("testing@gm.com", "wrongpass");
        });
    }

    @Test
    void testLoginUser_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.loginUser("nonexistent@example.com", "abcd1234");
        });
    }
}