package com.example.backendtest.authentication.repository;

import com.example.backendtest.authentication.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}

