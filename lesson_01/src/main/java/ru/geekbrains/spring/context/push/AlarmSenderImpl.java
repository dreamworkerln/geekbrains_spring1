package ru.geekbrains.spring.context.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class AlarmSenderImpl implements AlarmSender{

    private final MessageService messageService;

    @Autowired
    public AlarmSenderImpl(@Qualifier("fireBaseService") MessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public void sendAlarm(String message) {

        messageService.send(message, Priority.HI);
    }
}
