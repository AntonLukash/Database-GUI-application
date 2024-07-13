package com.example.lab5task1_3sem4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import javafx.scene.control.ListView;

import static com.example.lab5task1_3sem4.DatabaseOperations.importDataFromJSON;

public class DatabaseGUI extends Application {
    TableView<FirstEntityWithStreamAPI> courseTable;
    TableView<Lecture> lectureTable;

    // Метод для створення головного вікна програми
    @Override
    public void start(Stage primaryStage) {
        lectureTable = new TableView<>();
        TableColumn<Lecture, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Lecture, String> topicColumn = new TableColumn<>("Topic");
        topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        TableColumn<Lecture, Integer> studentsColumn = new TableColumn<>("Number of Students");
        studentsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfStudents"));

        lectureTable.getColumns().addAll(dateColumn, topicColumn, studentsColumn);

        courseTable = new TableView<>();
        TableColumn<FirstEntityWithStreamAPI, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        TableColumn<FirstEntityWithStreamAPI, String> teacherColumn = new TableColumn<>("Teacher Last Name");
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacherLastName"));

        courseTable.getColumns().addAll(courseNameColumn, teacherColumn);

        // Область відображення результатів пошуку
        VBox searchResultsBox = new VBox();
        Label searchResultsLabel = new Label("Search Results:");
        ListView<String> searchResultsListView = new ListView<>();
        searchResultsBox.getChildren().addAll(searchResultsLabel, searchResultsListView);

        VBox leftPanel = new VBox();
        Button loadLecturesButton = new Button("Load Lectures");
        loadLecturesButton.setOnAction(e -> loadLectures(lectureTable));

        Button loadCoursesButton = new Button("Load Courses");
        loadCoursesButton.setOnAction(e -> loadCourses(courseTable));

        Button ImportDataButton = new Button("Import Data");
        ImportDataButton.setOnAction(e -> {
            try {
                importDataFromJSON("data.json");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button addRecordButton = new Button("Add Record(course)");
        addRecordButton.setOnAction(event -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Add Record");
            dialog.setHeaderText("Enter course details:");

            Label courseNameLabel = new Label("Course Name:");
            Label teacherLastNameLabel = new Label("Teacher Last Name:");
            TextField courseNameField = new TextField();
            TextField teacherLastNameField = new TextField();

            VBox content = new VBox();
            content.getChildren().addAll(courseNameLabel, courseNameField, teacherLastNameLabel, teacherLastNameField);
            dialog.getDialogPane().setContent(content);

            ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == addButton) {
                    return courseNameField.getText() + "," + teacherLastNameField.getText();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(result -> {
                String[] parts = result.split(",");
                DatabaseOperations.addRecordToCourses(parts[0], parts[1]);
                loadCourses(courseTable);
            });
        });

        Button deleteRecordButton = new Button("Delete Record(course)");

        deleteRecordButton.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Delete Record");
            dialog.setHeaderText("Enter course name:");
            dialog.showAndWait().ifPresent(courseName -> {
                DatabaseOperations.deleteRecordFromCourses(courseName);
                loadCourses(courseTable);
            });
        });

        Button searchButton = new Button("Search lecture");

        searchButton.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search");
            dialog.setHeaderText("Enter keyword:");
            dialog.showAndWait().ifPresent(keyword -> {
                searchResultsListView.getItems().clear(); // Очистить содержимое списка результатов поиска
                DatabaseOperations.searchByTopicKeyword(keyword, searchResultsListView); // Заполнить результаты поиска
            });
        });

        Button sortByTopicButton = new Button("Sort by Topic");

        sortByTopicButton.setOnAction(event -> DatabaseOperations.sortByTopic(lectureTable));

        Button exportData = new Button("Export Data to Json");

        exportData.setOnAction(event -> DatabaseOperations.exportDataToJSON("export_data.json"));

        leftPanel.getChildren().addAll(ImportDataButton, loadLecturesButton, loadCoursesButton, addRecordButton, deleteRecordButton, searchButton, sortByTopicButton, exportData);


        HBox mainLayout = new HBox(10);
        mainLayout.getChildren().addAll(leftPanel, lectureTable, courseTable, searchResultsBox);

        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Database GUI");
        primaryStage.show();
    }
    // Метод для завантаження лекцій у таблицю
    private void loadLectures(TableView<Lecture> tableView) {
        tableView.getItems().clear(); // Очищення таблиці перед завантаженням нових даних
        DatabaseOperations.displayLectures(lectureTable);
    }

    // Метод для завантаження курсів у таблицю
    private void loadCourses(TableView<FirstEntityWithStreamAPI> tableView) {
        tableView.getItems().clear(); // Очищення таблиці перед завантаженням нових даних
        DatabaseOperations.displayCourses(courseTable);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

