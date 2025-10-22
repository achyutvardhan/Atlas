package com.usermodel.usermodel.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.usermodel.usermodel.model.User;


@Repository
public interface UserRepository extends JpaRepository<User,UUID> {
    User findByUserName(String userName);
}
