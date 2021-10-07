package com.bc.archive_users.dialog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface DialogRepository extends JpaRepository<Dialog, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO usersdialogs VALUES (?1, ?2)", nativeQuery = true)
    void addNewUsersDialog(Long user_id, Long dialog_id);

    @Query(value = "SELECT * FROM dialogs WHERE id IN " +
            "(SELECT dialogs_id FROM usersdialogs WHERE user_id = ?1)", nativeQuery = true)
    List<Dialog> findAllDialogs(Long id);

    //Проверка, участвует ли пользователь в диалоге (если да, вернёт Integer, если нет, то null)
    @Query(value = "SELECT 1 FROM usersdialogs WHERE user_id = ?1 AND dialogs_id = ?2", nativeQuery = true)
    Integer userIsInTheDialog(Long user_id, Long dialog_id);

    //Выбрать имя собеседника (Находятся два участника диалога и берётся имя того, чей ид не совпадает с авторизированным пользователем)
    @Query(value = "SELECT name FROM users WHERE id IN " +
            "(SELECT user_id FROM usersdialogs WHERE dialogs_id = ?2 AND NOT user_id = ?1)", nativeQuery = true)
    String getNameOfFriendsForDialogs(Long user_id, Long dialog_id);
}
