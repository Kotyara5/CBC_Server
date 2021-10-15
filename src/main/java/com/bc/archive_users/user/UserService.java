package com.bc.archive_users.user;

import com.bc.archive_users.dialog.Dialog;
import com.bc.archive_users.dialog.DialogRepository;
import com.bc.archive_users.dialog.DialogService;
import com.bc.archive_users.registration.RegistrationRequest;
import com.bc.archive_users.tools.ConverterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService implements UserDetailsService {

    @Autowired private UserRepository userRepository;
    @Autowired private DialogRepository dialogRepository;
    @Autowired private DialogService dialogService;
    private final ConverterDto converterDto = new ConverterDto();
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static Logger log = Logger.getLogger(UserService.class.getName());

    @Override
    public User loadUserByUsername(String login) {
            return userRepository.findByLogin(login);
    }

    public String register(RegistrationRequest request) {
        //Проверка на существование пользователя (логина)
        if (userRepository.findByLogin(request.getLogin()) != null) {
            return "login exists";
        }

        User user = new User(
                        request.getLogin(),
                        request.getPassword(),
                        request.getName(),
                        request.getEmail(),
                        EnumUserRole.USER
                    );
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        return "user is save";
    }

    public int enableUser(String login) { //Активировать аккаунт пользователя (Пока активен по умолчанию)
        return userRepository.enableUser(login);
    }

    public UserDto findByLoginAndPassword(String login, String password) { //Авторизация
        User user = userRepository.findByLogin(login);
        if (user != null) {
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return converterDto.fromUserToUserDto(user);
            }
        }
        return null;
    }

    public List<UserDto> findAll() {
        return converterDto.converterListUserToListUserDto(userRepository.findAll());
    }

    public String addNewFriend(Long friend_id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long users_id = user.getId();

        log.info("addNewFriend: id=" + friend_id.toString() + " to this user: " + user.getLogin() + "(" + user.getId() + ")");
        String isFriends = "the user is already a friend";
        String addedFriends = "user added to friends";

        //Проверка, являются ли пользователи уже друзьями
        if (userRepository.usersIsFriends(users_id, friend_id) != null) {
            log.info(isFriends);
            return isFriends;
        }

        Dialog dialog = new Dialog(null, null); //Если диалог без названия (null), то на месте названия должно отображаться имя собеседника
        dialogService.addNewDialog(dialog, new Long[]{users_id, friend_id});
        userRepository.addNewFriend(users_id, friend_id);

        log.info(addedFriends);
        return addedFriends;
    }

    public List<UserDto> findAllFriends() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("findAllFriends of User: " + user.getLogin());
        return converterDto.converterListUserToListUserDto(userRepository.findAllFriends(user.getId()));
    }
}
