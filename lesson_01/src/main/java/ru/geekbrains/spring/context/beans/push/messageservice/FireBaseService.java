package ru.geekbrains.spring.context.beans.push.messageservice;

import org.springframework.stereotype.Component;
import ru.geekbrains.spring.context.beans.push.Priority;

@Component("firebase")
public class FireBaseService implements MessageService{

    @Override
    public void send(String message, Priority priority) {
        System.out.println(String.format("Message send via FireBase with message: '%1$s' and priority: %2$d",
                message, priority.getLevel()));
    }
}
