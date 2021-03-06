package ru.geekbrains.spring.context.beans;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.geekbrains.spring.context.AppStartupRunner;

import java.util.Objects;

public class Tiger {

    private static final Logger log = LoggerFactory.getLogger(AppStartupRunner.class);

    @Value("Tiger")
    protected String name;

    /* Почему статический анализатор не орет здесь:
    Field injection is not recommended less... (Ctrl+F1)
    Spring Team recommends: "Always use constructor based dependency injection in your beans.
    Always use assertions for mandatory dependencies".
    - ???
    */
    @Autowired
    private Color color;


    public Tiger() {
        System.out.println("Tiger constructor");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color= color;
    }

    @Override
    public String toString() {
        return "The '" + name + "' has " + color.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tiger)) return false;
        Tiger tiger = (Tiger) o;
        return Objects.equals(name, tiger.name) &&
               Objects.equals(color, tiger.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}