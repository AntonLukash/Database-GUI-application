package com.example.lab5task1_3sem4;

/**
 * Клас, який представляє лекцію з інформацією про дату, тему та кількість студентів.
 */
public class Lecture implements Comparable<Lecture> {
    private String date;

    private String topic;
    private int numberOfStudents;

    public Lecture(String date, String topic, int numberOfStudents) {
        this.date = date;
        this.topic = topic;
        this.numberOfStudents = numberOfStudents;
    }

    public String getDate() {
        return date;
    }

    public String getTopic() {
        return topic;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    @Override
    public String toString() {
        return "Дата: " + date + ", Тема: " + topic + ", Кількість студентів: " + numberOfStudents;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Lecture lecture = (Lecture) obj;

        if (numberOfStudents != lecture.numberOfStudents) return false;
        if (!date.equals(lecture.date)) return false;
        return topic.equals(lecture.topic);
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + topic.hashCode();
        result = 31 * result + numberOfStudents;
        return result;
    }

    /**
     * Перевизначений метод compareTo для порівняння лекцій за алфавітом теми.
     *
     * @param other Інша лекція для порівняння
     * @return Результат порівняння за алфавітом теми
     */
    @Override
    public int compareTo(Lecture other) {
        return this.topic.compareTo(other.topic);
    }
}


