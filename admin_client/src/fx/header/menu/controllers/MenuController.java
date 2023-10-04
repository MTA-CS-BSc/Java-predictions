package fx.header.menu.controllers;

import consts.Animations;
import consts.ThemeNames;
import consts.ThemePaths;
import fx.Main;
import fx.themes.ScenesStore;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML private MenuItem toggleAnimations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Animations.IS_ANIMATIONS_ON.addListener((observableValue, aBoolean, t1) -> {
            toggleAnimations.setText(t1 ? "Toggle animations OFF" : "Toggle animations ON");
        });

        ScenesStore.SCENES_PROPERTY.addListener((ListChangeListener<Scene>) change -> {
            while (change.next())
                if (change.wasAdded())
                    change.getAddedSubList().forEach(scene -> setThemeToScene(scene, ScenesStore.SELECTED_THEME_PATH.getValue()));

        });

        ScenesStore.SELECTED_THEME_PATH.addListener((observableValue, s, t1) -> setThemeToAllScenes(t1));
    }

    private void setThemeToScene(Scene scene, String cssPath) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(String.valueOf(Main.class.getResource(cssPath)));
    }

    public void setThemeToAllScenes(String cssPath) {
        ScenesStore.SCENES_PROPERTY.forEach(scene -> setThemeToScene(scene, cssPath));
    }

    @FXML
    public void handleThemeChanged(ActionEvent event) {
        if (ScenesStore.SCENES_PROPERTY.isEmpty())
            return;

        MenuItem selectedMenuItem = (MenuItem) event.getTarget();

        switch (selectedMenuItem.getText()) {
            case ThemeNames.DEFAULT:
                ScenesStore.SELECTED_THEME_PATH.setValue(ThemePaths.DEFAULT_THEME_CSS);
                break;
            case ThemeNames.DARK:
                ScenesStore.SELECTED_THEME_PATH.setValue(ThemePaths.DARK_THEME_CSS);
                break;
            case ThemeNames.WIN7:
                ScenesStore.SELECTED_THEME_PATH.setValue(ThemePaths.WIN7_THEME_CSS);
        }
    }

    @FXML
    private void handleToggleAnimations() {
        Animations.IS_ANIMATIONS_ON.setValue(!Animations.IS_ANIMATIONS_ON.getValue());
    }
}
