package com.usermodel.usermodel.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usermodel.usermodel.model.UserDetails;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails,UUID> {
    
}
