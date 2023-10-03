package fx.threadpool.command.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ThreadsAmountPopupController implements Initializable {
    @FXML private TextArea amountTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addNumericFilter(amountTextArea);
    }

    private void addNumericFilter(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) return;

            textArea.setText(newValue.replaceAll("[^\\d]", ""));
        });
    }
}
