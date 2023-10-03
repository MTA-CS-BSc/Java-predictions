package fx.menu.controllers;

import consts.ThemeNames;
import consts.ThemePaths;
import fx.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    private Collection<Scene> scenes;
    private BooleanProperty isAnimationsOn;
    @FXML private MenuItem toggleAnimations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scenes = new ArrayList<>();
        isAnimationsOn = new SimpleBooleanProperty();

        isAnimationsOn.addListener((observableValue, aBoolean, t1) -> {
            toggleAnimations.setText(t1 ? "Toggle animations OFF" : "Toggle animations ON");
        });
    }

    public void setThemeToAllScenes(String cssPath) {
        scenes.forEach(scene -> {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(String.valueOf(Main.class.getResource(cssPath)));
        });
    }

    @FXML
    public void handleThemeChanged(ActionEvent event) {
        if (scenes.isEmpty())
            return;

        MenuItem selectedMenuItem = (MenuItem) event.getTarget();

        switch (selectedMenuItem.getText()) {
            case ThemeNames.DEFAULT:
                setThemeToAllScenes(ThemePaths.DEFAULT_THEME_CSS);
                break;
            case ThemeNames.DARK:
                setThemeToAllScenes(ThemePaths.DARK_THEME_CSS);
                break;
            case ThemeNames.WIN7:
                setThemeToAllScenes(ThemePaths.WIN7_THEME_CSS);
        }
    }

    @FXML
    private void handleToggleAnimations() {
        isAnimationsOn.setValue(!isAnimationsOn.getValue());
    }

    public void addScene(Scene scene) {
        scenes.add(scene);
    }

    public BooleanProperty isAnimationsOnProperty() {
        return isAnimationsOn;
    }
}
