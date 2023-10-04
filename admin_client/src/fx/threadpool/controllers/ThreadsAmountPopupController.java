package fx.threadpool.controllers;

import api.threadpool.HttpThreadpool;
import consts.Alerts;
import fx.themes.ScenesStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import json.JsonParser;
import json.Keys;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ThreadsAmountPopupController implements Initializable {
    @FXML private TextArea amountTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addNumericFilter(amountTextArea);

        try {
            Response amountResponse = HttpThreadpool.getThreadsAmount();

            if (!Objects.isNull(amountResponse.body()))
                amountTextArea.setText(JsonParser.getMapFromJsonString(amountResponse.body().string()).get(Keys.VALID_RESPONSE_KEY).toString());
        } catch (IOException ignored) { }
    }

    private void addNumericFilter(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) return;

            textArea.setText(newValue.replaceAll("[^\\d]", ""));
        });
    }

    @FXML
    private void handleSetClicked(ActionEvent event) {
        int amount = Integer.parseInt(amountTextArea.getText());

        if (amount <= 0) {
            Alerts.showAlert("Invalid input", "Threads amount must be positive", Alert.AlertType.ERROR);
            return;
        }

        try {
            Response response = HttpThreadpool.setThreadsAmount(amount);

            if (response.isSuccessful())
                Alerts.showAlert("SUCCESS", "Threadpool count set to " + amount, Alert.AlertType.INFORMATION);

            else if (!Objects.isNull(response.body()))
                Alerts.showAlert("ERROR", response.body().string(), Alert.AlertType.ERROR);

            closePopup(event);
        } catch (Exception ignored) { }
    }

    private void closePopup(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        ScenesStore.SCENES_PROPERTY.remove(sourceNode.getScene());
        Stage stage = (Stage) sourceNode.getScene().getWindow();
        stage.close();
    }
}
