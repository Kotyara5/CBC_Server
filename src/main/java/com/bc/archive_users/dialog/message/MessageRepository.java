package com.bc.archive_users.dialog.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * FROM messages WHERE dialogs_id = ?1", nativeQuery = true)
    List<Message> findAllMessagesOfDialog(Long dialog_id);

    @Query(value = "SELECT content FROM messages WHERE id = (SELECT MAX(id) FROM messages WHERE dialogs_id = ?1)", nativeQuery = true)
    String getLastMessageOfDialog(Long dialog_id);
}
