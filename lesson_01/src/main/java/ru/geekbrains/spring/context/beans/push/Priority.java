package ru.geekbrains.spring.context.beans.push;

public enum Priority {
    HI(0),
    LOW(1);

    private final int level;

    Priority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
