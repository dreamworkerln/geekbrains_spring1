package ru.geekbrains.spring.context.beans.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.geekbrains.spring.context.beans.push.messageservice.MessageService;

@Component
public class AlarmSenderGSM implements AlarmSender {



    private MessageService messageService;


    // same thing with @Qualifier bean name specification when injecting via setter


    // Обмажемся аннотациями @Qualifier(если указать разные, то не взлетит)
    @Autowired
    @Qualifier("sms")
    public void setMessageService(@Qualifier("sms") MessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public void sendAlarm(String message) {
        messageService.send(message, Priority.HI);
    }
}
