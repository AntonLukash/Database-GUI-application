package com.example.lab5task1_3sem4;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.*;
/**
 * Абстрактний клас, що представляє загальну інформацію про курс.
 */
public abstract class AbstractCourse {
    private String courseName;
    private String teacherLastName;

    public AbstractCourse(String courseName, String teacherLastName) {
        this.courseName = courseName;
        this.teacherLastName = teacherLastName;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacherLastName() {
        return teacherLastName;
    }

    public abstract Lecture[] getLectures();

    /**
     * Знайти лекцію з мінімальною кількістю студентів.
     *
     * @return Лекція з мінімальною кількістю студентів
     */
    public Lecture findLectureWithMinStudents() {
        return Arrays.stream(getLectures())
                .min(Comparator.comparingInt(Lecture::getNumberOfStudents))
                .orElse(null);
    }

    /**
     * Знайти лекції, в яких є ключове слово в темі.
     *
     * @param keyword Ключове слово для пошуку
     * @return Масив лекцій, які містять ключове слово в темі
     */
    public Lecture[] findLecturesWithTopicKeyword(String keyword) {
        Lecture[] lectures = getLectures();
        if (lectures.length == 0) {
            return null;
        }

        return Arrays.stream(lectures)
                .filter(lecture -> lecture.getTopic().contains(keyword))
                .toArray(size -> new Lecture[size]);
    }

    /**
     * Знайти останню літеру у прізвищі викладача.
     *
     * @return Остання літера в прізвищі викладача
     */
    public char findLastLetterInTeacherLastName() {
        String lastName = getTeacherLastName();
        if (lastName.isEmpty())
            return '\0';

        return lastName.charAt(lastName.length() - 1);
    }

    /**
     * Сортувати лекції за кількістю слів у темі (зростання) за методом бульбашкового сортування.
     */
    public void bubbleSortLecturesByTopicWordCountAscending() {
        Lecture[] lectures = getLectures();
        int n = lectures.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (lectures[j].getTopic().split(" ").length > lectures[j + 1].getTopic().split(" ").length) {
                    Lecture temp = lectures[j];
                    lectures[j] = lectures[j + 1];
                    lectures[j + 1] = temp;
                }
            }
        }
    }

    /**
     * Сортувати лекції за алфавітним порядком теми за методом вставки.
     */
    public void insertionSortLecturesByTopicAlphabeticalOrder() {
        Lecture[] lectures = getLectures();
        int n = lectures.length;

        for (int i = 1; i < n; i++) {
            Lecture key = lectures[i];
            int j = i - 1;

            while (j >= 0 && lectures[j].getTopic().compareTo(key.getTopic()) > 0) {
                lectures[j + 1] = lectures[j];
                j = j - 1;
            }
            lectures[j + 1] = key;
        }
    }

    /**
     * Перевизначений метод toString для виводу інформації про курс.
     *
     * @return Рядок, що містить назву курсу та прізвище викладача
     */
    @Override
    public String toString() {
        return "Назва курсу: " + courseName + ", Прізвище викладача: " + teacherLastName;
    }

    /**
     * Перевизначений метод equals для порівняння двох об'єктів AbstractCourse.
     *
     * @param o Об'єкт для порівняння
     * @return true, якщо об'єкти однакові, інакше - false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCourse that = (AbstractCourse) o;
        return courseName.equals(that.courseName) &&
                teacherLastName.equals(that.teacherLastName);
    }

    /**
     * Перевизначений метод hashCode для обчислення хеш-коду об'єкта AbstractCourse.
     *
     * @return Хеш-код об'єкта
     */
    @Override
    public int hashCode() {
        return Objects.hash(courseName, teacherLastName);
    }
}


