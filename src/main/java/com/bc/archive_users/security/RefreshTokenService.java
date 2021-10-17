package com.bc.archive_users.security;

import com.bc.archive_users.tools.ConverterDto;
import com.bc.archive_users.user.User;
import com.bc.archive_users.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class RefreshTokenService {
    @Autowired private RefreshTokenRepository refreshTokenRepository;
    @Autowired private ConverterDto converterDto;

    private static Logger log = Logger.getLogger(JwtProvider.class.getName());

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(UserDto userDto) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(converterDto.fromUserDtoToUser(userDto));
        refreshToken.setExpiryDate(Instant.now().plusMillis(86400000)); //Сутки
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public void deleteAllRefreshToken(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = user.getId();
        refreshTokenRepository.deleteAllRefreshToken(user_id);
    }
}
