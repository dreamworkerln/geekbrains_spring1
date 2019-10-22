package ru.geekbrains.spring.context.beans.push.messageservice;

import ru.geekbrains.spring.context.beans.push.Priority;

public interface MessageService {

    void send(String message, Priority hi);
}
