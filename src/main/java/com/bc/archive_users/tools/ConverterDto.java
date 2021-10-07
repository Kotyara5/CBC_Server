package com.bc.archive_users.tools;

import com.bc.archive_users.dialog.Dialog;
import com.bc.archive_users.dialog.DialogDto;
import com.bc.archive_users.dialog.DialogRepository;
import com.bc.archive_users.dialog.message.Message;
import com.bc.archive_users.dialog.message.MessageDto;
import com.bc.archive_users.dialog.message.MessageRepository;
import com.bc.archive_users.user.User;
import com.bc.archive_users.user.UserDto;
import com.bc.archive_users.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConverterDto {
    @Autowired private UserRepository userRepository;
    @Autowired private DialogRepository dialogRepository;
    @Autowired private MessageRepository messageRepository;

    public User fromUserDtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setLogin(userDto.getLogin());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        return user;
    }

    public UserDto fromUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public List<UserDto> converterListUserToListUserDto(List<User> user) {
        List<UserDto> userDto = new ArrayList<>();
        for (User value : user) {
            userDto.add(fromUserToUserDto(value));
        }
        return userDto;
    }

    public DialogDto fromDialogToDialogDto(Dialog dialog) {
        DialogDto dialogDto = new DialogDto();
        dialogDto.setId(dialog.getId());
        dialogDto.setName(dialog.getName());
        if (dialog.getUser() != null)
            dialogDto.setUser_id(dialog.getUser().getId());
        dialogDto.setLastMessage(messageRepository.getLastMessageOfDialog(dialog.getId()));
        return dialogDto;
    }

    public List<MessageDto> converterListMessageToListMessageDto(List<Message> message) {
        List<MessageDto> messageDto = new ArrayList<>();
        for (Message value : message) {
            messageDto.add(fromMessageToMessageDto(value));
        }
        return messageDto;
    }

    public Message fromMessageDtoToMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setId(messageDto.getId());
        message.setDialog(dialogRepository.findById(messageDto.getDialog_id()).get());
        message.setUser(userRepository.findById(messageDto.getSender_id()).get());
        message.setContent(messageDto.getContent());
        message.setTimestamp(Instant.parse(messageDto.getTimestamp()));
        message.setType(messageDto.getType());
        message.setStatus(messageDto.getStatus());
        return message;
    }

    public MessageDto fromMessageToMessageDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setDialog_id(message.getDialog().getId());
        messageDto.setSender_id(message.getUser().getId());
        messageDto.setSender_name(message.getUser().getName());
        messageDto.setContent(message.getContent());
        messageDto.setTimestamp(message.getTimestamp().toString());
        messageDto.setType(message.getType());
        messageDto.setStatus(message.getStatus());
        return messageDto;
    }
}
