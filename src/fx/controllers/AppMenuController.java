package fx.controllers;

import fx.consts.ThemeNames;
import fx.consts.StyleSheetsPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class AppMenuController implements Initializable {
    @FXML
    private Menu themeMenu;

    private Collection<Scene> scenes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scenes = new ArrayList<>();
    }

    public void setThemeToAllScenes(String cssPath) {
        scenes.forEach(scene -> {
           scene.getStylesheets().clear();
           scene.getStylesheets().add(String.valueOf(getClass().getResource(cssPath)));
        });
    }

    @FXML
    public void handleThemeChanged(ActionEvent event) {
        if (scenes.isEmpty())
            return;

        MenuItem selectedMenuItem = (MenuItem) event.getTarget();

        switch (selectedMenuItem.getText()) {
            case ThemeNames.DEFAULT:
                setThemeToAllScenes(StyleSheetsPaths.DEFAULT_THEME_CSS);
                break;
            case ThemeNames.DARK:
                setThemeToAllScenes(StyleSheetsPaths.DARK_THEME_CSS);
                break;
            case ThemeNames.WIN7:
                setThemeToAllScenes(StyleSheetsPaths.WINDOWS_7_THEME_CSS);
        }
    }

    public void addScene(Scene scene) {
        scenes.add(scene);
    }
}
