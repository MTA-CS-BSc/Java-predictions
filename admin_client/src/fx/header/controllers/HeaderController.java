package fx.header.controllers;

import fx.header.menu.controllers.MenuController;
import fx.header.navbar.controllers.NavbarController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {
    @FXML private NavbarController navbarController;
    @FXML private MenuController menuController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }
}
