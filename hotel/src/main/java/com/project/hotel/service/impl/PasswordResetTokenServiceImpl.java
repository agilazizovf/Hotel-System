package com.project.hotel.service.impl;

import com.project.hotel.entity.AdminEntity;
import com.project.hotel.entity.PasswordResetTokenEntity;
import com.project.hotel.entity.UserEntity;
import com.project.hotel.exception.CustomException;
import com.project.hotel.repository.AdminRepository;
import com.project.hotel.repository.PasswordResetTokenRepository;
import com.project.hotel.repository.UserRepository;
import com.project.hotel.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void initiatePasswordReset(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("İstifadəçi tapılmadı", "User not found",
                        "Not found", 404, null));

        Optional<UserEntity> user = userRepository.findByEmail(userEntity.getEmail());
        if (user.isEmpty()) {
            throw new CustomException("İstifadəçi müştəri ilə əlaqəli deyil","User not associated with client",
                    "Bad Request", 400, null);
        }

        // Delete any existing token for the user
        tokenRepository.deleteByUser_Email(user.get().getEmail());

        // Generate and save a new token with expiration date
        String token = generateAndSaveActivationToken(userEntity.getEmail());

        // Send the reset email
        sendResetEmail(userEntity.getEmail(), token);
    }

    private void sendResetEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, take this token:\n" +token);

        mailSender.send(message);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetTokenEntity resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidParameterException("Invalid token"));

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new InvalidParameterException("Token has expired");
        }

        UserEntity user = userRepository.findByEmail(resetToken.getUser().getEmail())
                .orElseThrow(() -> new CustomException("İstifadəçi tapılmadı", "User not found",
                        "Not found", 404, null));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Optionally, delete the token after use
        tokenRepository.deleteByToken(token);
    }


    private String generateAndSaveActivationToken(String email) {
        String generatedToken = generateActivationCode();
        PasswordResetTokenEntity token = PasswordResetTokenEntity.builder()
                .token(generatedToken)
                .expirationDate(LocalDateTime.now().plusMinutes(15))  // Set expiration date here
                .user(userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found!")))
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
