package com.example.SecurityPractise.service;

import com.example.SecurityPractise.config.UserInfoUserDetails;
import com.example.SecurityPractise.entity.UserInfo;
import com.example.SecurityPractise.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<UserInfo> userInfo = Optional.ofNullable(userRepo.findByUsername(username));
      return userInfo.map(UserInfoUserDetails::new)
               .orElseThrow(()->new UsernameNotFoundException("user not found "));

    }
}
