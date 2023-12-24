package com.jester2204.best_app.controllers;

import com.jester2204.best_app.dao.DatabaseConnection;
import com.jester2204.best_app.dao.User;
import com.jester2204.best_app.dao.UserDao;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class MainController {
    public TextField randomUserIdField;
    public TextField randomUserNameField;
    public TextField randomUserPasswordField;
    public SVGPath passwordIcon;
    public Button settingsButton;

    @FXML private Rectangle rectangle;
    @FXML private Rectangle rectangle1;
    @FXML private Group group_left;
    @FXML private Group group_right;
    @FXML private TextField userIdField;
    @FXML private TextField userNameField;
    @FXML private TextField userPasswordField;

    private UserDao userDao;

    @FXML
    public void initialize() {
        Tooltip passwordTooltip = new Tooltip("At least 8 characters and at least one special character");
        passwordTooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(passwordIcon, passwordTooltip);

        try {
            this.userDao = new UserDao(DatabaseConnection.getConnection());
        } catch (Exception e) {
            showAlert("Database Connection Error", "Failed to connect to database: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void handleScale(Group group, Rectangle rectangle, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(600), rectangle);
        group.toFront();
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }
    @FXML
    private void leftMoreThenRight() {
        handleScale(group_left, rectangle, 1.4);
    }
    @FXML
    private void leftReturnToNormalSize() {
        handleScale(group_left, rectangle, 1.0);
    }
    @FXML
    private void rightMoreThenLeft() {
        handleScale(group_right, rectangle1, 1.4);
    }
    @FXML
    private void rightReturnToNormalSize() {
        handleScale(group_right, rectangle1, 1.0);
    }
    @FXML
    private void saveUser() {
        // Получаем данные из текстовых полей
        String userIdText = userIdField.getText();
        String userName = userNameField.getText();
        String userPassword = userPasswordField.getText();

        Integer userId = validateFields(userIdText, userName, userPassword);
        if (userId == null) return;

        // Создаем объект пользователя
        User user = new User(userId, userName, userPassword);

        // Сохраняем пользователя в базу данных
        try {
            userDao.saveUser(user);
            showAlert("Success", "User saved successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to save user: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void getRandomUser() {
        try {
            userDao.getRandomUser().ifPresent(user -> {
                randomUserIdField.setText(String.valueOf(user.getId()));
                randomUserNameField.setText(user.getName());
                randomUserPasswordField.setText(user.getPassword());
            });
        } catch (Exception e) {
            showAlert("Error", "Failed to get random user: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private Integer validateFields(String userIdText, String userName, String userPassword) {
        // Проверяем, что поля не пустые
        if (userIdText.isEmpty() || userName.isEmpty() || userPassword.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled!", Alert.AlertType.WARNING);
            return null;
        }

        // Проверяем, что пароль соответствует требованиям
        if (!isPasswordValid(userPassword)) {
            showAlert("Validation Error", "Password must be at least 8 characters long and contain at least one special character!", Alert.AlertType.WARNING);
            return null;
        }

        // Преобразуем ID пользователя в число
        int userId;
        try {
            userId = Integer.parseInt(userIdText);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "User ID must be a number!", Alert.AlertType.WARNING);
            return null;
        }
        return userId;
    }
    private boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            return false;
        }

        return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\|,.<>\\/?].*");
    }
    public void reconnectToDatabase() {
        try {
            userDao = new UserDao(DatabaseConnection.getConnection());
            showAlert("Database Connection Success", "Successes reconnect to database: ", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Database Connection Error", "Failed to reconnect to database: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openSettingsDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jester2204/best_app/settings_dialog.fxml"));
            Parent parent = fxmlLoader.load();
            SettingsController settingsController = fxmlLoader.getController();
            settingsController.setMainController(this);

            Scene scene = new Scene(parent, 300, 200);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Error", "Cannot load settings dialog: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}