package ru.otus.java.sample_objects;

import java.util.List;

/**
 * ...
 */
public class Group {
    private final int groupNum;
    private final Student[] students;

    public Group (int groupNum, Student[] students) {
        this.groupNum = groupNum;
        this.students = students;
    }
}
