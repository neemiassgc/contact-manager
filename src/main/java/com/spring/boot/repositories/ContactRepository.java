package com.spring.boot.repositories;

import com.spring.boot.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("select c, p, e, a from Contact c join fetch c.phoneNumberMap p join fetch c.emailMap e join fetch addressMap a")
    List<Contact> getAll();
}
