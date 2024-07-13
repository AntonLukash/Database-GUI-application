package com.example.lab5task1_3sem4;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FirstEntityWithStreamAPI extends AbstractCourse{
    private List<Lecture> lectureList;

    public FirstEntityWithStreamAPI(String courseName, String teacherLastName, List<Lecture> lectureList) {
        super(courseName, teacherLastName);
        this.lectureList = lectureList;
    }
    public FirstEntityWithStreamAPI(String courseName, String teacherLastName) {
        super(courseName, teacherLastName);
    }
    @Override
    public Lecture[] getLectures() {
        return lectureList.toArray(new Lecture[0]);
    }
    @Override
    public void bubbleSortLecturesByTopicWordCountAscending() {
        lectureList = lectureList.stream()
                .sorted(Comparator.comparingInt(l -> l.getTopic().split(" ").length))
                .collect(Collectors.toList());
    }

    @Override
    public void insertionSortLecturesByTopicAlphabeticalOrder() {
        lectureList = lectureList.stream()
                .sorted(Comparator.comparing(Lecture::getTopic))
                .collect(Collectors.toList());
    }
}