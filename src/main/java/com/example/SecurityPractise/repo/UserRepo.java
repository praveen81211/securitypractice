package com.example.SecurityPractise.repo;

import com.example.SecurityPractise.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserInfo,Integer> {
    UserInfo findByUsername(String username);
}
