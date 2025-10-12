package com.productmodel.productmodel.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productmodel.productmodel.model.Images;

@Repository
public interface ImagesRepository extends JpaRepository<Images , UUID>{
    
}
