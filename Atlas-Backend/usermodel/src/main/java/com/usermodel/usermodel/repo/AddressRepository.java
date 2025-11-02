package com.usermodel.usermodel.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usermodel.usermodel.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,UUID> {
    
}
