package com.usermodel.usermodel.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.usermodel.usermodel.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,UUID> {
    
    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);
    User findByToken(String token);
}
