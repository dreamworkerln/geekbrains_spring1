package ru.geekbrains.spring.context.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AlarmSenderGSM implements AlarmSender {



    private MessageService messageService;


    // same thing with @Qualifier bean name specification when injecting via setter


    // Борьба аннотаций
    @Autowired
    //@Qualifier("SMSService")
    public void setMessageService(@Qualifier("SMSService") MessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public void sendAlarm(String message) {

        messageService.send(message, Priority.HI);
    }
}
