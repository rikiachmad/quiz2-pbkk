package com.example.application.data.repository;

import com.example.application.data.entity.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SeminarRepository extends JpaRepository<Seminar, UUID> {

    @Query("select s from Seminar s " +
            "where lower(s.name) like lower(concat('%', :searchTerm, '%')) ")
    List<Seminar> search(@Param("searchTerm") String searchTerm);
}
