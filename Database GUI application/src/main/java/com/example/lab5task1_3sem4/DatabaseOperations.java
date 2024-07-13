package com.example.lab5task1_3sem4;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.io.FileReader;

import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class DatabaseOperations {
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/for_task_1_1";
        String username = "root";
        String password = "root";
        return DriverManager.getConnection(url, username, password);
    }

    public static void importDataFromJSON(String jsonFilePath) throws SQLException {
        try (Connection connection = getConnection()) {
            JSONParser parser = new JSONParser();

            try (FileReader reader = new FileReader(jsonFilePath)) {
                Object obj = parser.parse(reader);
                JSONArray jsonArray = (JSONArray) obj;

                for (Object o : jsonArray) {
                    JSONObject jsonObject = (JSONObject) o;

                    // Перевірка типу даних для визначення таблиці
                    if (jsonObject.containsKey("date")) {
                        // Це запис для таблиці "lectures"
                        String date = (String) jsonObject.get("date");
                        String topic = (String) jsonObject.get("topic");
                        Long numberOfStudents = (Long) jsonObject.get("number_of_students");

                        // Виконуємо вставку даних в таблицю "lectures"
                        String sql = "INSERT INTO lectures (date, topic, number_of_students) VALUES (?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setString(1, date);
                        statement.setString(2, topic);
                        statement.setInt(3, numberOfStudents.intValue());
                        statement.executeUpdate();
                    } else if (jsonObject.containsKey("course_name")) {
                        // Це запис для таблиці "courses"
                        String courseName = (String) jsonObject.get("course_name");
                        String teacherLastName = (String) jsonObject.get("teacher_last_name");

                        // Виконуємо вставку даних в таблицю "courses"
                        String sql = "INSERT INTO courses (course_name, teacher_last_name) VALUES (?, ?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setString(1, courseName);
                        statement.setString(2, teacherLastName);
                        statement.executeUpdate();
                    } else {
                        System.err.println("Не вдалося визначити тип запису.");
                    }
                }
            } catch (SQLException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void displayLectures(TableView<Lecture> tableView) {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM lectures");

            while (resultSet.next()) {
                Lecture lecture = new Lecture(resultSet.getString("date"), resultSet.getString("topic"), resultSet.getInt("number_of_students"));
                tableView.getItems().add(lecture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void displayCourses(TableView<FirstEntityWithStreamAPI> tableView) {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM courses");

            while (resultSet.next()) {
                FirstEntityWithStreamAPI course = new FirstEntityWithStreamAPI(resultSet.getString("course_name"), resultSet.getString("teacher_last_name"));
                tableView.getItems().add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void searchByTopicKeyword(String keyword, ListView<String> listView) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM lectures WHERE topic LIKE ?");
            statement.setString(1, "%" + keyword + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String lectureInfo = "Date: " + resultSet.getString("date") + ", Topic: " + resultSet.getString("topic") + ", Number of Students: " + resultSet.getInt("number_of_students");
                listView.getItems().add(lectureInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void sortByTopic(TableView<Lecture> tableView) {
        tableView.getItems().clear(); // Очистить содержимое таблицы перед сортировкой
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM lectures ORDER BY topic");

            while (resultSet.next()) {
                Lecture lecture = new Lecture(resultSet.getString("date"), resultSet.getString("topic"), resultSet.getInt("number_of_students"));
                tableView.getItems().add(lecture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void addRecordToCourses(String courseName, String teacherLastName) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO courses (course_name, teacher_last_name) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, courseName);
            statement.setString(2, teacherLastName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRecordFromCourses(String courseName) {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM courses WHERE course_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, courseName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void exportDataToJSON(String jsonFilePath) {
        try (Connection connection = getConnection()) {
            JSONArray jsonArray = new JSONArray();

            // Exporting data from lectures table
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM lectures");
                while (resultSet.next()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("date", resultSet.getString("date"));
                    jsonObject.put("topic", resultSet.getString("topic"));
                    jsonObject.put("number_of_students", resultSet.getInt("number_of_students"));
                    jsonArray.add(jsonObject);
                }
            }

            // Exporting data from courses table
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM courses");
                while (resultSet.next()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("course_name", resultSet.getString("course_name"));
                    jsonObject.put("teacher_last_name", resultSet.getString("teacher_last_name"));
                    jsonArray.add(jsonObject);
                }
            }

            // Writing JSON array to file
            try (FileWriter fileWriter = new FileWriter(jsonFilePath)) {
                fileWriter.write(jsonArray.toJSONString());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }




}


