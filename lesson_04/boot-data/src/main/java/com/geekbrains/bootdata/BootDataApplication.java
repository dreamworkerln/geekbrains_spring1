package com.geekbrains.bootdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootDataApplication {
	// Домашнее задание:
	// 0. Нужно перейти на Spring Boot + Spring Data
	// 1. Перейти на сущности 'Товар'
	// 2. Добавить уровень service
	// 3. Сделать главную страницу с отображением всех товаров
	// 4. Над таблицей сделать форму с возможностью запонения
	// фильтров и их применения

	// !!! В пулл реквесте очень желательно чтобы был только код,
	// относящийся к домашнему заданию

	public static void main(String[] args) {
		SpringApplication.run(BootDataApplication.class, args);
	}
}
