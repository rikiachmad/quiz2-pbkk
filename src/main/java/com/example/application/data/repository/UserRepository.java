package com.example.application.data.repository;

import com.example.application.data.entity.Seminar;
import com.example.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("select u from User u " +
            "where lower(u.username) like lower(concat('%', :searchTerm, '%')) ")
    List<User> search(@Param("searchTerm") String searchTerm);

    @Query("select u from User u where u.username = :username")
    public User findByUsername(@Param("username") String username);

    @Query("select u from User u where u.email = :email")
    public User findByEmail(@Param("email") String email);
}
