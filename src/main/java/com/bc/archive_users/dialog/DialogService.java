package com.bc.archive_users.dialog;

import com.bc.archive_users.dialog.message.Message;
import com.bc.archive_users.dialog.message.MessageDto;
import com.bc.archive_users.dialog.message.MessageRepository;
import com.bc.archive_users.tools.ConverterDto;
import com.bc.archive_users.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class DialogService {
    @Autowired private DialogRepository dialogRepository;
    @Autowired private MessageRepository messageRepository;
    @Autowired private ConverterDto converterDto;

    private static Logger log = Logger.getLogger(DialogService.class.getName());

    public String addNewDialog(Dialog dialog, Long[] users_id){
        dialogRepository.save(dialog);
        //Для всех участников диалога (может быть больше двух при создании бесед) создать связь с диалогом
        Long dialog_id = dialog.getId();
        for (Long value : users_id)
            dialogRepository.addNewUsersDialog(value, dialog_id);
        return "dialog is save";
    }

    public List<DialogDto> findAllDialogs(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("findAllDialogs of User: " + user.getLogin());
        Long user_id = user.getId();
        List<Dialog> dialogs = dialogRepository.findAllDialogs(user_id);
        List<DialogDto> dialogDtos = new ArrayList<>();
        for (Dialog value : dialogs) {
            //Для диалогов без имени, установить имя собеседника
            if (value.getName() == null)
                value.setName(dialogRepository.getNameOfFriendsForDialogs(user_id, value.getId()));
            dialogDtos.add(converterDto.fromDialogToDialogDto(value));
        }
        return dialogDtos;
    }

    public List<MessageDto> findAllMessagesOfDialog(Long dialog_id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //Проверка, принимает ли пользователь участие в диалоге
        if (dialogRepository.userIsInTheDialog(user.getId(), dialog_id) != null) {
            log.info("findAllMessagesOfDialog: " + dialog_id + " of user: " + user.getLogin());
            return converterDto.converterListMessageToListMessageDto(messageRepository.findAllMessagesOfDialog(dialog_id));
        }
        return null;
    }

    public String saveNewMessage(Message message) {
        messageRepository.save(message);
        return "message is save";
    }
}
