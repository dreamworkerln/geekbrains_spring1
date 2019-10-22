package ru.geekbrains.spring.context.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import ru.geekbrains.spring.context.AppStartupRunner;

import java.util.Objects;

public class Color {

    private static final Logger log = LoggerFactory.getLogger(AppStartupRunner.class);

    @Value("white")
    private String baseColor;

    @Value("black")
    private String textureColor;

    public Color() {
        System.out.println("Color constructor");
    }

    public String getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(String baseColor) {
        this.baseColor = baseColor;
    }

    public String getTextureColor() {
        return textureColor;
    }

    public void setTextureColor(String textureColor) {
        this.textureColor = textureColor;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color)) return false;
        Color color = (Color) o;
        return Objects.equals(baseColor, color.baseColor) &&
               Objects.equals(textureColor, color.textureColor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(baseColor, textureColor);
    }

    @Override
    public String toString() {
        return "'" + baseColor + "' base skin color and '" + textureColor + "' texture color." ;

    }
}
