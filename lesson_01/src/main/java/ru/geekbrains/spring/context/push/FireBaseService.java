package ru.geekbrains.spring.context.push;

import org.springframework.stereotype.Component;

@Component
public class FireBaseService implements MessageService{

    @Override
    public void send(String message, Priority priority) {
        System.out.println(String.format("Message send via FireBase with message: '%1$s' and priority: %2$d",
                message, priority.getLevel()));
    }
}
