package com.saqaya.userapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.saqaya.userapi.model.User;
import com.saqaya.userapi.model.UserCreateCredentialsDTO;
import com.saqaya.userapi.model.UserResponseDTO;
import com.saqaya.userapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public UserCreateCredentialsDTO createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        try {
            UserCreateCredentialsDTO credentialsDTO= new UserCreateCredentialsDTO();
            String salt = "450d0b0db2bcf4adde5032eca1a7c416e560cf44";
            String sha1Id = generateSha1Hash(user.getEmail() + salt);
            String jwtToken = createJwtToken(user.getEmail());

            user.setId(sha1Id);
            user.setAccessToken(jwtToken);
            userRepository.save(user);

            logger.info("User created with ID: {}", sha1Id);
            return credentialsDTO.userToDTO(user);

        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
                throw e; // Rethrow the exception or handle it based on your application's needs
        }
    }

    private String generateSha1Hash(String value) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(value.getBytes("utf8"));
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }

    private String createJwtToken(String email) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                .sign(algorithm);
    }

    public UserResponseDTO getUser(String id, String accessToken) {
        try {
            UserResponseDTO responseDTO= new UserResponseDTO();
            DecodedJWT jwt = verifyJwtToken(accessToken);
            if (jwt == null) {
                logger.warn("Invalid access token for ID: {}", id);
                throw new JWTVerificationException("Invalid access token");
            }

            User user = userRepository.findById(id).orElse(null);
            if (user != null && !jwt.getSubject().equals(user.getEmail())) {
                logger.warn("Access token does not match user's email for ID: {}", id);
                throw new JWTVerificationException("Access token does not match user");
            }
            if (!user.isMarketingConsent()) {
                responseDTO.setEmail(null);
            }
            responseDTO.userToDTO(user);
            return responseDTO;
        } catch (JWTVerificationException exception) {
            logger.error("JWT verification error: {}", exception.getMessage());
            throw exception;
        }
    }

    private DecodedJWT verifyJwtToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        return JWT.require(algorithm)
                .build()
                .verify(token);
    }
}
