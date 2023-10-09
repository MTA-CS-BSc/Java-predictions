package fx.component.mgmt.threadpool.popup;

import api.mgmt.threadpool.HttpThreadpool;
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
            Response response = HttpThreadpool.getThreadsAmount();

            //TODO: Show error details
            if (!response.isSuccessful())
                response.close();

            if (!Objects.isNull(response.body()))
                amountTextArea.setText(JsonParser.objectMapper.readValue(response.body().string(), Integer.class).toString());
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

            //TODO: Show error details
            if (!response.isSuccessful())
                    response.close();
            else
                Alerts.showAlert("SUCCESS", "Threadpool count set to " + amount, Alert.AlertType.INFORMATION);

            //TODO: Show error details
            if (!response.isSuccessful() && !Objects.isNull(response.body()))
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
