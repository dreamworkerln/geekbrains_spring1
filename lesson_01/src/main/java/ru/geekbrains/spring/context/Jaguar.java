package ru.geekbrains.spring.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Scope("singleton")
public class Jaguar {

    private Color color;

    @Autowired
    public Jaguar(Color color) {
        this.color = color;
        System.out.println("Jaguar constructor");
    }


    @Override
    public String toString() {
        return "The jaguar has " + color.toString();
    }

    Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jaguar)) return false;
        Jaguar jaguar = (Jaguar) o;
        return Objects.equals(color, jaguar.color);
    }

    @Override
    public int hashCode() {

        return Objects.hash(color);
    }
}
