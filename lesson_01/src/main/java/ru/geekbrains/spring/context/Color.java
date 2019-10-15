package ru.geekbrains.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

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
    public String toString() {
        return "'" + baseColor + "' base skin color and '" + textureColor + "' texture color." ;

    }
}
