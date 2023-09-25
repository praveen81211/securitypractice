package com.example.SecurityPractise.service;

import com.example.SecurityPractise.entity.UserInfo;
import com.example.SecurityPractise.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String addUsers(UserInfo userInfo){
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
         userRepo.save(userInfo);
         return "User added successfully";
    }

}
