package fx.navbar.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class NavbarController implements Initializable {
    private BooleanProperty isAnimationsOn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isAnimationsOn = new SimpleBooleanProperty();
    }

    public void setIsAnimationsOn(boolean isAnimationsOn) {
        this.isAnimationsOn.setValue(isAnimationsOn);
    }

}
