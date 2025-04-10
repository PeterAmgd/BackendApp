package com.example.BackendApp.repository;


import com.example.BackendApp.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUserId(Long userId);
    Optional<Contact> findByIdAndUserId(Long id, Long userId);
    Page<Contact> findAllByUserId(Long userId, Pageable pageable);
}
