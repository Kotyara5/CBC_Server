package com.bc.archive_users.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login); //Метод авторизации

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO friends VALUES (?1, ?2)", nativeQuery = true)
    void addNewFriend(Long user_id, Long friend_id);

    //Проверка, друзья ли пользователи (если да, вернёт Integer, если нет, то null)
    @Query(value = "SELECT 1 FROM friends WHERE user_id = ?1 AND friend_id = ?2 OR user_id = ?2 AND friend_id = ?1", nativeQuery = true)
    Integer usersIsFriends(Long user_id, Long friend_id);

    //Получаем всех друзей определённого пользователя
    @Query(value = "SELECT * FROM users WHERE id IN " +
            "(SELECT friend_id FROM friends WHERE user_id = ?1 " +
            "UNION SELECT user_id FROM friends WHERE friend_id = ?1 )", nativeQuery = true)
    List<User> findAllFriends(Long id);

    @Query("UPDATE User a SET a.enabled = TRUE WHERE a.login = ?1") //Найти пользователя по логину и установить enabled = TRUE
    int enableUser(String login);
}
