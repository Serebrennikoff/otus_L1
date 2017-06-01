package ru.otus.java.sample_objects;

/**
 * ...
 */
public class Student {
    private final String name;
    private final String surname;
    private final int age;
    private final String[] courses;
    private final char avgMark;

    public Student (String name, String surname, int age, String[] courses, char avgMark) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.courses = courses;
        this.avgMark = avgMark;
    }
}
