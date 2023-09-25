package com.example.SecurityPractise.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(String username){
        Map<String,Object> claims= new HashMap<>();
        return createToken(claims,username);
    }

    private String createToken(Map<String, Object> claims, String username) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode("SjTUzouSBo3YEbTsSe+VDMJ81z2/IRWZ4VOTee25L9pTJgZzoLC0TzgmRuqqf+Fc4kFO9iLd/9lSC6R29wPOVTR5HgxU6JOSbADeNLDHjXOxMKdPEGUHgEQ6MF2K+0sXr47NnF/2+VOMHX2POy4OpAHIly/8NHLGA0XwEpQqjFEBVfiIcdqXNK4dy4fO2qMrIA1tl5Qy/QFZN+oAidWiJLbpDgxsXTSrVyhnGQqX4J7rxYfro89cO7edmtNX4ArsF5O+2UWvJm0H6q3WkXzmFzAkiI5L5CpRYzxZmZ/OMmOoyqT2IrRG4C29YJorEaMZrcsDtSDq3eQNiNfd2y3bdL7L9snigZYu5TqPRpG8mKc=");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
