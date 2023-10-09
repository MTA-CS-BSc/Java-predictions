package fx.components.login;

import api.users.HttpUsers;
import consts.Alerts;
import consts.ConnectedUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import json.JsonParser;
import json.Keys;
import okhttp3.Response;
import types.TypesUtils;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private TextField usernameTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    @FXML
    private void handleSubmitClicked() {
        if (TypesUtils.isNullOrEmpty(usernameTextField.getText())) {
            Alerts.showAlert("ERROR", "Username is required!", Alert.AlertType.ERROR);
            return;
        }

        try {
            Response response = HttpUsers.createUser(usernameTextField.getText(), true);

            if (!response.isSuccessful()) {
                if (!Objects.isNull(response.body()))
                    Alerts.showAlert("ERROR", JsonParser.getMapFromJsonString(response.body().string()).get(Keys.INVALID_RESPONSE_KEY).toString(), Alert.AlertType.ERROR);

                response.close();
            }

            else {
                ConnectedUser.USERNAME_PROPERTY.setValue(usernameTextField.getText());
                //TODO: Navigate to Details
            }
        } catch (Exception e) {
            Alerts.showAlert("ERROR", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
