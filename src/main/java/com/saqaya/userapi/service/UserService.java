package com.saqaya.userapi.service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.saqaya.userapi.model.User;
import com.saqaya.userapi.reposatory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        // Generate SHA1 hash for ID
        String salt = "450d0b0db2bcf4adde5032eca1a7c416e560cf44";
        String sha1Id = generateSha1Hash(user.getEmail() + salt);

        // Generate JWT token for accessToken
        String jwtToken = createJwtToken(user.getEmail());

        user.setId(sha1Id);
        user.setAccessToken(jwtToken);
        userRepository.save(user);

        return user;
    }

    private String generateSha1Hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(value.getBytes("utf8"));
            return String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException("Error generating SHA-1 hash", e);
        }
    }

    private String createJwtToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            return JWT.create()
                    .withSubject(email)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour expiry
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            throw new RuntimeException("Error creating JWT token", exception);
        }
    }

    public User getUser(String id, String accessToken) throws Exception{
        try {
            // Verify the access token
            DecodedJWT jwt = verifyJwtToken(accessToken);
            if (jwt == null) {
                throw new Exception("invalid access Token");
            }
            // Fetch user
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                // Check if JWT subject matches user's email
                if (!jwt.getSubject().equals(user.getEmail())) {
                    throw new Exception(" access Token does not match user");
                }

                // Omit email if marketingConsent is false
                if (!user.isMarketingConsent()) {
                    user.setEmail(null);
                }
            }
            return user;
        } catch (JWTVerificationException exception) {
            throw  exception;
        }
    }

    private DecodedJWT verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret"); // Use the same secret key used for creating the token
            return JWT.require(algorithm)
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            return null;
        }
    }}
