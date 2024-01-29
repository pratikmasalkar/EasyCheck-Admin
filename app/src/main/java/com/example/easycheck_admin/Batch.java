package com.example.easycheck_admin;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Batch {
    private String name;
    private String course;
    private Map<String, String> subjects = new HashMap<>();
    private Map<String, String> teachers = new HashMap<>();

    // Constructors (including a no-argument constructor for Firebase serialization)
    public Batch() {
    }

    public Batch(String name) {
        this.name = name;
    }

    public Batch(String name, String course, Map<String, String> subjects, Map<String, String> teachers) {
        this.name = name;
        this.course = course;
        this.subjects = subjects;
        this.teachers = teachers;
    }

    // Getters and setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Map<String, String> getSubjects() {
        return subjects;
    }

    public void setSubjects(Map<String, String> subjects) {
        this.subjects = subjects;
    }

    public Map<String, String> getTeachers() {
        return teachers;
    }

    public void setTeachers(Map<String, String> teachers) {
        this.teachers = teachers;
    }


    // Methods to manipulate or access data
    public void addSubject(String subjectName, String teacherName) {
        subjects.put(subjectName, teacherName);
    }

    public void removeSubject(String subjectName) {
        subjects.remove(subjectName);
    }

    // Other methods as needed

    public String getTeachersAsString() {
        StringBuilder teacherString = new StringBuilder();
        for (Map.Entry<String, String> entry : teachers.entrySet()) {
            teacherString.append(entry.getKey())
                    .append(" - ")
                    .append(entry.getValue())
                    .append("\n");
        }
        return teacherString.toString();
    }

    public String getSubjectsAsString() {
        return TextUtils.join(", ", teachers.values());
    }
}
