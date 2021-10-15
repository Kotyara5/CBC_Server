package com.bc.archive_users.user;

import com.bc.archive_users.dialog.DialogService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private DialogService dialogServiceMock;

    @InjectMocks
    private UserService userService;

    static User user_1 = new User("login", "pass", "name", "email", EnumUserRole.USER);
    static User user_2 = new User("login2", "pass2", "name2", "email2", EnumUserRole.USER);
    static User user_3 = new User("login3", "pass3", "name3", "email3", EnumUserRole.USER);

    @BeforeAll
    static void setUp() {
        user_1.setId(1);
        user_2.setId(2);
        user_3.setId(3);
    }

    @BeforeEach
    void setAuth(){
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user_1, null, user_1.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void findAll_success() {
        List<User> users = new ArrayList<>(Arrays.asList(user_1, user_2, user_3));
        Mockito.when(userRepositoryMock.findAll()).thenReturn(users);
        List<UserDto> userDtos = userService.findAll();
        assertEquals(userDtos.size(), users.size());
    }

    @Test
    void addNewFriend_alreadyFriend() {
        Mockito.when(userRepositoryMock.usersIsFriends(anyLong(), anyLong())).thenReturn(1);

        String s = userService.addNewFriend(2L);
        assertEquals("the user is already a friend", s);
    }

    @Test
    void addNewFriend_addFriend() {
        Mockito.when(userRepositoryMock.usersIsFriends(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);

        String s = userService.addNewFriend(2L);
        assertEquals("user added to friends", s);
    }

    @Test
    void findAllFriends_success() {
        List<User> users = new ArrayList<>(Arrays.asList(user_1, user_2, user_3));

        Mockito.when(userRepositoryMock.findAllFriends(Mockito.anyLong())).thenReturn(users);

        List<UserDto> userDtos = userService.findAllFriends();
        assertEquals(userDtos.size(), 3);
    }
}