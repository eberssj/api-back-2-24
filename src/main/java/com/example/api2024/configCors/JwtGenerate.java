package com.example.api2024.configCors;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import com.example.api2024.service.AdmDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGenerate {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    @SuppressWarnings("deprecation")
    public String AdmTokenDetailsImpl(AdmDetails tokenDetail) {
        return Jwts.builder().setSubject(tokenDetail.getUsername()).setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + expirationTime)).signWith(signinKey(), SignatureAlgorithm.HS512).compact();}

    public Key signinKey() { SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        return key;}

    @SuppressWarnings("deprecation")
    public String UsernameToken(String token) {return Jwts.parser().setSigningKey(signinKey()).build().parseClaimsJws(token).getBody().getSubject();}

    @SuppressWarnings("deprecation")
    public boolean JwtToken(String authToken) {
        try { Jwts.parser().setSigningKey(signinKey()).build().parseClaimsJws(authToken);
            return true;}
        catch(MalformedJwtException e) {
            System.out.println("Token inválido " + e.getMessage());}
        catch(ExpiredJwtException e) {
            System.out.println("Token expirado " + e.getMessage());}
        catch(UnsupportedJwtException e) {
            System.out.println("Token não suportado " + e.getMessage());}
        catch(IllegalArgumentException e) {
            System.out.println("Token Argumento inválido " + e.getMessage());}

        return false;
    }
}