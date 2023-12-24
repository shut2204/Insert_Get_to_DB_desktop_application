package com.jester2204.best_app.controllers;

import com.jester2204.best_app.dao.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private TextField dbUrlField;
    @FXML
    private TextField dbUserField;
    @FXML
    private PasswordField dbPasswordField;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void applySettings() {
        String url = dbUrlField.getText();
        String user = dbUserField.getText();
        String password = dbPasswordField.getText();

        try {
            DatabaseConnection.setCredentials(url, user, password);
            mainController.reconnectToDatabase();
            closeDialog();
        } catch (Exception e) {
            // Показать сообщение об ошибке
        }
    }


    @FXML
    private void closeDialog() {
        Stage stage = (Stage) dbUrlField.getScene().getWindow();
        stage.close();
    }
}
