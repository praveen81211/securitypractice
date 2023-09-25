package com.example.SecurityPractise.filter;

import com.example.SecurityPractise.service.JwtService;
import com.example.SecurityPractise.service.UserInfoUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoUserDetailsService userInfoUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Authorization is what we have in postman where we are getting this bearer token
        //Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrdW1hciIsImlhdCI6MTY5NDg3MDcxMCwiZXhwIjoxNjk0ODcyNTEwfQ.zHL2U4aiZ7q05gcEs6jjhkc-0IFs6KiU64lpe2aD0KI
        //token starts with Bearer by default
        String authHeader= request.getHeader("Authorization");
        String token=null;
        String username = null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
           username = jwtService.extractUsername(token);
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
           UserDetails userDetails = userInfoUserDetailsService.loadUserByUsername(username);
           if(jwtService.validateToken(token,userDetails)) {
               UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authToken);
           }
        }
        filterChain.doFilter(request,response);
    }
}
