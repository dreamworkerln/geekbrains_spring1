package ru.geekbrains.spring.context.push;

import org.springframework.stereotype.Component;

@Component
public class SMSService implements MessageService{

    @Override
    public void send(String message, Priority hi) {

        // SMS can't handle/use priority

        System.out.println(String.format("Message send via SMS with message: '%1$s' and default priority",
                message));
    }
}
