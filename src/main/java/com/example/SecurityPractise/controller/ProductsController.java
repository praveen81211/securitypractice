package com.example.SecurityPractise.controller;

import com.example.SecurityPractise.dto.AuthRequest;
import com.example.SecurityPractise.entity.UserInfo;
import com.example.SecurityPractise.repo.UserRepo;
import com.example.SecurityPractise.service.JwtService;
import com.example.SecurityPractise.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String getProducts(){
        return  "Welcome to Production unit";
    }

    @GetMapping("/all-products")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String allProducts(){
        return "all the products are empty";
    }

    @GetMapping("/reviews")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String allReviews(){
        return "all products reviews are good";
    }

    @GetMapping("/user-info")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public UserInfo userInfo(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return   userRepo.findByUsername(username);

    }


    @PostMapping("/add-user")
    public String addUsers(@RequestBody UserInfo userInfo){
        return productService.addUsers(userInfo);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        //authentication for verify the user before generating the token
      Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
      if(authentication.isAuthenticated()){
          return jwtService.generateToken(authRequest.getUsername());
      }
      else {
          throw new UsernameNotFoundException("Invalid user request !");
      }

    }


}
