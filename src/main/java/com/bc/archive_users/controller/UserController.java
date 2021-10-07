package com.bc.archive_users.controller;

import com.bc.archive_users.registration.*;
import com.bc.archive_users.security.JwtProvider;
import com.bc.archive_users.security.RefreshToken;
import com.bc.archive_users.security.RefreshTokenService;
import com.bc.archive_users.user.User;
import com.bc.archive_users.user.UserDto;
import com.bc.archive_users.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired private UserService userService;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private RefreshTokenService refreshTokenService;

    private static Logger log = Logger.getLogger(UserController.class.getName());

    @PostMapping("/register")
    public String register(@RequestBody RegistrationRequest request) {
        return userService.register(request);
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        log.info("auth: " + request.getLogin());
        UserDto userDto = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userDto.getLogin());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDto);

        return new AuthResponse(token, refreshToken.getToken(), userDto);
    }

    @PostMapping("/refreshtoken")
    public TokenRefreshResponse refreshtoken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken);
        User user = refreshTokenService.verifyExpiration(refreshToken).getUser();
        String token = jwtProvider.generateToken(user.getUsername());
        return new TokenRefreshResponse(token, requestRefreshToken);
    }

    @GetMapping("/findAll")
    public List<UserDto> findAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/addNewFriend")
    public String addNewFriend(@RequestParam Long friend_id) {
        return userService.addNewFriend(friend_id);
    }

    @GetMapping("/findAllFriends")
    public List<UserDto> findAllFriends() {
        return userService.findAllFriends();
    }

}
