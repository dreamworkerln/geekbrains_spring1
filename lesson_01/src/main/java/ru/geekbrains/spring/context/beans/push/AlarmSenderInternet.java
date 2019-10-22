package ru.geekbrains.spring.context.beans.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.geekbrains.spring.context.beans.push.messageservice.MessageService;


@Component
public class AlarmSenderInternet implements AlarmSender{

    private final MessageService messageService;


    // same thing with @Qualifier bean name specification when injecting via setter
    @Autowired
    public AlarmSenderInternet(@Qualifier("firebase") MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void sendAlarm(String message) {
        messageService.send(message, Priority.HI);
    }
}
