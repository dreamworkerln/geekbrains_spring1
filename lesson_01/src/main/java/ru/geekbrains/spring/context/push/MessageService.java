package ru.geekbrains.spring.context.push;

public interface MessageService {

    void send(String message, Priority hi);
}
