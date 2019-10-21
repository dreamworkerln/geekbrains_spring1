package ru.geekbrains.spring.context.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AlarmSenderGSM implements AlarmSender {



    private MessageService messageService;


    // same thing with @Qualifier bean name specification when injecting via setter


    // Обмажемся аннотациями (если указать разные, то не взлетит)
    @Autowired
    @Qualifier("fireBaseService")
    public void setMessageService(@Qualifier("fireBaseService") MessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public void sendAlarm(String message) {

        messageService.send(message, Priority.HI);
    }
}
