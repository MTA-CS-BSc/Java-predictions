package fx.header.controllers;

import fx.menu.controllers.MenuController;
import fx.navbar.controllers.NavbarController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {
    @FXML private NavbarController navbarController;
    @FXML private MenuController menuController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void addScene(Scene scene) {
        menuController.addScene(scene);
    }

    public void setThemeToAllScenes(String cssPath) {
        menuController.setThemeToAllScenes(cssPath);
    }
}
