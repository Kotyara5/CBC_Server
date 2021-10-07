package com.bc.archive_users.controller;

import com.bc.archive_users.dialog.DialogDto;
import com.bc.archive_users.dialog.DialogService;
import com.bc.archive_users.dialog.message.Message;
import com.bc.archive_users.dialog.message.MessageDto;
import com.bc.archive_users.tools.ConverterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/dialogs")
@CrossOrigin
public class DialogController {
    @Autowired private DialogService dialogService;
    @Autowired private ConverterDto converterDto;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    private static Logger log = Logger.getLogger(DialogController.class.getName());

    @GetMapping("/findAllDialogs")
    public List<DialogDto> findAllDialogs() {
        return dialogService.findAllDialogs();
    }

    @GetMapping("/findAllMessagesOfDialog")
    public List<MessageDto> findAllMessagesOfDialog(@RequestParam Long dialog_id) {
        return dialogService.findAllMessagesOfDialog(dialog_id);
    }

    @MessageMapping("/sendMessage")
    public void messageConvertAndSend(@RequestBody MessageDto messageDto) {
        messageDto.setTimestamp(Instant.now().toString());
        Message message = converterDto.fromMessageDtoToMessage(messageDto);
        dialogService.saveNewMessage(message);
        log.info("dialog id: " + message.getDialog().getId() + " message content: " + messageDto.getContent());
        //Отправка сообщения активным в чате пользователям
        messagingTemplate.convertAndSend("/queue/dialog_" + messageDto.getDialog_id() + "/messages", converterDto.fromMessageToMessageDto(message));
        //Отправка диалога для обновления "последнего сообщения" диалога в списке диалогов
        messagingTemplate.convertAndSend("/queue/dialog_" + messageDto.getDialog_id(), converterDto.fromDialogToDialogDto(message.getDialog()));
    }
}
