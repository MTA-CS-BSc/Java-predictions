package fx.threadpool.controllers;

import api.threadpool.HttpThreadpool;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
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
            amountTextArea.setText(Objects.isNull(amountResponse.body()) ? "ERROR" : amountResponse.body().string());
        } catch (IOException e) {
            amountTextArea.setText("ERROR");
        }
    }

    private void addNumericFilter(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) return;

            textArea.setText(newValue.replaceAll("[^\\d]", ""));
        });
    }
}
